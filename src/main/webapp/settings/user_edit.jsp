<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("user", ThreadSessionHelper.getUser());
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
<script type="text/javascript">
function submits(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/user/selfUpdate',
    data:a,
    mysuccess: function(data){
        //art.dialog.close();
        alert('修改成功');
        window.top.location.reload();
    }
  });
}

$(document).ready(function() {
    var parent = art.dialog.parent,       // 父页面window对象
    api = art.dialog.open.api,  //      art.dialog.open扩展方法
    form1=$('.form1');
    if (!api) return;
    // 操作对话框
    api.title('修改个人信息')
    // 自定义按钮
      .button({
          name: '保存',
          callback: function () {
            form1.submit();
            return false;
          },focus: true
      },{
          name: '取消'
      }).height(200);

});

</script>
</head>
<body>
<div class="html edit title">
    <div class="bodyer mainCont">
        <div class="maxHW" style="min-width: 500px;">
            <form name="form1" class="form-horizontal form1" role="form" onsubmit="submits();return false;">
            	<input type="hidden" name="id" value="${user.id }"/>
                <div>
                    <input type="hidden" name="id" id="id">
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">用 户 名:</label>
                    <div class="col-xs-8">
                        <input type="text" class="form-control" name="uname" value="${user.uname }" placeholder="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">手机号码:</label>
                    <div class="col-xs-8">
                        <input type="tel" class="form-control" name="tel" value="${user.tel }" placeholder="">
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>