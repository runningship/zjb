package com.youwei.zjb;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.bc.web.ThreadSession;

import com.youwei.zjb.user.entity.User;

public class ThreadSessionHelper {

	public static User getUser(){
    	HttpSession session = ThreadSession.getHttpSession();
    	if(session==null){
    		return null;
    	}
    	return (User)session.getAttribute("user");
    }
    public static String getIp(){
    	HttpSession session = ThreadSession.getHttpSession();
    	return (String)session.getAttribute("ip");
    }
    
    public static String getDomain(){
    	if(ThreadSessionHelper.getUser()==null){
    		return "hefei";
    	}
    	String domain = (String)ThreadSessionHelper.getUser().domain;
		if(StringUtils.isEmpty(domain)){
			domain="hefei";
		}
		return domain;
    }
    
    /**
     * @return 城市坐标
     */
    public static String getCity(){
    	if(ThreadSessionHelper.getUser()==null){
    		//合肥
    		return "117.23355, 31.827258";
    	}
    	String city = (String)ThreadSessionHelper.getUser().city;
		if(StringUtils.isEmpty(city)){
			city="117.23355, 31.827258";
		}
		return city;
    }
}
