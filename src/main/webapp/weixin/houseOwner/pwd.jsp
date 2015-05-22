<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>忘记密码</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />   
<link href="css/style.css" rel="stylesheet">
<!-- <script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script> -->
<script type="text/javascript" src="js/jquery.js"></script>
<script src="js/layer.m/layer.m.js"></script>
<script src="js/reg.js"></script>

<script type="text/javascript">
function resetPwd(){
	var dom_tel_v = $('#tel').val();
	var dom_pwd_v = $('#pwd').val();
	if(dom_pwd_v.length<6){
		layer.open({
            content:'请输入至少6位密码',
            btn: ['OK']
        });
	}
	$.ajax({
	    type: 'POST',
	    url: '/c/weixin/houseOwner/doLogin?tel='+dom_tel_v+'&pwd='+dom_pwd_v,
	    success: function(data){
	        var exp = new Date();
	        exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
	        document.cookie = "tel=" + dom_tel_v + ";expires=" + exp.toGMTString();
	        window.location = 'houses.jsp';
	    },
	    error:function(data){
	        var json = JSON.parse(data.responseText);
	        alert(json.msg);
	        layer.open({
	          content:json.msg,
	          btn: ['OK']
	      });
	    }
	});
}

</script>
</head>
<body>
<div class="body wx addtel">
    <div class="wrap PT30">
        <div class="item write">
            <input type="tel" class="text" name="tel" id="tel" value="" placeholder="手机号码">
        </div>
        <div class="item write">
            <input type="password" class="text" name="pwd" id="pwd" value="" placeholder="登陆密码">
        </div>
        <div class="item write">
            <input type="number" class="text w60b" name="code" id="code" value="" placeholder="验证码">
            <a href="#" class="btn blue w40b btn_act getcode " data-type="getcode">获取验证码</a>
        </div>
        <div class="item tipbox hide">
            <div class="cRed"><b>错误提示等</b></div>
        </div>
        <div class="item">
            <a href="#" class="btn gray btn_act MB10" id="submit" data-type="submit">设置</a>
        </div>
    </div>
</div>
    
</body>
</html>