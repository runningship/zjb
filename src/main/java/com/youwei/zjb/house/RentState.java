package com.youwei.zjb.house;

import org.bc.sdak.utils.LogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum RentState {
	在租(1),
	已租(2),
	停租(3);
	private int code;
	
	private RentState( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(RentState state : RentState.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", state.name());
			jobj.put("name", state.name());
			jobj.put("code", state.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static RentState parse(String code){
		try{
			int codec = Integer.valueOf(code);
			for(RentState zx : RentState.values()){
				if(zx.code==codec){
					return zx;
				}
			}
		}catch(Exception ex){
			LogUtil.warning("state with error code "+ code);
		}
		return null;
	}
}
