<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="height:570px;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="../inc/top.jsp" />
<title>无标题文档</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/user.js"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script src="/js/validate.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/keyEnter.js"></script>
<script type="text/javascript">
var lxingcont=0;
var hxingcont=0;
var zxiucont=0;
var quyucont=0;
var cid=${me.cid};
var id;
var dataScope="ky";
function save(){
	 if(!$('#name').val()){
		  alert('请先检查客户姓名');
	      return ;
	    }
	  if(checkDateyear($('#areas'))==false){
		  alert('请先输入期望楼盘名称');
	      return ;
	    }
	  if(checkTel()==false){
		  alert('请先检查客户电话号码');
	    return;
	  }
  var a=$('form[name=form1]').serialize();
  if ($('#uid').val()==undefined||$('#uid').val()=='') {
    alert('请先选择业务员');
  }else if ($('#TelAll').val()==undefined||$('#TelAll').val()=='') {
    alert('请填写联系人电话');
  }else{
    YW.ajax({
      type: 'POST',
      url: '/c/client/update',
      data:a,
      mysuccess: function(data){
          // $('#saveBtn').removeAttr('disabled');
        art.dialog.close();
        art.dialog.opener.updateClient(id,data);
        alert('修改成功');
      }
    });
  }
}

function close(){
  art.dialog.close();
}

function deleteThis(){
  event.preventDefault();
  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
  YW.ajax({
    type: 'POST',
    url: '/c/client/delete?id='+id,
    mysuccess: function(data){
      art.dialog.opener.deleteClient();
      art.dialog.close();
      alert('删除成功');
    }
  });
  },function(){},'warning');
}

function setSearchValue(index , click){
    var ThiA=$('#autoCompleteBox').find('a');
    ThiA.removeClass('hover');
    var Vals=ThiA.eq(index).addClass('hover').attr('area');
    $('#areas').val(Vals);
  //  if(click){
      houseAddMain();
      $("#HouseBoxMore div i").click(function(){
                
        var telText = "";
        $("#HouseAreaAll").val("");
        $(this).parent("div").remove();
        
        var divNumChange = $("#HouseBoxMore div").size();
        $("#HouseBoxMore div").each(function(i) {
          if(i == (divNumChange-1)){
           telText += $(this).find("span").text();
          }else{
           telText += $(this).find("span").text() + "/";
          }
        });
        $("#HouseAreaAll").val(telText);
		
		var pleft = $("#HouseBoxMore").width() + 10 ;
		  var wInput = 688 - pleft;
		
		  $("#areas").css({
			"padding-left":pleft + 5,
			"width":wInput
		  });
		  
		  $("#areas").focus();

      });  
  //  }
    
}

function getContent(){
  YW.ajax({
    type: 'get',
    url: '/c/client/get?id='+id,
    data:'',
    mysuccess: function(result){
      var json = JSON.parse(result);
      $('#id').val(json['id']);
      $('#name').val(json['name']);
      // $('#tel').val(json['tels']);
      if(json['tels']!=undefined && json['tels']!=""){
        var arr = json['tels'].split('/');
        for(var i=0;i<arr.length;i++){
          if(arr[i]==""){
            continue;
          }
          $('#tel').focus();
          $('#tel').val(arr[i]);
          event.keyCode=13;
          addTel(event);
        }
      }
      if(json['areas']!=undefined && json['areas']!=""){
        var arr = json['areas'].split(',');
        for(var i=0;i<arr.length;i++){
          if(arr[i]==""){
            continue;
          }
          $('#areas').focus();
          $('#areas').val(arr[i]);
          addHouse(event);
        }
      }
      $('#lcengFrom').val(json['lcengFrom']);
      $('#lcengTo').val(json['lcengTo']);
      // $('#areas').val(json['areas']);
      $('#mjiFrom').val(json['mjiFrom']);
      $('#mjiTo').val(json['mjiTo']);
      $('#jiageFrom').val(json['jiageFrom']);
      $('#jiageTo').val(json['jiageTo']);
      $('#djiaFrom').val(json['djiaFrom']);
      $('#djiaTo').val(json['djiaTo']);
      $('#yearFrom').val(json['yearFrom']);
      $('#yearTo').val(json['yearTo']);
      $('#beizhu').val(json['beizhu']);
      chaifen(json['lxings']);
      chaifen(json['zxius']);
      chaifen(json['hxings']);
      chaifen(json['quyus']);
      $('#did').val(json['did']);
      $('#did').change();
      $('#uid').val(json['uid']);
    }
  });
}

function chaifen(val,name){
  if (val==null) {
    return;
  }else{
    var row = val.split(',');
    for (var i = 0; i < row.length ; i++) {
      var value = row[i];
      $('input[value="'+value+'"]').attr('checked','checked');
    }
  }
}

$(document).ready(function() {
  id = getParam('id');
  // autoComplete($('#search'));
  $.get('/c/config/getQueryOptions', function(data) {
    queryOptions=JSON.parse(data);
    buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
    buildHtmlWithJsonArray("id_zxiu",queryOptions['zhuangxiu'],true);
    buildHtmlWithJsonArray("id_hxing",queryOptions['fxing'],true);
    buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
    getContent();
    autoComplete($('#areas'));
  });
  
  
   
  
  
  
});

