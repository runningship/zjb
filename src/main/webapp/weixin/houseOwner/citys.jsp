<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title >选择城市</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />

<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link href="css/iconfont/iconfont.css" rel="stylesheet">
<link href="css/style.css?11" rel="stylesheet">
<script src="js/jquery.js"></script>
<script type="text/javascript" src="js/buildHtml.js"></script>
<script>
function setCity(cityPy){
     YW.ajax({
        type: 'POST',
        url: '/c/weixin/houseOwner/setCity?city='+cityPy,
        mysuccess: function(data){
        	//document.cookie = "house_owner_city=" + cityPy + ";expires=" + exp.toGMTString();
       		if(document.cookie.indexOf('tel')>-1){
           		window.location="houses.jsp";
           	}else{
           		window.location="login.jsp";
           	}	
        }
     });
}
$(function(){
});
</script>
</head>
<body class=" citys">
<div class="body wx">
    <div class="wrap">
        <ul class="item ul_list">
        <c:forEach items="${citys}" var="city">
        	<c:if test="${city.status eq 'on' }">
        		<li onclick="setCity('${city.py}')">
	                <div class="ibox">
	                    <span class="hname">${city.name }</span>
	                </div>
	            </li>
        	</c:if>
        	
        </c:forEach>
        </ul>
    </div>
</div>
    
</body>
</html>