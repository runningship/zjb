package com.youwei.zjb;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.user.entity.User;
public class ThreadSession {

	private static final ThreadLocal<HttpSession> HttpSession = new ThreadLocal<HttpSession>();
	
	private static final ThreadLocal<Boolean> superAdmin = new ThreadLocal<Boolean>();
    private ThreadSession() {  
    }  
  
    public static boolean isSuperAdmin() {  
        return superAdmin.get(); 
    }  
  
    public static void setSuperAdminr(boolean sup) {  
    	superAdmin.set(sup);
    }
    
    public static HttpSession getHttpSession(){
    	return HttpSession.get();
    }
    
    public static void setHttpSession(HttpSession session){
    	HttpSession.set(session);
    }
    
    public static User getUser(){
    	HttpSession session = HttpSession.get();
    	if(session==null){
    		return null;
    	}
    	return (User)session.getAttribute("user");
    }
    public static String getIp(){
    	HttpSession session = HttpSession.get();
    	return (String)session.getAttribute("ip");
    }
    
    public static String getDomain(){
    	if(ThreadSession.getUser()==null){
    		return "hefei";
    	}
    	String domain = (String)ThreadSession.getUser().domain;
		if(StringUtils.isEmpty(domain)){
			domain="hefei";
		}
		return domain;
    }
    
    public static String getCity(){
    	if(ThreadSession.getUser()==null){
    		//合肥
    		return "117.23355, 31.827258";
    	}
    	String city = (String)ThreadSession.getUser().city;
		if(StringUtils.isEmpty(city)){
			city="117.23355, 31.827258";
		}
		return city;
    }
}
