<%@page import="com.youwei.zjb.house.HouseQuery"%>
<%@page import="org.bc.sdak.Page"%>
<%@page import="com.youwei.zjb.house.HouseService"%>
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
	CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	HouseService hs = new HouseService();
	Page<House> p = new Page<House>();
	HouseQuery query = new HouseQuery();
	hs.listAll(query, p);
	request.setAttribute("list", p.getResult());
	request.setAttribute("zxius", ZhuangXiu.toJsonArray());
	request.setAttribute("lxings", LouXing.toJsonArray());
	request.setAttribute("hxings", FangXing.toJsonArray());
%>