</script>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
</head>

<body >

<script>
   $(document).keypress(function(e) { 
       if(e.which == 13) {
        e.preventDefault();
          // jQuery(".confirmButton").click(); 
       } 
   }); 
</script>
<form name="form1" role="form">
<input type="hidden" name="id" id="id"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="layerTable1 TableBgColor1" style=" border-top:4px solid #e9e9e9;">

  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">姓名：</td>
    <td width="300" class="TableTdBgColor"><input type="text" class="w1" id="name" name="name" desc="客户姓名"/></td>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">电话：</td>
    <td width="356" class="TableTdBgColor" style="position:relative;"><div id="TelBoxMore" style="margin: 0 0 0 2px; top:10px; position:absolute;"></div><input type="text" id="tel" style="width:315px;"    placeholder="输入号码后按回车键（Enter）确认，仅限3个号码。"  /><input type="hidden" value="" name="tels" id="TelAll"/></td>
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot" style="white-space:nowrap;"><span style="margin-right:12px;">期望</span><br />楼盘：</td>
    <td class="TableTdBgColor" colspan="3" width="726" height="35" style="position:relative;">
          <input type="text" class="w1" id="areas" autocomplete="off" placeholder="添加楼盘后按回车键（Enter）确认，仅限5个楼盘。" style="float:left; width:690px; position:absolute; left:8px; top:5px; z-index:1;" /><input type="hidden" name="areas" id="HouseAreaAll" />
          <div id="HouseBoxMore" style=" top:8px; left:15px; position:absolute; z-index:10;"></div>
    </td>
    
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot">楼型：</td>
    <td class="TableTdBgColor">
        <div class="id_lxing"><input type="checkbox" value="$[name]" id="lxings" name="lxings" onclick="jianCe(this);" />$[name]</div>
    </td>
    <td align="right" class="TableTdBorBot">装潢：</td>
    <td class="TableTdBgColor">
        <div class="id_zxiu"><input type="checkbox" value="$[name]" name="zxius" id="zxius" onclick="jianCe(this);"/>$[name]</div>
    </td>
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot">户型：</td>
    <td class="TableTdBgColor">
        <div class="id_hxing"><input type="checkbox" value="$[name]" name="hxings" id="hxings" onclick="jianCe(this);"/>$[name]</div>
    </td>
    <td align="right" class="TableTdBorBot">区域：</td>
    <td class="TableTdBgColor">
        <div class="id_quyu"><input type="checkbox" value="$[name]" name="quyus" id="quyus" onclick="jianCe(this);"/>$[name]</div>
    </td>
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot">面积：</td>
    <td class="TableTdBgColor"><input type="text" class="w2" desc="面积" onkeyup="checkmji(this)" id="mjiFrom" name="mjiFrom" />-<input type="text" desc="面积" class="w2" onkeyup="checkmji(this)" id="mjiTo" name="mjiTo"/> ㎡</td>
    <td align="right" class="TableTdBorBot">价格：</td>
    <td class="TableTdBgColor"><input type="text" class="w2" desc="价格" onkeyup="checkzjia(this)" id="jiageFrom" name="jiageFrom" />-<input type="text" class="w2" desc="价格" onkeyup="checkzjia(this)" id="jiageTo" name="jiageTo"/> 万</td>
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot">单价：</td>
    <td class="TableTdBgColor"><input type="text" class="w2" desc="单价" onkeyup="checkdjia(this)" id="djiaFrom" name="djiaFrom" />-<input type="text" desc="单价" class="w2" onkeyup="checkdjia(this)" id="djiaTo" name="djiaTo"/></td>
    <td align="right" class="TableTdBorBot">楼层：</td>
    <td class="TableTdBgColor"><input type="text" class="w2" id="lcengFrom" onkeyup="checklceng(this)" desc="楼层" name="lcengFrom" />-<input type="text" desc="楼层" class="w2" onkeyup="checklceng(this)" id="lcengTo" name="lcengTo" /> 层</td>
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot">年代：</td>
    <td class="TableTdBgColor"><input type="text" class="w2" onkeyup="checkyear(this)" id="yearFrom" desc="年代" name="yearFrom" />-<input type="text" class="w2" desc="年代" onkeyup="checkyear(this)" id="yearTo" name="yearTo"/></td>
    <td align="right" class="TableTdBorBot" style="white-space:nowrap;">业务员：</td>
    <td colspan="3" class="TableTdBgColor">
        <select name="did" id="did" class="input-sm input-rightB w3 dept_select">
	    </select>
	    <select name="uid" id="uid" class="input-sm input-left w3 user_select">
        </select>
    </td>
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot">备注：</td>
    <td colspan="3" class="TableTdBgColor">
       <textarea class="TA" name="beizhu" id="beizhu"></textarea>
    </td>
  </tr>
  <tr>
    <td align="right" class="" style="background-color:#e9e9e9;padding:0px;"> </td>
    <td colspan="3" class="" height="50" style=" background-color:#e9e9e9;padding:0px;">
       <button onclick="save();return false;" class="save btn btn-primary btn-block" >保存</button><!-- <button class="cancel" onclick="close();">取消</button> -->
       <button class="cancel" onclick="deleteThis();">删除</button>
    </td>
  </tr>
  
</table>
</form>
</body>
</html>
