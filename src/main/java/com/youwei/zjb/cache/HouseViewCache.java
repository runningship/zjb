package com.youwei.zjb.cache;

import java.util.HashMap;
import java.util.Map;

import com.youwei.zjb.ThreadSessionHelper;

public class HouseViewCache {

	private static HouseViewCache cache = new HouseViewCache();
	
	private Map<String , HouseCacheVO> data = new HashMap<String , HouseCacheVO>();
	
	private HouseViewCache(){
		
	}
	
	public static HouseViewCache getInstance(){
		return cache;
	}
	
	public HouseCacheVO loadHouse(Integer hid){
		String key = ThreadSessionHelper.getCityPinyin()+hid;
		if(data.containsKey(key)){
			return data.get(key);
		}
		return null;
	}
	
	public void putHouse(HouseCacheVO house){
		String key = ThreadSessionHelper.getCityPinyin()+house.hid;
		data.put(key, house);
	}
	
	public void remove(Integer hid){
		String key = ThreadSessionHelper.getCityPinyin()+hid;
		data.remove(key);
	}
	
	public void clear(){
		data.clear();
	}
	
	public int size(){
		return data.size();
	}
}
