<%@page import="com.youwei.zjb.house.entity.SchoolDistrict"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String id = request.getParameter("id");
CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
SchoolDistrict po = dao.get(SchoolDistrict.class, Integer.valueOf(id));
request.setAttribute("district", po);
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
var id;
function submits(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/schoolDistrict/update',
    data:a,
    mysuccess: function(data){
    	var json = JSON.parse(data);
        art.dialog.close();
        art.dialog.opener.updateXueQu(json.district);
        alert('修改成功');
    }
  });
}

$(document).ready(function() {
    var parent = art.dialog.parent,       // 父页面window对象
    api = art.dialog.open.api,  //      art.dialog.open扩展方法
    form1=$('.form1');
    if (!api) return;
    // 操作对话框
    api.title('学区编辑')
    // 自定义按钮
      .button({
          name: '保存',
          callback: function () {
            form1.submit();
            return false;
          },focus: true
      },{
    	  name:'删除',
    	  callback: function () {
    		  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
    			  delXueQu();
   			  },function(){},'warning');
              return false;
            }
      });
    $('#idCname').focus();
});

function delXueQu(){
	YW.ajax({
	    type: 'POST',
	    url: '/c/schoolDistrict/delete?id='+${district.id},
	    mysuccess: function(data){
	    	art.dialog.opener.removeXueQu(${district.id});
	      	alert('删除成功');
	      	art.dialog.close();
	    }
	  });
}
</script>
</head>
<body>
<div class="html edit title">
    <div class="bodyer mainCont">
        <div class="maxHW" style="min-width: 500px;">
            <form name="form1" class="form-horizontal form_label_right form1" role="form" onsubmit="submits();return false;">
            	<input type="hidden" value="${district.id }" name="id" />
                <div>
                    <input type="hidden" name="id" id="id">
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">学区名:</label>
                    <div class="col-xs-8">
                        <input  class="form-control" name="name"  value="${district.name }" id="name" placeholder="学区名称" >
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">offsetX:</label>
                    <div class="col-xs-8">
                        <input  class="form-control" name="offsetX" value="${district.offsetX }" id="offsetX" placeholder="学区名称显示位置左右偏移">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-xs-3 control-label">offsetY:</label>
                    <div class="col-xs-8">
                        <input  class="form-control" name="offsetY" value="${district.offsetY }" id="offsetY" placeholder="学区名称显示位置上下偏移" >
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>
</body>
</html>