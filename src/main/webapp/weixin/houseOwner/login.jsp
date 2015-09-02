<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>登录</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />   
<link href="css/style.css" rel="stylesheet">
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script src="js/layer.m/layer.m.js"></script>
<script src="js/login.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/buildHtml.js"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="body wx addtel">
    <div class="wrap PT30">
        <div class="item write">
            <input type="number" class="text" name="tel" id="tel" value="" placeholder="手机号码">
        </div>
        <div class="item write">
            <input type="password" class="text" name="pwd" id="pwd" value="" placeholder="密码">
        </div>
        <div class="item write">
            <input type="text" class="text btn_act" name="city" id="city"  data-type="city" value="" readonly="readonly" placeholder="城市">
            <input type="hidden"  name="cityPy" id="cityPy"/>
        </div>
        <div class="item tipbox hide">
            <div class="cRed"><b>错误提示等</b></div>
        </div>
        <div class="item">
            <a href="#" class="btn gray btn_act MB10" id="submit" data-type="submit">登录</a>
        </div>
        <div class="item">
            <a href="index.jsp" class="btn link w50b">注册</a>
            <a href="pwd.jsp" class="btn link w50b">忘记密码</a>
        </div>
        <div style="padding:10px;">
            <h2 style="color:gray;">温馨提示：</h2>
            <p style="padding:3px;color:#CDC0B0;">1、注册登录后，可发布出售房源。该信息展示在各城市数百家房产中介公司使用的中介宝端口；</p>
            <p style="padding:3px;color:#CDC0B0;">2、登录后，自动同步您现有的房源信息。您可修改、删除信息，中介端口自动同步。避免持续电话骚扰！</p>
        </div>
        <a href="#"  style="width:80px;" onclick='document.cookie="city="' class="btn link w50b">清空缓存</a>
    </div>
</div>
    
</body>
</html>