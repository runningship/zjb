package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.List;

import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.npl.entity.Aggregation;

public class BasicConcept {

	private static List<String> list = new ArrayList<String>();
	
	static {
		list.add("视觉");
		list.add("客观");
		list.add("单位");
		list.add("数量");
		list.add("指代");
		list.add("嗅觉");
		list.add("味觉");
		list.add("触觉");
	}
	
	public static boolean isBasic(String str){
		return list.contains(str);
	}
	
	public static String findRootAggregation(String str){
		Aggregation aggr = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(Aggregation.class, "elem", str);
		if(aggr==null){
			return null;
		}
		if(isBasic(aggr.sets)){
			return aggr.sets;
		}else{
			return findRootAggregation(aggr.sets);
		}
	}
	
	public static String findSet(String str){
		Aggregation aggr = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(Aggregation.class, "sets", str);
		if(aggr==null){
			return null;
		}
		return aggr.sets;
	}
}
