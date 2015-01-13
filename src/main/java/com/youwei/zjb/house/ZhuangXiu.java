package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum ZhuangXiu {

	毛坯,
	简装,
	中装,
	精装,
	豪装,
	老式简装,
	老式中装,
	老式精装,
	老式豪装;
	
//	毛坯,简装,中装,精装,豪装,老式简装,老式中装,老式精装,老式豪装
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(ZhuangXiu zx : ZhuangXiu.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", zx.name());
			jobj.put("name", zx.name());
			arr.add(jobj);
		}
		return arr;
	}
	
	public static List<Map<String,Object>> toList(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(ZhuangXiu state : ZhuangXiu.values()){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", state.name());
			list.add(map);
		}
		return list;
	}
}
