<%@page import="com.youwei.zjb.user.entity.Department"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.youwei.zjb.user.entity.User"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="org.bc.sdak.TransactionalServiceHelper"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
User user = ThreadSessionHelper.getUser();

 //request.setAttribute("lname", lname);
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
<link href="/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/jquery.timers.js" type="text/javascript"></script>
<script src="/js/jquery.input.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>

<link  href="/js/YMX_input/css/style.css" rel="stylesheet">
<!-- <script src="/js/YMX_input/YMX_input.js" type="text/javascript"></script> -->

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
        uname.parents('label').addClass('form-err').find('.tip').text('请输入您的姓名');
        return;
    }else if(unameL<=1||unameL>4){
        alert('请输入您的真实姓名');
        uname.focus();
        uname.parents('label').addClass('form-err').find('.tip').text('请输入您的真实姓名');
        return;
    }
    var tel = $('#tel'),
    telV = tel.val();
	if(!validateMobile(telV)){
		alert('请输入正确的手机号码');
        tel.focus();
        tel.parents('label').addClass('form-err').find('.tip').text('请输入正确的手机号');
		return;
	}
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/user/reg',
    data:a,
    mysuccess: function(data){
        //alert('添加成功');
        var json = JSON.parse(data);
        setTimeout(function(){
        	$('#reg_tel').text(json.tel);
        	$('.lname').text(json.lname);
            divShow('yes');//显示添加成功的页面
        	//$('.form1').addClass('dn');
            //$('.alert').removeClass('dn');
        } , 1000);
    },
    myerror:function(data){
    	if(data.field=='lname'){
    		$('.lname').text(data.msg);
        	divShow('no1');//显示错误1的页面
    	}else{
    		alert(data['msg']);	
    	}
    }
  });
}

    var parent = art.dialog.parent,       // 父页面window对象
    api = art.dialog.open.api;  //      art.dialog.open扩展方法
$(document).ready(function() {
    var form1=$('.form1');
    if (!api) return;
    // 操作对话框
    api.title('添加用户')
    // 自定义按钮
      .button({
          name: '确定',
          callback: function () {
            $('.db').find('.submit').click();
            //form1.submit();
            return false;
          },focus: true
      },{
          name: '取消'
      });
});

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
var sendingVerifyCode;
function getVerfiyCode(btn){
	event.preventDefault();
	event.cancelBubbled=true;
	if(sendingVerifyCode){
		return;
	}
	var tel = $('#tel'),
    telV = tel.val();
	if(!validateMobile(telV)){
		alert('请先填写有效手机号码');
        tel.focus();
        tel.parents('label').addClass('form-err').find('.tip').text('请输入正确的手机号');
		return;
	}
	telV = telV.trim();
	$(btn).addClass('gray');
	sendingVerifyCode = true;
	YW.ajax({
	    type: 'POST',
	    url: '/c/mobile/user/sendVerifyCode',
	    data:{tel:telV , cityPy:'${cityPy}'},
	    mysuccess: function(data){
	    	alert('验证码已发送');
            $('.verifyCode').focus();
			setcode();
	    }
	  });
}

function setcode(){
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


$(document).ready(function() {
    $('.uname').focus();
});

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
}).on('change', 'input.input', function(event) {
    var Thi=$(this),
    ThiP   = Thi.parents('label');
    ThiP.removeClass('form-err').find('.tip').text('');
})
</script>
<style type="text/css">
.yzm{width: 60%;    float: left;}
.getCode{    float: right;  color: blue; padding:5px 22px;}
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

.form-err{ color: #F00; }
.form-err .input-label{ color: #F00;}
.form-err input.input{ color: #F00; border-color: #F00; background: #FEFFC4; }
.form-err span.tip{ color: #F00; }
</style>
</head>
<body>
<div class="bodybox">
    <form name="form1" class="boxs form-box form1 db" role="form" onsubmit="submits();return false;">
        <ul class="form-ul">
            <li class=""><label class="form-section form-active"><strong class="input-label">姓名：</strong><input type="text" class="input placeholders uname" name="uname" placeholder="您的姓名"><span class="tip">有</span></label></li>
            <li class=""><label class="form-section form-active"><strong class="input-label">手机号：</strong><input type="text" class="input placeholders" id="tel" name="tel" placeholder="您的手机号" maxlength="11" autocomplete="off"><span class="tip">有</span></label></li>
            <li class=""><label class="form-section form-active"><strong class="input-label">密码：</strong><input type="password" class="input placeholders" name="pwd" placeholder="登录密码"></label></li>
            <li class=""><label class="form-section tow form-active"><strong class="input-label">验证码：</strong><a href="#" class="btn btn_act code getCode" data-type="regCode" data-txt="发送验证码" onclick="getVerfiyCode(this);return false;">发送验证码</a><input type="text" class="input placeholders c" name="verifyCode" placeholder="收到的验证码"></label></li>
            <li class="">
                <input type="submit" class="submit hidden" value="submit">
            </li>
          </ul>
    </form>
    <div class="alert boxs yes dn">
        <p class="ewmbox"><img src="../style/images/ajb_all_600.png" width="120" alt="" class="ewm"><br>        下载中介宝APP</p>
        <!-- <i class="iconfont">&#xe67b;</i> -->
        <div><i class="iconfont">&#xe67b;</i> 您的电脑帐号注册成功:</div>
        <h1 class="lname"></h1>
        <h3>手机帐号：<span id="reg_tel"></span></h3>
        <p class="conts">（登录密码一致。请用该账号登录电脑版，可实现电脑版和手机版“收藏、新房推荐记录”等数据的同步！）</p>
        <a onclick="api.close()" class="submit"></a>
    </div>
    <div class="alert boxs no1 dn">
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