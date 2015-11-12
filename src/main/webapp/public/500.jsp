<%@page import="com.youwei.zjb.KeyConstants"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.youwei.zjb.sys.entity.City"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.youwei.zjb.sys.CityService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  isErrorPage="true" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	//exception.printStackTrace(response.getWriter());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="css/reset.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/layer/layer.js"></script>
<script type="text/javascript" src="js/jQuery.resizeEnd.min.js"></script>
<script type="text/javascript" src="js/javascript.js"></script>
<script type="text/javascript" src="js/buildHtml.js"></script>
<script type="text/javascript" src="js/list.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="bodyer">
    <div class="header">
        <jsp:include page="top.jsp?type=chushou"></jsp:include>
        <div class="search">
        </div>
        <div class="searchLine">
            <div class="wrap">
            	数据有点问题，如果您是切换城市引起的，可能这个城市的数据还没哟开通，换个城市试试吧
            </div>
        </div>
    </div>
    <div class="mainer">
        <div class="wrap">

        </div>
    </div>
    
    <div class="footer">
    </div>
</div>
<div class="">
    <div class="citybox hidden">
      <ul>
        <c:forEach items="${citys}" var="city">
        	<c:if test="${city.status eq 'on' }">
       			<li><a py="${city.py }" href="javascript:void(0)" onclick="switchCity('${city.py}' , '${city.name }' );">${city.name }</a></li>
       		</c:if>
       	</c:forEach>
      </ul>
    </div>
</div>

<div style="display:none">
	错误堆栈信息：<br/>
	<c:forEach var="trace" items="${pageContext.exception.stackTrace}">
	<p>${trace}</p>
	</c:forEach>
</div>
</body>
</html>