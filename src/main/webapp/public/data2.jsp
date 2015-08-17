<%@page import="java.util.ArrayList"%>
<%@page import="com.youwei.zjb.house.RentType"%>
<%@page import="com.youwei.zjb.house.HouseOwnerService"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.youwei.zjb.house.entity.HouseOwner"%>
<%@page import="com.youwei.zjb.KeyConstants"%>
<%@page import="org.bc.web.ThreadSession"%>
<%@page import="com.youwei.zjb.house.entity.HouseOwnerFav"%>
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
	HouseOwner user = (HouseOwner)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_House_Owner);
	String action = request.getParameter("action");
	HouseQuery query = new HouseQuery();
	if(user!=null){
		if(StringUtils.isNotEmpty(action)){
			query.action = action;
			query.userid = user.id;
		}
		if("my".equals(action)){
			query.tel = user.tel;
		}
		List<HouseOwnerFav> rentFavList = dao.listByParams(HouseOwnerFav.class, "from HouseOwnerFav where cuzu=0 and hoid=?", user.id);
		StringBuilder rentFavStr = new StringBuilder(",");
		for(HouseOwnerFav fav : rentFavList){
			rentFavStr.append(fav.hid).append(",");
		}
		request.setAttribute("rentFavStr", rentFavStr.toString());
	}

	if(!"my".equals(action)){
		query.ztai="1";
		query.seeGX=1;
		query.sh= 1;
	}
	String area= request.getParameter("area");
	if(area!=null){
		area = new String(area.getBytes("ISO-8859-1"),"UTF-8");	
	}
	
	String address= request.getParameter("address");
	if(address!=null){
		address = new String(address.getBytes("ISO-8859-1"),"UTF-8");	
	}
	String mjiStart = request.getParameter("mjiStart");
	String mjiEnd = request.getParameter("mjiEnd");
	String zjiaStart = request.getParameter("zjiaStart");
	String zjiaEnd = request.getParameter("zjiaEnd");
	String lcengStart = request.getParameter("lcengStart");
	String lcengEnd = request.getParameter("lcengEnd");
	
	String fangshi = request.getParameter("fangshi");
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
	
	if(StringUtils.isNotEmpty(fangshi)){
		query.fangshi = Integer.valueOf(fangshi);
		request.setAttribute("s_fangshi", Integer.valueOf(fangshi));
	}
	
	String citypy=ThreadSessionHelper.getCityPinyin();
	CityService cityService = new CityService();
	JSONArray citys = cityService.getCitys();
	for(int i=0;i<citys.size();i++){
		JSONObject city = citys.getJSONObject(i);
		if(city.getString("py").equals(citypy)){
			request.setAttribute("quyus",city.getJSONArray("quyu"));
		}
	}
	hs.listAllRent(query, p);
	request.setAttribute("list", p.getResult());
	request.setAttribute("p", p);
	request.setAttribute("fangshis", RentType.toJsonArray());
	request.setAttribute("zxius", ZhuangXiu.toJsonArray());
	request.setAttribute("lxings", LouXing.toJsonArray());
	request.setAttribute("hxings", FangXing.toJsonArray());
%>