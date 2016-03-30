<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="com.youwei.zjb.cache.ConfigCache"%>
<%@page import="com.youwei.zjb.user.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
User user = (User)request.getSession().getAttribute("user");
request.setAttribute("user", user);
request.setAttribute("comp", user.Company().namea);
String new_house_server = ConfigCache.get("new_house_server", "www.zhongjiebao.com");
String new_house_server_port = ConfigCache.get("new_house_server_port", "8080");
request.setAttribute("new_house_server", new_house_server);
request.setAttribute("new_house_server_port", new_house_server_port);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title></title>
<link rel="stylesheet" type="text/css" href="../style/css.css" />
<link rel="stylesheet" type="text/css" href="../style/style.css" />
<link rel="stylesheet" type="text/css" href="../style/css_ky.css" />
<link href="../style/animate.min.css" rel="stylesheet" />
<script type="text/javascript" src="../js/jquery.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="../js/jquery.j.tool.v2.js" type="text/javascript"></script>
<style type="text/css">
#menuTop{position:fixed;width:100%;display: inline-block;margin-top: -50px;height: 50px;}
.mainer { overflow: hidden; }
.mainer iframe{width:100%;height:100%;}
.newhouse{background:none;}
</style>
</head>
<body class="newhouse">
<div class="mainbox">
  <div class="bodyer">
  <jsp:include page="menuTop.jsp?type=bank" />
    <div class="mainer">
    	<iframe src="http://${new_house_server }:${new_house_server_port }/new-house/public/bank/list.jsp?sellerTel=${user.tel }&sellerName=${user.uname}&comp=${comp}"></iframe>
    </div>
  </div>

  <script type="text/javascript">
 $(document).ready(function() {
	initTopMenu();
 });

function resizebox(){
  win=$(window),
  winH=win.innerHeight(),
  main=$('.mainer'),
  menu=$('#menuTop'),
  menuH=menu.innerHeight();
  menu.css({'margin-top':-menuH})
  //menu.find('ul').css({'display':'inline-block','width':'auto'})
  main.height(winH-menuH);
}
$(document).ready(function(){
  resizebox();
});
$(window).resize(function(){
  resizebox();
});

</script>
</div>
</body>
</html>