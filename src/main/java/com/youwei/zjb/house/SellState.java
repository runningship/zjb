package com.youwei.zjb.house;

import org.bc.sdak.utils.LogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum SellState {
	在售(4),
	已售(6),
	停售(7);
//	未知(8);
	private int code;
	
	private SellState( int code){
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public String getCodeString() {
		return String.valueOf(code);
	}
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(SellState state : SellState.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", state.name());
			jobj.put("name", state.name());
			jobj.put("code", state.code);
			arr.add(jobj);
		}
		return arr;
	}
	
	public static SellState parse(String code){
		try{
			int codec = Integer.valueOf(code);
			for(SellState zx : SellState.values()){
				if(zx.code==codec){
					return zx;
				}
			}
		}catch(Exception ex){
			//LogUtil.warning(""+ code);
		}
		return null;
	}
}
