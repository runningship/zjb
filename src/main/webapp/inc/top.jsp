<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.youwei.zjb.user.entity.User" %>
<%@ page import="com.youwei.zjb.user.UserHelper" %>
<%
	User user = (User)request.getSession().getAttribute("user");
  	if(user!=null){
  		request.setAttribute("me" , user);
  		List<String> list = (List<String>)session.getAttribute("authNames");
  		request.setAttribute("authNames",session.getAttribute("authNames"));
  		//request.setAttribute("authNames",UserHelper.getAuthorityNames(user));
  	}
  	
  	String agent = request.getHeader("User-Agent");
  	request.setAttribute("useLocalResource", 0);
%>
