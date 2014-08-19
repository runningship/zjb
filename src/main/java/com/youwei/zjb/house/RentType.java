package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum RentType {
	整租(1),
	合租(2);
	private int code;
	
	private RentType( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(RentType state : RentType.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", state.name());
			jobj.put("name", state.name());
			jobj.put("code", state.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static RentType parse(int codec){
		for(RentType zx : RentType.values()){
			if(zx.code==codec){
				return zx;
			}
		}
		return null;
	}
}
