package com.youwei.zjb.biz;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum LeaveStatus {
	未审批(0),
	通过(1),
	未通过(2);
	
	private LeaveStatus(int code){
		this.code = code;
	}
	private int code;
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(LeaveStatus state : LeaveStatus.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", String.valueOf(state.code));
			jobj.put("name", state.name());
			arr.add(jobj);
		}
		return arr;
	}
}


