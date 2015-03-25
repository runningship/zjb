package com.youwei.zjb.task;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.util.DataHelper;

public class TaskHelper {

	public static Integer getHxtFromText(String text){
		int ting = text.indexOf("厅");
		try{
			if(ting>0){
				return Integer.valueOf(String.valueOf(text.charAt(ting-1)));
			}
		}catch(Exception ex){
			//暂不处理
		}
		
		return 0;
	}
	
	public static Integer getHxsFromText(String text){
		int ting = text.indexOf("室");
		try{
			if(ting>0){
				return Integer.valueOf(String.valueOf(text.charAt(ting-1)));
			}
		}catch(Exception ex){
			//暂不处理
		}
		
		return 0;
	}
	
	public static Integer getHxwFromText(String text){
		int ting = text.indexOf("卫");
		try{
			if(ting>0){
				return Integer.valueOf(String.valueOf(text.charAt(ting-1)));
			}
		}catch(Exception ex){
			//暂不处理
		}
		
		return 0;
	}
	
	public static int getLcengFromText(String text){
		text = text.replace("楼", "");
		if(StringUtils.isEmpty(text)){
			return 0;
		}
		try{
			String tmp = text.split("/")[0];
			return Integer.valueOf(tmp);
		}catch(Exception ex){
			//暂不处理
			return 0;
		}
	}
	public static int getZcengFromText(String text){
		text = text.replace("楼", "");
		try{
			String tmp = text.split("/")[1];
			return Integer.valueOf(tmp);
		}catch(Exception ex){
			//暂不处理
			return 0;
		}
	}
	
	public static int getYearFromText(String text){
		String tmp = "";
		if(text.contains("年")){
			tmp =  text.replace("年", "").trim();
		}else{
			tmp = text;
		}
		if("暂无".equals(tmp)){
			return 0;
		}
		try{
			return Integer.valueOf(tmp);
		}catch(Exception ex){
			return 0;
		}
	}
	
	public static Float getMjiFromText(String text){
		if(StringUtils.isEmpty(text)){
			return 0f;
		}
		String tmp = "";
		text  = text.replace("　", " ");
		text = text.split("（")[0];
		for(String str : text.split(" ")){
			if(str.contains("㎡") && !str.contains("（")){
				tmp =  str.replace("㎡", "").trim();
				break;
			}
		}
		try{
			return Float.valueOf(tmp);
		}catch(Exception ex){
			return 0f;
		}
	}
	
	public static String getTelFromText(String text){
		if(StringUtils.isEmpty(text)){
			return "";
		}
		String[] arr = text.split("src=");
		if(arr.length>1){
			text = arr[1];
			arr = text.split("'");
			if(arr.length>2){
				text = arr[1];
				return text;
			}
		}
		return "";
	}
	
	public static String getAreaFromText(String text){
		if(StringUtils.isEmpty(text)){
			return "";
		}
		text = text.replace(String.valueOf((char)160), "");
		return text.replace("-", "").trim();
	}
	
	public static Date getPubtimeFromText(String text){
		text = text.trim();
		try{
			return DataHelper.dateSdf.parse(text);
		}catch(Exception ex){
			return new Date();
		}
		
	}
}
