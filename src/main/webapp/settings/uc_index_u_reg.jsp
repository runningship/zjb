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
Department comp = user.Company();
int count=0;
List<Map> list = dao.listAsMap("select max(lname) as maxLName from User where cid=?", comp.id);
if(!list.isEmpty()){
	Object mlname = list.get(0).get("maxLName");
	if(mlname!=null && !"null".equals(mlname)){
		count = Integer.valueOf(mlname.toString());
	}
}
String pattern="0000000";
String lname="";
 java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
 if(count==0){
	 lname=comp.cnum+"0001";
 }else{
	 lname=df.format(count+1);
 }
 request.setAttribute("lname", lname);
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
<script type="text/javascript">
function submits(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/user/reg',
    data:a,
    mysuccess: function(data){
        alert('添加成功');
    }
  });
}

$(document).ready(function() {
    var parent = art.dialog.parent,       // 父页面window对象
    api = art.dialog.open.api,  //      art.dialog.open扩展方法
    form1=$('.form1');
    if (!api) return;
    // 操作对话框
    api.title('添加用户')
    // 自定义按钮
      .button({
          name: '保存',
          callback: function () {
            form1.submit();
            return false;
          },focus: true
      },{
          name: '取消'
      });
});

var sendingVerifyCode;
function getVerfiyCode(btn){
	event.preventDefault();
	event.cancelBubbled=true;
	if(sendingVerifyCode){
		return;
	}
	var tel = $('#tel').val();
	if(!tel){
		alert('请先填写有效手机号码');
		return;
	}
	tel = tel.trim();
	$(btn).addClass('gray');
	//提示信息
	YW.ajax({
		url:'/c/mobile/user/sendVerifyCode',
		method:'post',
		data: {tel:tel , cityPy:'${cityPy}'},
		cache:false,
		returnAll:false
	},function(ret , err){
		if(ret){
			sendingVerifyCode = true;
			setcode();
		}else{
			alert(err.msg);
		}
	});
	
	sendingVerifyCode = true;
	setcode();
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
</script>
<style type="text/css">
.yzm{width: 60%;    float: left;}
.getCode{    float: right;  color: blue; padding:5px 22px;}
.gray{color:gray;}
</style>
</head>
<body>
<div class="html edit title">
    <div class="bodyer mainCont">
        <div class="maxHW" style="min-width: 500px;">
            <form name="form1" class="form-horizontal form_label_right form1" role="form" onsubmit="submits();return false;">
                
                <div class="form-group">
                    <label class="col-xs-3 control-label">登录帐号:</label>
                    <div class="col-xs-8">
                        <input type="text" class="form-control" name="lname" readonly="readonly" value="${lname}" id="lname" placeholder="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">姓名:</label>
                    <div class="col-xs-8">
                        <input type="text" class="form-control" name="uname" placeholder="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">登录密码:</label>
                    <div class="col-xs-8">
                        <input type="password" class="form-control" name="pwd" value="" placeholder="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">电话:</label>
                    <div class="col-xs-8">
                        <input id="tel"  type="text" class="form-control" name="tel" placeholder="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">验证码:</label>
                    <div class="col-xs-8">
                        <input type="text"   class="form-control yzm" name="verifyCode" placeholder="">
                        <button class="getCode" onclick="getVerfiyCode(this);return false;">验证码</button>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>