<%@page import="java.lang.reflect.Field"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.youwei.zjb.house.HouseQuery"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.youwei.zjb.sys.CityService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%!
    public void buidlFloatValueQueryItem(HouseQuery q , String pname , HttpServletRequest req){
		String pvalue = req.getParameter(pname);
		try{
			if(StringUtils.isNotEmpty(pvalue)){
				//q.mjiStart = Float.valueOf(pvalue);
				Float floatValue = Float.valueOf(pvalue);
				Field field = q.getClass().getDeclaredField(pname);
				 if("int".equals(field.getType().getName()) || "java.lang.Integer".equals(field.getType().getName())){
					 field.set(q, floatValue.intValue());
				 }else{
					 field.set(q, floatValue);	 
				 }
				
				if(floatValue-floatValue.intValue()==0){
					req.setAttribute(pname, floatValue.intValue());	
				}else{
					req.setAttribute(pname,floatValue);
				}
			}
		}catch(Exception ex){
			//ignore
		}
    }
%>