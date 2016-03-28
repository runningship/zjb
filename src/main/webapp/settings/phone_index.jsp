<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../inc/resource.jsp"></jsp:include>
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
<link href="${refPrefix}/style/css.css" rel="stylesheet">
<link href="${refPrefix}/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${refPrefix}/style/style.css" rel="stylesheet">
<link href="${refPrefix}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="${refPrefix}/js/jquery.js" type="text/javascript"></script>
<script src="${refPrefix}/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.timers.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.input.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.j.tool.js" type="text/javascript"></script>
<script src="${refPrefix}/js/DatePicker/WdatePicker.js" type="text/javascript" ></script>
<script src="${refPrefix}/js/buildHtml.js" type="text/javascript" ></script>
<script src="${refPrefix}/js/pagination.js" type="text/javascript" ></script>
<script type="text/javascript">
var queryOptions;
try{
}catch(e){}

function doSearch(page){
	if(page){
		  Page.currentPageNo=page;
		  $('.pageInput').val(1);
	  }
  var a=$('form[name=form1]').serialize();
  
  YW.ajax({
    type: 'POST',
    url: '/c/user/listphone',
    data:a,
    mysuccess: function(data){
        var houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        setTableFix();
        // if(event==undefined || $(event.srcElement).attr('action')!='page'){
        //   $('.pageInput').val(1);
        // }
        if(houseData['page']['data']<=0){
          $('.id_House_list').parents('.maxHW').addClass('list_noThing');
        }else{
          $('.id_House_list').parents('.maxHW').removeClass('list_noThing');
        }
    }
  });
}

function uc_edit(id){
  art.dialog.open('/v/settings/phone_edit.html?id='+id)
}

function SeeCharge(id){
  art.dialog.open('/v/settings/phone_ChargeList.html?id='+id)
}

function setOrderField(field){
	$('#orderBy').val(field);
	doSearch(1);
}
$(document).ready(function() {
  Page.Init();
  doSearch(1);
   //设置拖动栏
   var bodyW = $(window.top.document).width()-50;
   //-200为margin-right:200
   $(window.top.document).find('#dragbar').width(bodyW-$('#menuTop').width()-200);
});
$(window).resize(function() {      //类别
});
</script>

</head>
<body>
<div class="html list title addSide">
    <div class="header">
        <div class="maxHW title">
            <ul class="menuLi clearfix title" id="menuTop">
            	<jsp:include page="menuTop.jsp"></jsp:include>
            </ul>
        </div>
  <form class="form-horizontal form1" onsubmit="doSearch();return false;" role="form" name="form1">
    <input type="hidden" class="hidden" name="pageInput" value="">
    <input type="hidden" class="hidden" name="order" value="desc">
    <input type="hidden" class="hidden" name="orderBy" id="orderBy" value="u.lastPaytime">
    <div class="form-group clearfix">
        <div class="input-group input-group-max" style="width:200px;">
          <span class="input-group-addon">电话号码</span>
          <input type="text" class="form-control" name="tel" id="tel">
        </div>
        <div class="input-group input-group-max" style="width:200px;">
          <span class="input-group-addon">用户名</span>
          <input type="text" class="form-control" name="name" id="name">
        </div>
      <div class="input-group input-group-min" style="width:150px;">
        <span class="input-group-addon">类别</span>
        <select class="form-control " name="mobileON" id="mobileON">
          <option value="">全部</option>
          <option value="1">开启</option>
          <option value="0">未开启</option>
        </select>
      </div>
      <div class="input-group input-group-max" style="width:300px;">
                            <span class="SpanNameH2 not-select" style="float:left;margin-right:10px;margin-left:30px;"><em>日期</em></span>
                            <div class="DivBoxW1 Fleft" style="display:inline">
                            <input type="text" class="form-control input-left input-sm" name="rqtimeStart" id="idTimes" onFocus="var timeend=$dp.$('idTime');WdatePicker({lang:'zh-cn',onpicked:function(){idTime.focus();},maxDate:'#F{$dp.$D(\'idTime\')}'})" style="width:100px;height:35px;">
        <input type="text" class="form-control input-left input-sm" name="rqtimeEnd" id="idTime" onFocus="WdatePicker({lang:'zh-cn',minDate:'#F{$dp.$D(\'idTimes\')}'})" style="width:100px;height:35px;">
                            </div>
	        </div>
      <div class="btn-group">
          <button type="button" class="btn btn-primary " onclick="doSearch(1);">搜索</button>
      </div>
    </div>
  </form>

  <table class="table table-nopadding TableH " >
    <tr>
      <th>电话号码</th>
      <th>用户名</th>
      <th style="cursor:pointer" onclick="setOrderField('addtime')">注册时间</th>
      <th style="width:80px;cursor:pointer;" onclick="setOrderField('mobileDeadtime')">到期时间</th>
      <th style="width:100px;cursor:pointer;" onclick="setOrderField('lastPaytime')">最近付费时间</th>
      <th>操作</th>
    </tr>
  </table>
 
    </div>
    <div class="bodyer">
      <div class="maxHW" style="min-width: 700px;">

        <table class="table table-hover table-striped table-nopadding TableB" >
          <tbody>
          <tr data-hid="$[id]" style="display:none;" class="id_House_list">
            <td>$[tel]</td>
            <td>$[uname]</td>
            <td>$[addtime]</td>
            <td style="width:80px;">$[endtime]</td>
            <td style="width:100px;">$[lastPaytime]</td>
            <td><span class="btn-group null-float">
              <a href="javascript:void(0)" auth="sz_user_edit" class="btns btn btn-xs btn-primary" data-type="edit" onclick="uc_edit($[uid])">编辑</a>
              <a href="javascript:void(0)" auth="sz_user_del" class="btns btn btn-xs btn-danger" data-type="del" onclick="SeeCharge($[uid])">充值查看</a>
              </span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="footer">
        <div class="maxHW mainCont ymx_page foot_page_box"></div>
    </div>
</div>
</body>
</html>