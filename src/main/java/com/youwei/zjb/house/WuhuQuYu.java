package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum WuhuQuYu {
	镜湖区,
	鸠江区,
	弋江区,
	三山区,
	开发区,
	芜湖县,
	繁昌县,
	南陵县,
	无为县;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(WuhuQuYu cx : WuhuQuYu.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", cx.name());
			jobj.put("value", cx.name());
			arr.add(jobj);
		}
		return arr;
	}
}
