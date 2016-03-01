<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("user", ThreadSessionHelper.getUser());
	request.setAttribute("cityPy", ThreadSessionHelper.getCityPinyin());
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝房源软件系统</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<link href="/style/css.css" rel="stylesheet">
<link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>

<link  href="/js/YMX_input/css/style.css" rel="stylesheet">
<script src="/js/YMX_input/js/YMX_input.js" type="text/javascript"></script>

<script type="text/javascript">
// 只让某个DIV显示，同级其他隐藏
function divShow(T){
$('.'+T).addClass('db').removeClass('dn').siblings().removeClass('db').addClass('dn');
}
function submits(){
    var uname = $('.uname'),
    unameV = uname.val(),
    unameL = unameV.length;
    if(!unameV){
        alert('请输入您的姓名');
        uname.focus();
        return;
    }else if(unameL<=1&&unameL>4){
        alert('请输入您的真实姓名');
        uname.focus();
        return;
    }
	var tel = $('#tel'),
    telV = tel.val();
	if(!validateMobile(telV)){
		alert('请输入有效的手机号码');
        tel.focus();
		return;
	}
    var a=$('form[name=form1]').serialize();
    YW.ajax({
        type: 'POST',
        url: '/c/user/selfUpdate',
        data:a,
        mysuccess: function(data){
            divShow('yes');
            //art.dialog.close();
            //alert('修改成功');
        }
    });
}

var parent = art.dialog.parent,       // 父页面window对象
api = art.dialog.open.api,  //      art.dialog.open扩展方法
form1=$('.form1');
$(document).ready(function() {
    if (!api) return;
    // 操作对话框
    api.title('修改个人信息')
    // 自定义按钮
      .button({
          name: '确定',
          callback: function () {
            //form1.submit();
            $('.db').find('.submit').click();
            return false;
          },focus: true
      },{
          name: '取消'
      })

});
var sendingVerifyCode = false;
function getCode(){
	event.preventDefault();
	event.cancelBubbled=true;
	var tel = $('#tel'),
    telV = tel.val();
	if(!validateMobile(telV)){
		alert('请输入有效的手机号码');
        tel.focus();
		return;
	}
    if(tel.parents('label').hasClass('form-err')){
        alert('已被店内其他帐号绑定！');
        tel.focus();
        return;
    }
    telV = telV.trim();
	sendingVerifyCode = true;
	$('.getCode').addClass('gray');
	YW.ajax({
	    type: 'POST',
	    url: '/c/mobile/user/sendVerifyCode',
	    data:{tel:telV , cityPy:'${cityPy}'},
	    mysuccess: function(data){
	    	alert('验证码已发送');
            $('.verifyCode').focus();
	    	setGetCodeTimer();
	    }
    });
	
}

function validateMobile(tel){
    var isMob = new RegExp(/^(1[34578][0-9]{9})$/);
    if(!tel){
        return false;
    }
    if(isMob.test(tel)){
        return true;
    }else{
        return false;
    }
}

function setGetCodeTimer(){
	var times=60;
	var clock =  setInterval(function(){
		times--;
		if(times<1){
			$('.getCode').html('获取验证码');
			$('.getCode').removeClass('gray');
			clearInterval(clock);
			sendingVerifyCode = false;
			return;
		}
		$('.getCode').html('已发送('+times+')');
	},1000);
}
function addU(){
    parent.openReg();
}
$(document).ready(function() {
    $('.uname').focus();
});

function isPhoneUse(){
    var tel = $('#tel'),
    telV = tel.val(),
    telP = tel.parents('label');
    YW.ajax({
        type: 'POST',
        url: '/c/user/hasPcBinding',
        data:{phoneNum:telV},
        mysuccess: function(result){
            var data = JSON.parse(result);
            console.log('data:'+data)
            if(data.result==0){
                telP.removeClass('form-err').find('.tip').text('可用');
            }else{
                telP.addClass('form-err').find('.tip').text('已被绑定:'+data.lname);

            }
        }
    });
}
$(document).on('keyup focus change', '#tel', function(event) {
    var Thi=$(this),
    ThiV   = Thi.val(),
    ThiLen = ThiV.length,
    ThiP   = Thi.parents('label');
//    console.log(ThiLen)
    if(ThiLen==11){
        ThiP.removeClass('form-err').find('.tip').text('');
    }else if(!validateMobile(ThiV)){
        ThiP.addClass('form-err').find('.tip').text('请输入正确的手机号');
    }else{
        ThiP.removeClass('form-err').find('.tip').text('');
    }
    //event.preventDefault();
    /* Act on the event */
});
</script>
<style type="text/css">
input{outline:none} 
.getCode{float: right; padding: 6px 6px; color: blue;}
.verifyCode{width:60%;display:inline;}
.gray{color:gray;}

