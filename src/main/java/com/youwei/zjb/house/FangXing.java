package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bc.sdak.utils.LogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

//户型
public enum FangXing {

	房1厅0卫0("1房0厅0卫",1,0,0),
	房1厅0卫1("1房0厅1卫",1,0,1),
	房1厅1卫0("1房1厅0卫",1,1,0),
	房1厅1卫1("1房1厅1卫",1,1,1),
	房1厅2卫1("1房2厅1卫",1,2,1),
	房2厅1卫1("2房1厅1卫",2,1,1),
	房2厅2卫1("2房2厅1卫",2,2,1),
	房2厅2卫2("2房2厅2卫",2,2,2),
	房3厅1卫1("3房1厅1卫",3,1,1),
	房3厅1卫2("3房1厅2卫",3,1,2),
	房3厅2卫1("3房2厅1卫",3,2,1),
	房3厅2卫2("3房2厅2卫",3,2,2),
	房4厅1卫1("4房1厅1卫",4,1,1),
	房4厅1卫2("4房1厅2卫",4,1,2),
	房4厅2卫1("4房2厅1卫",4,2,1),
	房4厅2卫2("4房2厅2卫",4,2,2),
	房4厅2卫3("4房2厅3卫",4,2,3),
	房4厅3卫2("4房3厅2卫",4,3,2),
	房5厅1卫1("5房1厅1卫",5,1,1),
	房5厅1卫2("5房1厅2卫",5,1,2),
	房5厅2卫2("5房2厅2卫",5,2,2),
	房5厅3卫2("5房3厅2卫",5,3,2),
	房6厅2卫2("6房2厅2卫",6,2,2);
	
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
	
	public static FangXing parse(String name){
		for(FangXing fx : FangXing.values()){
			if(fx.name.equals(name)){
				return fx;
			}
		}
		return null;
	}
	
	public static FangXing parse(Integer  f , Integer t , Integer w){
		if(f==null){
			f=0;
		}
		if(t==null){
			t=0;
		}
		if(w==null){
			w=0;
		}
		for(FangXing fx : FangXing.values()){
			if(fx.hxf==f && fx.hxt==t && fx.hxw==w){
				return fx;
			}
		}
		return null;
	}

	public String getName() {
		return name;
	}
	
	public static List<Map<String,Object>> toList(){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(FangXing state : FangXing.values()){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("name", state.name);
			list.add(map);
		}
		return list;
	}
}
