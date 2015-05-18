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
<script type="text/javascript">
function getCode(){
    var tel = $('#tel').val();
    if (tel=='') {
      alert('请输入手机号');
    };
  YW.ajax({
      type: 'POST',
      url: '/c/houseOwner/sendVerifyCode?tel='+tel,
      mysuccess: function(data){
        $('#code').focus();
        alert('验证码已发送，请于5分钟内填写');
      }
  });
}

function setTel(){}

</script>
</head>
<body>
<div class="body wx addtel">
    <div class="wrap PT30">
        <div class="item write">
            <input type="text" class="text" name="tel" id="tel" value="" placeholder="请绑定手机号码">
        </div>
        <div class="item write">
            <input type="password" class="text" name="pwd" id="pwd" value="" placeholder="登陆密码">
        </div>
        <div class="item write">
            <input type="text" class="text w60b" name="code" id="code" value="" placeholder="验证码">
            <a href="#" class="btn blue w40b btn_act getcode " data-type="getcode" onclick="getCode();return false;">获取验证码</a>
        </div>
        <div class="item tipbox hide">
            <div class="cRed"><b>错误提示等</b></div>
        </div>
        <div class="item">
            <a href="#" class="btn gray btn_act" id="submit" data-type="submit" onclick="setTel();return false;">绑定手机号</a>
        </div>
        <div class="item MT30 cGray hide">
            <b>主意事项：</b>
            <p>主意事项：</p>
        </div>
    </div>
</div>
    
</body>
</html>