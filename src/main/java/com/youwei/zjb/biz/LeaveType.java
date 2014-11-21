package com.youwei.zjb.biz;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.youwei.zjb.house.RentType;

public enum LeaveType {
	事假,
	病假, 
	年假, 
	婚假, 
	产假, 
	调休, 
	探亲假, 
	工伤假,
	丧假;
	
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(LeaveType state : LeaveType.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("value", state.name());
			jobj.put("name", state.name());
			arr.add(jobj);
		}
		return arr;
	}
}


