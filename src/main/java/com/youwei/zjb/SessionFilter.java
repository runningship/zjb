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
import org.bc.sdak.SimpDaoTool;
import org.bc.web.ThreadSession;

import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.user.MobileUserDog;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.user.entity.UserSession;
import com.youwei.zjb.util.SessionHelper;

public class SessionFilter implements Filter{
	private static List<String> excludes= new ArrayList<String>();
	public static Map<String,Integer> calls = new HashMap<String,Integer>();
	static{
		excludes.add("/c/user/login");
		excludes.add("/c/user/logout");
		excludes.add("/c/pc/add");
		excludes.add("/c/dept/listDept");
		excludes.add("/login/index.html");
		excludes.add("/login/iframe_reg.html");
		excludes.add("/login/index.jsp");
		excludes.add("/login/iframe_reg.jsp");
		excludes.add("/welcome.html");
		excludes.add("/welcome2.html");
		excludes.add("/v/start.html");
		excludes.add("/version.jsp");
		excludes.add("/start.html");
		excludes.add("/c/city/list");
		excludes.add("/v/pay/return_url.html");
		excludes.add("/c/feedback/reportError");
		excludes.add("/v/pay/submit.html");
		excludes.add("/v/swq/index_58.html");
		excludes.add("/swq/index_58.html");
		
		excludes.add("/swq/index_58.html");
		excludes.add("/v/swq/index_58.html");
		
		excludes.add("/v/swq/index_365_hefei.html");
		excludes.add("/swq/index_365_hefei.html");
		excludes.add("/v/swq/index_365_wuhu.html");
		excludes.add("/swq/index_365_wuhu.html");
		
		excludes.add("/swq/index_ganji.html");
		excludes.add("/v/swq/index_ganji.html");
		
		excludes.add("/swq/login/index.html");
		excludes.add("/c/swq/add");
		excludes.add("/c/swq/login");
		excludes.add("/swq/jump.html");
		excludes.add("/c/house/rent/listNoTel");
		excludes.add("/c/house/rent/updateTel");
		excludes.add("/c/trial/add");
		
		excludes.add("/bosh/basic.html");
		excludes.add("/bosh-test/basic.html");
		excludes.add("/public/");
//		excludes.add("/c/house/exist");
	}
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;
		ThreadSession.HttpServletRequest.set(req);
		req.setCharacterEncoding("utf-8");
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
//		System.out.println("----------->clientId="+clientId+",path="+path+",oldSession="+oldSessionId+",session="+session.getId());
		ThreadSession.setHttpSession(session);
		if(excludes.contains(path)){
			chain.doFilter(request, response);
			return;
		}
		if(path.contains("houseOwner") || path.contains("/public/")){
			String cityPy = ThreadSessionHelper.getHouseOwnerCity(req);
			ThreadSession.setCityPY(cityPy);
			chain.doFilter(request, response);
			return;
		}
		if(path.contains("/mobile")){
//			ThreadSession.setCityPY(city);
			String cityPy = request.getParameter("cityPy");
			ThreadSession.setCityPY(cityPy);
			request.getCharacterEncoding();
			String deviceId = request.getParameter("deviceId");
			String tel = request.getParameter("tel");
			if(StringUtils.isNotEmpty(tel)){
				if(!path.contains("login") && !path.contains("logout") && !path.contains("sendVerifyCode") && !path.contains("verifyCode")){
					if(MobileUserDog.loginFromOther(tel, deviceId)){
						resp.setStatus(400);
						resp.getWriter().print("loginFromOther");
						return;
					}
				}
			}
			chain.doFilter(request, response);
			return;
		}
		if(ThreadSessionHelper.getUser()==null){
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
		User me = ThreadSessionHelper.getUser();
		ThreadSession.setCityPY(me.cityPinyin);
		if(path.endsWith(".jsp")){
			StringBuilder auths = new StringBuilder();
			if(me.getRole()!=null){
				for(RoleAuthority ra : me.getRole().Authorities()){
					auths.append(ra.name).append(",");
				}
			}
			req.setAttribute("user", me);
			req.setAttribute("auths", auths.toString());
		}
		
		ThreadSession.setCityCoordinate(me.cityCoordinate);
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
