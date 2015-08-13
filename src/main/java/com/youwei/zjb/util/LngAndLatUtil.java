package com.youwei.zjb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class LngAndLatUtil {
	
	public static Map<String, String> cityMap = new HashMap<String,String>();
	static{
		cityMap.put("hefei", "合肥市");
	}
	public static void main(String[] args){
		Map<String,Double> map=LngAndLatUtil.getLngAndLat("阜阳市和谐家苑" , "");
		System.out.println("经度："+map.get("lng")+"---纬度："+map.get("lat"));
	}
	
	public static Map<String,Double> getLngAndLat(String address ,String cityPy){
		Map<String,Double> map=new HashMap<String, Double>();
		try{
		 String url = "http://api.map.baidu.com/geocoder/v2/?address="+address+"&output=json&ak=9ad26b763c7cd0619e372f993cdc9849&city="+cityMap.get(cityPy);
	        String json = loadJSON(url);
	        JSONObject obj = JSONObject.fromObject(json);
	        if(obj.get("status").toString().equals("0")){
	        	double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
	        	double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
	        	map.put("lng", lng);
	        	map.put("lat", lat);
	        	//System.out.println("经度："+lng+"---纬度："+lat);
	        }else{
	        	//System.out.println("未找到相匹配的经纬度！");
	        }
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return map;
	}
	
	 public static String loadJSON (String url) {
	        StringBuilder json = new StringBuilder();
	        try {
	            URL oracle = new URL(url);
	            URLConnection yc = oracle.openConnection();
	            BufferedReader in = new BufferedReader(new InputStreamReader(
	                                        yc.getInputStream()));
	            String inputLine = null;
	            while ( (inputLine = in.readLine()) != null) {
	                json.append(inputLine);
	            }
	            in.close();
	        }catch (IOException e) {
	        }
	        return json.toString();
	    }
}