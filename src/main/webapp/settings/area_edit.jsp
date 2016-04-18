<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
var id;
function save(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/areas/update',
    data:a,
    mysuccess: function(data){
        art.dialog.close();
        art.dialog.opener.doSearch();
        alert('修改成功');
        
    }
  });
}

function getContent(){
  YW.ajax({
    type: 'get',
    url: '/c/areas/get?areaId='+id,
    data:'',
    mysuccess: function(result){
      var json = JSON.parse(result);
      $('#id').val(json['area']['id']);
      $('#name').val(json['area']['name']);
      $('#id_quyu').val(json['area']['quyu']);
      $('#address').val(json['area']['address']);
      $('#maplat').val(json['area']['maplat']);
      $('#maplng').val(json['area']['maplng']);
    }
  });
}


var parent = art.dialog.parent,       // 父页面window对象
api = art.dialog.open.api,  //      art.dialog.open扩展方法
form1=$('.form1');
    api.title('修改楼盘')
    .size(200, 300)
    .button({
      name: '保存',
      callback: function () {
        save();
        return false;
      },focus: true
    },{
      name: '取消'
    });
function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a'),
    ThiAH=ThiA.eq(index);
    ThiA.removeClass('hover');
    var Vals=ThiAH.addClass('hover').attr('title');
    $('#area').val(Vals);
    $('#address').val(ThiAH.data('address'));
    $('#quyu option[value="'+ThiAH.data('quyu')+'"]').attr('selected',true);
}
$(document).ready(function() {
  id = getParam('id');
  if(id==null || id==""){
    alert("未指明的分类");
  }
    $.get('/c/config/getQueryOptions', function(data) {
        queryOptions=JSON.parse(data);;
        buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
        getContent();
    });
//添加提示框
});
</script>
</head>
<body>
<div class="html edit title" style="height:500px; width:620px;">
    <div class="bodyer mainCont">
        <div class="maxHW">

            <form class="form-horizontal form_label_right form1" name="form1" role="form" onsubmit="return false;">
                <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <div class="col-xs-8 hidden">
                                <input type="text" class="form-control" value="" name="id" id="id" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="area" class="col-xs-4 control-label">楼盘名称:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" name="name" id="name" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="quyu" class="col-xs-4 control-label">区域:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="quyu" id="id_quyu" type="tabs">
                                    <option class="id_quyu" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="area" class="col-xs-4 control-label">楼盘地址:</label>
                            <div class="col-xs-8">
                                <input type="text" style="width:400px;" class="form-control" value="" name="address" id="address" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dateyear" class="col-xs-4 control-label">经纬度:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" name="maplat" id="maplat" placeholder="纬度">
                                <input type="text" class="form-control" value="" name="maplng" id="maplng" placeholder="经度">
                            </div>
                        </div>
                    </div>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>