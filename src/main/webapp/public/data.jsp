<%@page import="java.lang.reflect.Field"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.youwei.zjb.house.entity.HouseOwner"%>
<%@page import="com.youwei.zjb.KeyConstants"%>
<%@page import="org.bc.web.ThreadSession"%>
<%@page import="com.youwei.zjb.house.entity.HouseOwnerFav"%>
<%@page import="com.youwei.zjb.house.HouseOwnerService"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.youwei.zjb.sys.CityService"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.youwei.zjb.house.HouseQuery"%>
<%@page import="org.bc.sdak.Page"%>
<%@page import="com.youwei.zjb.house.LouXing"%>
<%@page import="com.youwei.zjb.house.FangXing"%>
<%@page import="java.util.List"%>
<%@page import="com.youwei.zjb.house.entity.House"%>
<%@page import="com.youwei.zjb.house.ZhuangXiu"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="util.jsp" %>
<%
	String currentPageNo = request.getParameter("currentPageNo");
	
	CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	HouseQuery query = new HouseQuery();
	HouseOwner user = (HouseOwner)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_House_Owner);
	String action = request.getParameter("action");
	if(user!=null){
		if(StringUtils.isNotEmpty(action)){
			query.action = action;
			query.userid = user.id;
		}
		if("my".equals(action)){
			query.tel = user.tel;
		}
		List<HouseOwnerFav> buyFavList = dao.listByParams(HouseOwnerFav.class, "from HouseOwnerFav where hoid=?", user.id);
		StringBuilder buyFavStr = new StringBuilder(",");
		for(HouseOwnerFav fav : buyFavList){
			buyFavStr.append(fav.hid).append(",");
		}
		request.setAttribute("buyFavStr", buyFavStr.toString());
	}
	if(!"my".equals(action)){
		query.ztai="4";
		query.sh= 1;
		query.seeGX=1;
	}
	
	String area= request.getParameter("area");
	if(StringUtils.isNotEmpty(area)){
		area = new String(area.getBytes("ISO-8859-1"),"UTF-8");
		query.area = area;
		request.setAttribute("area", area);
	}
	
	String address= request.getParameter("address");
	if(StringUtils.isNotEmpty(address)){
		address = new String(address.getBytes("ISO-8859-1"),"UTF-8");
		query.address = address;
		request.setAttribute("area", area);
	}
	
	String mjiStart = request.getParameter("mjiStart");
	String mjiEnd = request.getParameter("mjiEnd");
	String zjiaStart = request.getParameter("zjiaStart");
	String zjiaEnd = request.getParameter("zjiaEnd");
	String djiaStart = request.getParameter("djiaStart");
	String djiaEnd = request.getParameter("djiaEnd");
	String lcengStart = request.getParameter("lcengStart");
	String lcengEnd = request.getParameter("lcengEnd");
	
	String[] quyus = request.getParameterValues("quyus");
	String[] lxings = request.getParameterValues("lxings");
	String[] hxings = request.getParameterValues("hxings");
	String[] zxius = request.getParameterValues("zxius");
	HouseOwnerService hs = new HouseOwnerService();
	Page<House> p = new Page<House>();
	try{
		p.setCurrentPageNo(Integer.valueOf(currentPageNo));
	}catch(Exception ex){
		
	}
	
	buidlFloatValueQueryItem(query , "mjiStart" , request);
	buidlFloatValueQueryItem(query , "mjiEnd" , request);
	buidlFloatValueQueryItem(query , "zjiaStart" , request);
	buidlFloatValueQueryItem(query , "zjiaEnd" , request);
	buidlFloatValueQueryItem(query , "djiaStart" , request);
	buidlFloatValueQueryItem(query , "djiaEnd" , request);
	buidlFloatValueQueryItem(query , "lcengStart" , request);
	buidlFloatValueQueryItem(query , "lcengEnd" , request);
	if(quyus!=null && quyus.length>0){
		query.quyus = new ArrayList<String>();
		for(int i=0;i<quyus.length;i++){
			query.quyus.add(address = new String(quyus[i].getBytes("ISO-8859-1"),"UTF-8"));
		}
		request.setAttribute("s_quyus", Arrays.toString(query.quyus.toArray()));
	}
	if(lxings!=null && lxings.length>0){
		query.lxing = new ArrayList<String>();
		for(int i=0;i<lxings.length;i++){
			query.lxing.add(address = new String(lxings[i].getBytes("ISO-8859-1"),"UTF-8"));
		}
		request.setAttribute("s_lxings", Arrays.toString(query.lxing.toArray()));
// 		query.lxing = Arrays.asList(lxings);
// 		request.setAttribute("s_lxings", Arrays.toString(lxings));
	}
	if(hxings!=null && hxings.length>0){
		query.fxing = new ArrayList<String>();
		for(int i=0;i<hxings.length;i++){
			query.fxing.add(address = new String(hxings[i].getBytes("ISO-8859-1"),"UTF-8"));
		}
		request.setAttribute("s_hxings", Arrays.toString(query.fxing.toArray()));
		//query.fxing = Arrays.asList(hxings);
		//request.setAttribute("s_hxings", Arrays.toString(hxings));
	}
	if(zxius!=null && zxius.length>0){
		query.zxiu = new ArrayList<String>();
		for(int i=0;i<zxius.length;i++){
			query.zxiu.add(address = new String(zxius[i].getBytes("ISO-8859-1"),"UTF-8"));
		}
		request.setAttribute("s_zxius", Arrays.toString(query.zxiu.toArray()));
		
		//query.zxiu = Arrays.asList(zxius);
		//request.setAttribute("s_zxius", Arrays.toString(zxius));
	}
	String citypy=ThreadSessionHelper.getCityPinyin();
	CityService cityService = new CityService();
	JSONArray citys = cityService.getCitys();
	for(int i=0;i<citys.size();i++){
		
		JSONObject city = citys.getJSONObject(i);
		if(!"on".equals(city.getString("status"))){
			continue;
		}
		if(city.getString("py").equals(citypy)){
			request.setAttribute("quyus",city.getJSONArray("quyu"));
		}
	}
	hs.listAllHouse(query, p);
	request.setAttribute("list", p.getResult());
	request.setAttribute("totalCount", p.totalCount);
	request.setAttribute("p", p);
	request.setAttribute("zxius", ZhuangXiu.toJsonArray());
	request.setAttribute("lxings", LouXing.toJsonArray());
	request.setAttribute("hxings", FangXing.toJsonArray());
%>