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
<script src="${refPrefix}/js/jquery.js" type="text/javascript"></script>
<script src="${refPrefix}/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.timers.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.input.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.j.tool.js" type="text/javascript"></script>
<script src="${refPrefix}/js/buildHtml.js" type="text/javascript" ></script>
<script src="${refPrefix}/js/pagination.js"  type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
<script type="text/javascript">
var queryOptions;
try{
}catch(e){}

function doSearch(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/areas/list',
    data:a,
    mysuccess: function(data){
        var houseData=JSON.parse(data);
        if (houseData.leibie=='chongfu') {
          buildHtmlWithJsonArray("id_House_list",houseData['list']);
        }else {
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
      }
        setTableFix();
    }
  });
}

function editArea(id){
  art.dialog.open('/v/settings/area_edit.html?id='+id,{
      id:'EditArea',
      title:'修改楼盘',
      height:450,
      width:700,
    })
}

function editMap(id,name){
  art.dialog.open('/v/settings/baidu_map.html?area='+name+'&&id='+id,{
      id:'EditMap',
      title:'修改楼盘',
      height:600,
      width:900,
    })
}

function viewArea(name){
  $('#search').val(name);
  $('#leibie').val('');
  doSearch();
}

function addArea(){
  art.dialog.open('/v/settings/area_add.html',{
      id:'AddArea',
      title:'添加楼盘',
      height:450,
      width:700,
    })
}

function deleteArea(id){
  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
      YW.ajax({
        type: 'POST',
        url: '/c/areas/delete?id='+id,
        data:'',
        mysuccess: function(data){
            doSearch();
            alert('删除成功');
        }
      });
  },function(){},'warning');
}

$(document).ready(function() {
        Page.Init();

doSearch();
});
$(window).resize(function() {      //类别
});

var targetId="";
function setTargetId(id){
	targetId=id;
}
function mergeDistrict(){
	var ids=[];
	var box = $('.checkbox');
	for(var i=0;i<box.length;i++){
		if(box[i].checked){
			ids.push($(box[i]).attr('data-id'));
		}
	}
	if(ids.length==0){
		alert('请先选择需要合并的楼盘');
		return;
	}
	if(!targetId){
		alert('请先选择合并的目标楼盘');
		return ;
	}
	
	
	art.dialog.confirm('确定要合并选中的楼盘吗？', function () {
	      YW.ajax({
	        type: 'POST',
	        url: '/c/areas/merge?targetId='+targetId+'&ids='+ids.join(),
	        data:'',
	        mysuccess: function(data){
	        	targetId="";
	            doSearch();
	            alert('合并成功');
	        }
	      });
	  },function(){},'warning');
}
</script>
<style type="text/css">
.checkbox{position:absolute;top: 0px; right: 6px}
.radio{position:absolute;top: 0px;    left: 6px}

</style>
</head>
<body>
<div class="winThree list title addSide">
    <div class="winHeader">
      <div class="maxHW title">
          <ul class="winMenuTop menuLi clearfix title" id="menuTop">
          	<jsp:include page="menuTop.jsp"></jsp:include>
          </ul>
      </div>
      <form class="form-horizontal form1 " onsubmit="doSearch();return false;" role="form" name="form1">
        <input type="hidden" class="hidden" name="pageInput" value="">
        <div class="form-group clearfix">
        <div class="input-group input-group-max" style="width:200px;">
          <span class="input-group-addon">楼盘名称</span>
          <input type="text" class="form-control" name="search" id="search">
        </div>
      <div class="input-group input-group-min" style="width:150px;">
        <span class="input-group-addon">类别</span>
        <select class="form-control " name="leibie" id="leibie">
          <option value="">所有</option>
          <option value="chongfu">重复</option>
          <option value="kong">经纬度空</option>
        </select>
      </div>
        <div class="btn-group">
          <button type="button" class="btn btn-primary " onclick="doSearch();">搜索</button>
        </div>
        </div>
      </form>

      <table class="table table-nopadding TableH " >
        <tr>
        	<th><a href="javascript:void(0)" onclick="mergeDistrict();">合并</a></th>
          <th>操作</th>
          <th>名称</th>
          <th>区域</th>
          <th>拼音</th>
          <th>缩写</th>
          <th>地址</th>
          <th>经纬度</th>
        </tr>
      </table>
    </div>
    <div class="winBodyer " style=" top: 127px;">
      <div class="maxHW" style="min-width: 700px;">

        <table class="table table-hover table-striped table-nopadding TableB" >
          <tbody>
          <tr data-hid="$[id]" style="display:none;" class="id_House_list">
          	<td style="width:45px;position:relative;"><input class="radio"  onclick="setTargetId($[id])" name="targetId" type="radio"/><input data-id="$[id]"  class="checkbox" type="checkbox"/></td>
            <td style="width:130px">
              <a href="javascript:void(0)" class="btns" data-type="edit" onclick="editArea($[id])">编辑</a>
              <a href="javascript:void(0)" class="btns" data-type="edit" onclick="viewArea('$[name]')">查看</a>
              <a href="javascript:void(0)" class="btns" data-type="del" onclick="deleteArea($[id])">删除</a>
              <a href="javascript:void(0)" class="btns" data-type="map" onclick="editMap($[id],'$[name]')"> 地图</a>
            </td>
            <td style="width:100px">$[name]</td>
            <td style="width:60px">$[quyu]</td>
            <td style="width:120px">$[pinyin]</td>
            <td style="width:80px">$[pyShort]</td>
            <td>$[address]</td>
            <td style="width:200px">$[maplat] , $[maplng]</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="winFooter">
        <div class="maxHW mainCont ymx_page foot_page_box"></div>
    </div>
</div>
</body>
</html>