body{ width: 500px; height: auto; }
.form1{ padding: 20px 60px 20px 0;}
.form-ul{  }

.alert{ padding: 20px 10px 20px 10px;  color: #737383; text-align: center; }
.alert i{ font-size: 36px;}
.alert h1{ font-size: 50px;  }
.alert div{ font-weight: normal; font-size: 20px;}
.alert div i{ font-size: 26px;}
.alert .conts{}

.dn{ display: none; }
.ewmbox{ float: left; padding-bottom: 50px; }

.yes { padding-top: 0; padding-bottom: 0; }
.yes .ewmbox{ float: none; padding:0; padding-top: 20px; }

.form-err{ color: #F00; }
.form-err .input-label{ color: #F00;}
.form-err input.input{ color: #F00; border-color: #F00; background: #FEFFC4; }
.form-err span.tip{ color: #F00; }
</style>
</head>
<body>
<div class="bodybox">
    <form name="form1" class=" form-box form1 db" role="form" onsubmit="submits();return false;">
        <ul class="form-ul">
            <li class=""><label class="form-section  form-active"><strong class="input-label">账号：</strong><input type="text" class="input placeholders " name="lname" value="${user.lname }"  disabled="disabled" placeholder="用户名/手机/input"><span class="tip"></span></label></li>
            <li class="xm"><label class="form-section form-active"><strong class="input-label">姓名：</strong><input type="text" class="input placeholders uname" name="uname" value="${user.uname }" placeholder="您的姓名"><span class="tip"></span></label></li>
            <li class="zjh"><label class="form-section form-active"><strong class="input-label">手机号：</strong><input type="text" class="input placeholders" id="tel" name="tel" maxlength="11" value="${user.tel }" placeholder="您的手机号"autocomplete="off"><span class="tip">当前账号绑定</span></label></li>
            <li class="mm"><label class="form-section form-active"><strong class="input-label">密码：</strong><input type="password" class="input placeholders" name="pwd" placeholder="不修改请留空"></label></li>
            <li class="yzm"><label class="form-section tow form-active"><strong class="input-label">验证码：</strong><a href="#" class="btn btn_act code getCode" data-type="regCode" data-txt="发送验证码" class="" onclick="getCode();return false;">发送验证码</a><input type="text" class="input placeholders c verifyCode" name="verifyCode" value="" placeholder="收到的验证码"></label></li>
        </ul>
        <input type="hidden" name="id"  value="${user.id}" >
        <input type="submit" class="submit" style="display: none;">
    </form>
    <div class="alert yes dn">
        <h2><i class="iconfont">&#xe67b;</i> 成功修改个人信息！</h2>
        <p class="conts">（绑定手机可实现电脑版和手机版“收藏、新房推荐记录”等数据的同步！）</p>
        <p class="ewmbox"><img src="../style/images/ajb_all_600.png" width="120" alt="" class="ewm"><br>        下载中介宝APP</p>
        <a onclick="api.close()" class="submit"></a>
    </div>
    <div class="alert no1 dn">
        <p class="ewmbox"><img src="../style/images/ajb_all_600.png" width="120" alt="" class="ewm"><br>        下载中介宝APP</p>
        <!-- <i class="iconfont">&#xe67b;</i> -->
        <div><i class="iconfont">&#xe69e;</i> 您的手机号已经绑定电脑版账号为：</div>
        <h1 class="lname"></h1>
        <p class="conts">（登录密码一致。请用该账号登录电脑版，可实现电脑版和手机版“收藏、新房推荐记录”等数据的同步！）</p>
        <a onclick="api.close()" class="submit"></a>
    </div>
</div>

</body>
</html>