package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum LouXing {

	多层,
	高层,
	小高层,
	多层复式,
	高层复式,
	多层跃式,
	高层跃式,
	独立别墅,
	连体别墅,
	双拼别墅,
	沿街门面,
	室内商铺,
	商住楼,
	写字楼,
	小产权房,
	自建房,
	车位,
	其他;
	
	//多层,高层,小高层,多层复式,高层复式,多层跃式,高层跃式,独立别墅,连体别墅,双拼别墅,沿街门面,室内商铺,商住楼,写字楼,小产权房,自建房,车位,其他
	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(LouXing lx : LouXing.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", lx.name());
			jobj.put("value", lx.name());
			arr.add(jobj);
		}
		return arr;
	}
}
