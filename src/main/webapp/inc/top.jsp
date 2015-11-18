<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.youwei.zjb.user.entity.User" %>
<%@ page import="com.youwei.zjb.user.UserHelper" %>
<%
User user = (User)request.getSession().getAttribute("user");
  	if(user!=null){
  		request.setAttribute("me" , user);
  		request.setAttribute("authNames",UserHelper.getAuthorityNames(user));
  	}
  	
  	String agent = request.getHeader("User-Agent");
  	if(agent.contains("Chrome/35.0.1916.157") || agent.contains("Chrome/30.0.1599.66")){
  		request.setAttribute("nwjs", true);
  		if("0000003".equals(user.lname) || "0000013".equals(user.lname) || "0000015".equals(user.lname) || "0000016".equals(user.lname)){
  			request.setAttribute("useLocalResource", 1);	
  		}
  	}else{
  		request.setAttribute("useLocalResource", 0);
  	}
%>
