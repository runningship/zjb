<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.youwei.zjb.user.entity.User" %>
<%
User user = (User)request.getSession().getAttribute("user");
  	if(user!=null){
  		request.setAttribute("me" , user);
  	}
%>
