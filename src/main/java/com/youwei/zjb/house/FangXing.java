package com.youwei.zjb.house;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public enum FangXing {

	房1厅0卫0("1房0厅0卫",1,0,0),
	房1厅1卫0("1房1厅0卫",1,1,0),
	房1厅1卫1("1房1厅1卫",1,1,1),
	房2厅1卫1("2房2厅1卫",2,1,1),
	房2厅2卫1("2房2厅1卫",2,2,1),
	房3厅1卫1("3房1厅1卫",3,1,1),
	房3厅2卫1("3房2厅1卫",3,2,1),
	房3厅2卫2("3房2厅2卫",3,2,2),
	房4厅2卫1("4房2厅1卫",4,2,1),
	房4厅2卫2("4房2厅2卫",4,2,2),
	房5厅2卫2("5房2厅2卫",5,2,2),
	房5厅3卫2("5房3厅2卫",5,3,2);
	
	private String name;
	private int hxf;
	private int hxt;
	private int hxw;
	
	private FangXing(String name, int f , int t , int w){
		this.name = name;
		this.hxf = f;
		this.hxt = t;
		this.hxw = w;
	}

	public int getHxf() {
		return hxf;
	}

	public int getHxt() {
		return hxt;
	}

	public int getHxw() {
		return hxw;
	}

	public static JSONArray toJsonArray(){
		JSONArray arr = new JSONArray();
		for(FangXing fx : FangXing.values()){
			JSONObject jobj = new JSONObject();
			jobj.put("name", fx.name);
			jobj.put("value", fx.name());
			arr.add(jobj);
		}
		return arr;
	}
}
