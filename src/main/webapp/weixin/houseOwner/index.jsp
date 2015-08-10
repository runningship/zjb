<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>绑定手机号</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />   
<link href="css/style.css" rel="stylesheet">
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script src="js/layer.m/layer.m.js"></script>
<script src="js/reg.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/buildHtml.js"></script>
</head>
<body>
<div class="body wx addtel">
    <div class="wrap PT30">
        <div class="item write">
            <input type="number" class="text" name="tel" id="tel" value="" placeholder="请输入手机号码">
        </div>
        <div class="item write">
            <input type="password" class="text" name="pwd" id="pwd" value="" placeholder="请设置新密码">
        </div>
        <div class="item write">
            <input type="number" class="text w60b" name="code" id="code" value="" placeholder="验证码">
            <a href="#" class="btn blue w40b btn_act getcode " data-type="getcode">获取验证码</a>
        </div>
        <div class="item write">
            <input type="text" class="text btn_act" name="city" id="city"  data-type="city" value="" readonly="readonly" placeholder="城市">
            <input type="hidden"  name="cityPy" id="cityPy"/>
        </div>
        <div class="item tipbox hide">
            <div class="cRed"><b>错误提示等</b></div>
        </div>
        <div class="item">
            <a href="#" class="btn gray btn_act MB10" id="submit" data-type="submit">立即注册</a>
            <a href="login.jsp" class="btn link">登录</a>
        </div>
        <div class="item MT30 cGray hide">
            <b>主意事项：</b>
            <p>主意事项：</p>
        </div>
    </div>
</div>
    
</body>
</html>