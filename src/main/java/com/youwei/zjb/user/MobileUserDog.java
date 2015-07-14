package com.youwei.zjb.user;

import java.util.HashMap;
import java.util.Map;

public class MobileUserDog {

	//tel-->deviceId
	public static Map<String,String> map = new HashMap<String,String>();
	
	public static boolean loginFromOther(String tel, String deviceId){
		if(!map.containsKey(tel)){
			return false;
		}
		if(map.get(tel).equals(deviceId)){
			return false;
		}
		return true;
	}
}
