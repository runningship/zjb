package com.youwei.zjb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.bc.sdak.utils.LogUtil;

import com.youwei.zjb.sys.CityService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class LngAndLatUtil {
	
	public static Map<String, String> cityMap = new HashMap<String,String>();
	static{
		CityService cs = new CityService();
		JSONArray citys = cs.getCitys();
		for(int i=0;i<citys.size();i++){
			JSONObject jobj = citys.getJSONObject(i);
			cityMap.put(jobj.getString("py"), jobj.getString("name"));
		}
	}
	public static void main(String[] args){
		Map<String,Double> map=LngAndLatUtil.getLngAndLat("南瑞锦秋园" , "wuhu");
		System.out.println("经度："+map.get("lng")+"---纬度："+map.get("lat"));
	}
	
	public static Map<String,Double> getLngAndLat(String address ,String cityPy){
		Map<String,Double> map=new HashMap<String, Double>();
		address = address.replace(" ", "");
		String cityName = cityMap.get(cityPy)+"市";
		String url ="";
		String json = "";
		try{
//			address = URLEncoder.encode(address, "utf-8");
//			cityName = URLEncoder.encode(cityName, "utf-8");
			url = "http://api.map.baidu.com/geocoder/v2/?address="+cityName+address+"&output=json&ak=9ad26b763c7cd0619e372f993cdc9849";
//			url = "http://api.map.baidu.com/geocoder/v2/?address=%E6%9C%9D%E9%98%B3%E6%96%B0%E6%9D%91&output=json&ak=9ad26b763c7cd0619e372f993cdc9849&city=%E8%8A%9C%E6%B9%96%E5%B8%82";
	        json = loadJSON(url);
	        JSONObject obj = JSONObject.fromObject(json);
	        if(obj.get("status").toString().equals("0")){
	        	double lng=obj.getJSONObject("result").getJSONObject("location").getDouble("lng");
	        	double lat=obj.getJSONObject("result").getJSONObject("location").getDouble("lat");
	        	map.put("lng", lng);
	        	map.put("lat", lat);
	        	//System.out.println("经度："+lng+"---纬度："+lat);
	        }else{
	        	LogUtil.info("获取楼盘["+address+"]经纬度失败,url = "+url +" ,result = "+json);
	        	//System.out.println("未找到相匹配的经纬度！");
	        }
		}catch(Exception ex){
			LogUtil.info("获取楼盘["+address+"]经纬度失败,url = "+url +" ,result = "+json);
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
