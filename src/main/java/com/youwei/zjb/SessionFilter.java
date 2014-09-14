package com.youwei.zjb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.user.entity.UserSession;
import com.youwei.zjb.util.SessionHelper;

public class SessionFilter implements Filter{
	private static List<String> excludes= new ArrayList<String>();
	public static Map<String,Integer> calls = new HashMap<String,Integer>();
	static{
		excludes.add("/c/user/login");
		excludes.add("/c/pc/add");
		excludes.add("/c/dept/listDept");
		excludes.add("/login/index.html");
		excludes.add("/login/iframe_reg.html");
		excludes.add("/welcome.html");
		excludes.add("/welcome2.html");
		excludes.add("/v/start.html");
		excludes.add("/start.html");
		excludes.add("/c/city/list");
		excludes.add("/v/pay/return_url.html");
		excludes.add("/c/feedback/reportError");
		excludes.add("/v/pay/submit.html");
		
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String path = req.getRequestURI().toString();
		HttpSession session = req.getSession();
		String oldSessionId = getClientSid(req);
		String clientId = getClientID(req);
		if(!calls.containsKey(path)){
			calls.put(path,0);
		}
		Integer count = calls.get(path);
		count++;
		calls.put(path, count);
		System.out.println("----------->clientId="+clientId+",path="+path+",oldSession="+oldSessionId+",session="+session.getId());
		ThreadSession.setHttpSession(session);
		if(excludes.contains(path)){
			chain.doFilter(request, response);
			return;
		}
		if(ThreadSession.getUser()==null){
			if(StringUtils.isEmpty(oldSessionId)){
				relogin(req,resp);
				return;
			}
			UserSession us = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(UserSession.class, "sessionId", oldSessionId);
			if(us==null){
				relogin(req,resp);
				return;
			}
			User user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, us.userId);
			if(user==null){
				SimpDaoTool.getGlobalCommonDaoService().delete(us);
				relogin(req,resp);
				return;
			}
			SessionHelper.initHttpSession(session,user , us);
		}
		
//		if(session.isNew()){
//			
//		}else{
//			if("true".equals(session.getAttribute("relogin"))){
//				
//			}else{
//				if(session.getAttribute("user")==null){
//					relogin(req, resp);
//					return;
//				}
//			}
//			
//		}
		chain.doFilter(request, response);
	}

	private void relogin(HttpServletRequest req , HttpServletResponse resp) throws IOException{
		req.getSession().setAttribute("relogin", "true");
//		resp.setContentType("text/javascript");
		resp.getWriter().write("<script action='relogin' type='text/javascript'>window.parent.location="+req.getContextPath()+"'/login/index.html'</script>");
//		resp.sendRedirect(req.getContextPath()+"/login/index.html");
	}
	
	@Override
	public void destroy() {
		
	}

	private String getClientSid(HttpServletRequest req){
		String oldSessionId = "";
		if(req.getCookies()==null){
			return "";
		}
		for(Cookie coo : req.getCookies()){
			if("JSESSIONID".equals(coo.getName())){
				oldSessionId = coo.getValue();
				break;
			}
		}
		return oldSessionId;
	}
	
	private String getClientID(HttpServletRequest req){
		String oldSessionId = "";
		if(req.getCookies()==null){
			return "";
		}
		for(Cookie coo : req.getCookies()){
			if("client_id".equals(coo.getName())){
				oldSessionId = coo.getValue();
				break;
			}
		}
		return oldSessionId;
	}
}
