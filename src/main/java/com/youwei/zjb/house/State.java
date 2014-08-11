package com.youwei.zjb.house;

import org.bc.sdak.utils.LogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum State {

	在售(4),
	已售(6),
	停售(7);
	private int code;
	
	private State( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(State state : State.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", state.name());
			jobj.put("name", state.name());
			jobj.put("code", state.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static State parse(String code){
		try{
			int codec = Integer.valueOf(code);
			for(State zx : State.values()){
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
