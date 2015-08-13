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
<%
	String currentPageNo = request.getParameter("currentPageNo");
	
	CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	HouseOwner user = (HouseOwner)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_House_Owner);
	if(user!=null){
		List<HouseOwnerFav> buyFavList = dao.listByParams(HouseOwnerFav.class, "from HouseOwnerFav where hoid=?", user.id);
		StringBuilder buyFavStr = new StringBuilder();
		for(HouseOwnerFav fav : buyFavList){
			buyFavStr.append(fav.hid).append(",");
		}
		request.setAttribute("buyFavStr", buyFavStr);
	}
	
	String area = request.getParameter("area");
	String address = request.getParameter("address");
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
	HouseQuery query = new HouseQuery();
	if(StringUtils.isNotEmpty(area)){
		query.area = area;
		request.setAttribute("area", area);
	}
	if(StringUtils.isNotEmpty(address)){
		query.address = address;
		request.setAttribute("address", address);
	}
	if(StringUtils.isNotEmpty(mjiStart)){
		query.mjiStart = Float.valueOf(mjiStart);
		request.setAttribute("mjiStart", Integer.valueOf(mjiStart));
	}
	if(StringUtils.isNotEmpty(mjiEnd)){
		query.mjiEnd = Float.valueOf(mjiEnd);
		request.setAttribute("mjiEnd", Float.valueOf(mjiEnd));
	}
	if(StringUtils.isNotEmpty(zjiaStart)){
		query.zjiaStart = Float.valueOf(zjiaStart);
		request.setAttribute("zjiaStart", Float.valueOf(zjiaStart));
	}
	if(StringUtils.isNotEmpty(zjiaEnd)){
		query.zjiaEnd = Float.valueOf(zjiaEnd);
		request.setAttribute("zjiaEnd", Float.valueOf(zjiaStart));
	}
	if(StringUtils.isNotEmpty(djiaStart)){
		query.djiaStart = Float.valueOf(djiaStart);
		request.setAttribute("djiaStart", Float.valueOf(djiaStart));
	}
	if(StringUtils.isNotEmpty(djiaEnd)){
		query.djiaEnd = Float.valueOf(djiaEnd);
		request.setAttribute("djiaEnd", Float.valueOf(djiaEnd));
	}
	if(StringUtils.isNotEmpty(lcengStart)){
		query.lcengStart = Integer.valueOf(lcengStart);
		request.setAttribute("lcengStart", Integer.valueOf(lcengStart));
	}
	if(StringUtils.isNotEmpty(lcengEnd)){
		query.lcengEnd = Integer.valueOf(lcengEnd);
		request.setAttribute("lcengEnd", Integer.valueOf(lcengEnd));
	} 
	if(quyus!=null && quyus.length>0){
		query.quyus = Arrays.asList(quyus);
		request.setAttribute("s_quyus", Arrays.toString(quyus));
	}
	if(lxings!=null && lxings.length>0){
		query.lxing = Arrays.asList(lxings);
		request.setAttribute("s_lxings", Arrays.toString(lxings));
	}
	if(hxings!=null && hxings.length>0){
		query.fxing = Arrays.asList(hxings);
		request.setAttribute("s_hxings", Arrays.toString(hxings));
	}
	if(zxius!=null && zxius.length>0){
		query.zxiu = Arrays.asList(zxius);
		request.setAttribute("s_zxius", Arrays.toString(zxius));
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
	hs.listAllHouse(query, p);
	request.setAttribute("list", p.getResult());
	request.setAttribute("p", p);
	request.setAttribute("zxius", ZhuangXiu.toJsonArray());
	request.setAttribute("lxings", LouXing.toJsonArray());
	request.setAttribute("hxings", FangXing.toJsonArray());
%>