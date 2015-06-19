package com.youwei.zjb;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.bc.web.ThreadSession;

import com.youwei.zjb.house.entity.HouseOwner;
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
    
    public static String getCityPinyin(){
    	//先从ThreadLocal中取，没有再从HttpSession中取
    	String city = ThreadSession.getCityPY();
    	if(StringUtils.isNotEmpty(city)){
    		return city;
    	}else{
    		return "hefei";
    	}
//    	city = (String)ThreadSessionHelper.getUser().cityPinyin;
//		if(StringUtils.isEmpty(city)){
//			city="hefei";
//		}
//		return city;
    }
    
    /**
     * @return 城市坐标
     * 合肥老客户端为设置城市信息，则默认返回合肥市坐标
     */
    public static String getCityCordinate(){
    	String cityCoordinate = ThreadSession.getCityCoordinate();
		if(StringUtils.isEmpty(cityCoordinate)){
			cityCoordinate="117.23355, 31.827258";
		}
		return cityCoordinate;
    }
    
    public static String getHouseOwnerCity(){
    	HouseOwner owner = (HouseOwner) ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_House_Owner);
    	return owner.city;
//    	String city = (String)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_House_Owner_City);
//    	if(StringUtils.isNotEmpty(city)){
//    		return city;
//    	}
//    	String cityInCookie = null;
//    	Cookie[] cookies = ThreadSession.HttpServletRequest.get().getCookies();
//    	if(cookies==null){
//    		System.out.println("cookie is null");
//    		return null;
//    	}
//    	for(Cookie cookie : cookies){
//			if(KeyConstants.Session_House_Owner_City.equals(cookie.getName())){
//				cityInCookie = cookie.getValue();
//			}
//		}
//    	ThreadSession.getHttpSession().setAttribute(KeyConstants.Session_House_Owner_City , city);
//    	return cityInCookie;
    }
    
}
