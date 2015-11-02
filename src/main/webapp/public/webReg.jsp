<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String cityPy = request.getParameter("cityPy");
request.setAttribute("cityPy", cityPy);
String invitationCode = request.getParameter("code");
request.setAttribute("invitationCode", invitationCode);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0"/>
<title></title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="" rel="stylesheet">
<link href="css/reset.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/layer/layer.js"></script>
<script type="text/javascript" src="../js/buildHtml.js"></script>
<script type="text/javascript">
var server_host="192.168.1.222:8081";
var sendingVerifyCode=false;
$(document).ready(function() {
    $(document).find('.form-active').find('.input').focusin(function(){
        $(this).parent().addClass('active').addClass('focus');
    }).focusout(function(){
        if($(this).is('select')){
            if($(this).is(":selected")){
                $(this).parent().removeClass('active');
            }
        }else{
            if(!$(this).val()){
                $(this).parent().removeClass('active');
            }
        }
        $(this).parent().removeClass('focus').removeClass('curr');
    }).hover(function() {
        $(this).parent().addClass('hover');
    }, function() {
        $(this).parent().removeClass('hover');
    }).each(function(index, el) {
        if($(this).is('select')){
            $(this).parent().addClass('curr');
        }
    });
});

$(function(){
	if(!$('#invitationCode').val()){
		layer.open({
	           content:'参数不正确',
	           btn: ['OK']
	       });
	}
	if(!$('#cityPy').val()){
		layer.open({
	           content:'参数不正确',
	           btn: ['OK']
	       });
	}
});
function getVerfiyCode(btn){
	if(sendingVerifyCode){
		return;
	}
	//$(btn).css('background','#ddd');
	var tel = $('#tel').val();
	var pwd = $('#pwd').val();
	var set = $('.blue');
	if(!set){
		return;
	}
	if(!tel){
		layer.open({
	           content:'请先填写有效手机号码',
	           btn: ['OK']
	       });
		return;
	}
	//提示信息
   	 YW.ajax({
	   type: 'POST',
	   url:'http://'+server_host+'/c/mobile/user/sendVerifyCode?cityPy=${cityPy}&tel='+tel,
	   mysuccess: function(data){
		   var ret = JSON.parse(data);
		   if(ret && ret.result){
			   sendingVerifyCode = true;
				setcode();
			}else{
				layer.open({
			           content: '获取验证码失败',
			           btn: ['OK']
			       });
			}
       }
	  });
}

function setcode(){
	var times=60;
	var clock =  setInterval(function(){
		times--;
		if(times<1){
			$('.getcode').html('获取验证码');
			$('.getcode').attr('class','btn blue w40b btn_act getcode');
			clearInterval(clock);
			sendingVerifyCode = false;
			//$('.getcode').css('background','white');
			return;
		}
		$('.getcode').html('已发送('+times+')');
		$('.getcode').attr('class','btn gray w40b btn_act getcode');
	},1000);
}

function doReg(){
	var tel = $('#tel').val();
	var pwd = $('#pwd').val();
	var uname = $('#uname').val();
	var code = $('#verifyCode').val();
	if(!tel){
		layer.open({
	           content:'请输入正确的手机号码',
	           btn: ['OK']
	       });
		return;
	}
	if(!code){
		layer.open({
           content:'请输入验证码',
           btn: ['OK']
       });
		return;
	}
	if(!code){
		layer.open({
           content:'请输入您的姓名',
           btn: ['OK']
       });
		return;
	}
	if(!pwd){
		layer.open({
	           content:'请设置登录密码',
	           btn: ['OK']
	       });
		return;
	}
	//提示信息
	 var a=$('form[name=form1]').serialize();
	   YW.ajax({
	   type: 'POST',
	   url:'http://'+server_host+'/c/mobile/user/verifyCode',
	   data:a,
	   mysuccess: function(data){
		   var ret = JSON.parse(data);
		   if(ret && ret.result=='1'){
			   layer.open({
		           content:'注册成功,马上下载中介宝激活礼包!',
		           btn: ['OK'],
		           yes: function(layero,index){
			       		window.location='http://a.app.qq.com/o/simple.jsp?pkgname=com.wudoumi.zhongjiebao';
		           }
		       });
			}else{
				layer.open({
			           content: ret.msg,
			           btn: ['OK']
			       });
			}
       }
	   });
}
</script>
</head>
<body>
<div class="webreg bodyer">
<div class="box">
<form class="form1" name="form1" onsubmit="return false;">
	<input type="hidden"  id="invitationCode" value="${invitationCode }" name="invitationCode" />
	<input type="hidden"  id="cityPy" value="${cityPy }" name="cityPy" />
<ul class="form-ul ">
    <li class=""><label class="form-section form-active"><strong class="input-label">您的手机号码</strong><input id="tel" type="tel" class="input placeholder" name="tel" placeholder="您的手机号码"><span class="tip">必填</span></label></li>
    <li class=""><label class="form-section form-active"><strong class="input-label">姓名</strong><input id="uname" type="text" class="input placeholder" name="uname" placeholder="您的姓名"><span class="tip">必填</span></label></li>
    <li class=""><label class="form-section form-active"><strong class="input-label">您的密码</strong><input id="pwd" type="password" class="input placeholder" name="pwd" placeholder="您的密码"><span class="tip"></span><span class="tip">必填</span></label></li>
    <li class=""><label class="form-section tow form-active"><strong class="input-label">验证码</strong><a onclick="getVerfiyCode(this)" href="javascript:void(0)" class="btn blue btn_act code getcode" data-type="getCode" data-txt="发送验证码">发送验证码</a><input id="verifyCode" type="tel" class="input placeholder" name="code" placeholder="验证码"></label></li>
    <li class="">
        <a href="javascript:void(0)" class="btn btn_act block round5 orange btnForm1 hvr-hong" onclick="doReg();">立即领取</a>
    </li>
</ul>
</form>
</div>
</div>
</body>
</html>