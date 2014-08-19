package com.youwei.zjb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
	static{
		excludes.add("/c/user/login");
		excludes.add("/c/pc/add");
		excludes.add("/c/dept/listDept");
		excludes.add("/login/index.html");
		excludes.add("/login/iframe_reg.html");
		excludes.add("/welcome.html");
		excludes.add("/start.html");
		excludes.add("/c/city/list");
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		String path = req.getRequestURI().toString();
		System.out.println(path);
		HttpSession session = req.getSession();
		ThreadSession.setHttpSession(session);
		if(excludes.contains(path)){
			chain.doFilter(request, response);
			return;
		}
		
		if(session.isNew()){
			//has an old session id?
			String oldSessionId = getClientSid(req);
			if(StringUtils.isEmpty(oldSessionId)){
				//a fresh connection
			}else{
				// update session
				UserSession us = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(UserSession.class, "sessionId", oldSessionId);
				if(us==null){
					us = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(UserSession.class, "sessionId", session.getId());
					if(us==null){
						//have to login again.
						relogin(req,resp);
						return;
					}else{
						System.out.println("已经成功更新session");
					}
				}else{
					if(us.userId==null){
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
			}
		}else{
			if("true".equals(session.getAttribute("relogin"))){
				
			}else{
				if(session.getAttribute("user")==null){
					relogin(req, resp);
					return;
				}
			}
			
		}
		chain.doFilter(request, response);
	}

	private void relogin(HttpServletRequest req , HttpServletResponse resp) throws IOException{
		req.getSession().setAttribute("relogin", "true");
//		resp.setContentType("text/javascript");
		resp.getWriter().write("<script type='text/javascript'>window.parent.location="+req.getContextPath()+"'/login/index.html'</script>");
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
}
