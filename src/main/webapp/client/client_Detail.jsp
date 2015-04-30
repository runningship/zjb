<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript">
var id;
var cid=$${cid};
function save(){
  if($('#conts').val()==""){
    event.preventDefault();
    return false;
  }
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/client/gj/add',
    data:a,
    mysuccess: function(data){
      $('#conts').val('');
      getGenjins();
      $('#btn_submit').css('background-color','#ddd');
      $('#btn_submit').css('border-color','#ddd');
      alert('跟进成功');
    }
  });
}

function getContent(){
  YW.ajax({
    type: 'get',
    url: '/c/client/get?id='+id,
    data:'',
    mysuccess: function(result){
      var json = JSON.parse(result);
      $('#id').val(json['id']);
      $('#name').html(json['name']);
      $('#tel').html(json['tels']);
      panduan(json['lcengFrom'],json['lcengTo'],'层','#lceng');
      panduan2(json['areas'],'#areas');
      panduan(json['mjiFrom'],json['mjiTo'],'㎡','#mji');
      panduan(json['jiageFrom'],json['jiageTo'],'万元','#jiage');
      panduan(json['djiaFrom'],json['djiaTo'],'元','#djia');
      panduan3(json['yearFrom'],json['yearTo'],'#year');
      $('#beizhu').html(json['beizhu']);
      panduan2(json['lxings'],'#lxings');
      panduan2(json['zxius'],'#zxius');
      panduan2(json['hxings'],'#hxings');
      panduan2(json['quyus'],'#quyus');
      $('#user').html(json['dname']+' - '+json['uname']);
      $('#djr').html(json['djrDname']+' - '+json['djrName']);
    }
  })
}

function panduan2(a,d){
  if (a==0||a==''||a==undefined) {
    $(d).html('不限');
  }else{
    $(d).html(a);
  }
}

function panduan(a,b,c,d){
  if (a==0&&b==0) {
      $(d).html('不限');
  }else if (a==0) {
      $(d).html('小于'+b+c);
  }else if (b==0) {
      $(d).html('大于'+a+c);
  }else {
      $(d).html(a+c+'-'+b+c);
  }
}

function panduan3(a,b,d){
  if (a==0&&b==0) {
      $(d).html('不限');
  }else if (a==0) {
      $(d).html(b+'年前');
  }else if (b==0) {
      $(d).html(a+'年后');
  }else {
      $(d).html(a+'年-'+b+'年');
  }
}

function getGenjins(){
  YW.ajax({
    type: 'get',
    url: '/c/client/gj/list?clientId='+id,
    data:'',
    mysuccess: function(result){
      var json = JSON.parse(result);
        buildHtmlWithJsonArray("see_client_genjin",json['page']['data']);
    }
  });
}

function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a');
    ThiA.removeClass('hover');
    var Vals=ThiA.eq(index).addClass('hover').attr('area');
    $('#search').val(Vals);
}

$(document).ready(function() {
  $('#conts').focus();
  id = getParam('id');
  getContent();
  getGenjins();
});

function contsChange(val){
  if(val==""){
    $('#btn_submit').css('background-color','#ddd');
    $('#btn_submit').css('border-color','#ddd');
  }else{
    $('#btn_submit').css('background-color','#71d3ba');
    $('#btn_submit').css('border-color','#71d3ba');
  }
}
</script>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
</head>

<body>

<script>
   $(document).keypress(function(e) { 
    // 回车键事件 
       if(e.which == 13) { 
          jQuery(".confirmButton").click(); 
       } 
   }); 
</script>
<form name="form1" role="form">
<input type="hidden" name="clientId" id="id"/>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="layerTable1 TableBgColor1" style=" border-top:4px solid #e9e9e9;float:none;">
  <tr>
    <td width="80" height="33" align="right" class="TableTdBorBot TableTdBorTop">姓名：</td>
    <td width="318" class="TableTdBgColor" id="name"><span class="w1 h"></span></td>
    <td width="80" align="right" class="TableTdBorBot TableTdBorTop">电话：</td>
    <td width="318" class="TableTdBgColor" id="tel"><span class="w1 h"></span></td>
  </tr>
  <tr>
    <td align="right" height="33" class="TableTdBorBot">楼盘：</td>
    <td class="TableTdBgColor" id="areas"><span class="w1 h"></span></td>
    <td align="right" class="TableTdBorBot">楼层：</td>
    <td class="TableTdBgColor" id="lceng"><span class="w1 h"></span></td>
  </tr>
  <tr>
    <td align="right" height="33" class="TableTdBorBot">楼型：</td>
    <td class="TableTdBgColor" id="lxings">
        <span class="w1 h"></span>
    </td>
    <td align="right" class="TableTdBorBot" style="cursor:default;">装潢：</td>
    <td class="TableTdBgColor" id="zxius">
        <span class="w1 h"></span>
    </td>
  </tr>
  <tr>
    <td align="right" height="33" class="TableTdBorBot">户型：</td>
    <td class="TableTdBgColor" id="hxings">
        <span class="w1 h"></span>
    </td>
    <td align="right" class="TableTdBorBot">区域：</td>
    <td class="TableTdBgColor" id="quyus">
        <span class="w1 h"></span>
    </td>
  </tr>
  <tr>
    <td align="right" height="33" class="TableTdBorBot">面积：</td>
    <td class="TableTdBgColor" id="mji"><span class="w1 h"> </span></td>
    <td align="right" class="TableTdBorBot">总价：</td>
    <td class="TableTdBgColor" id="jiage"><span class="w1 h"></span></td>
  </tr>
  <tr>
    <td align="right" class="TableTdBorBot">单价：</td>
    <td class="TableTdBgColor" id="djia"><span class="w1 h"></span></td>
    <td align="right" class="TableTdBorBot">登记人员：</td>
    <td colspan="3" class="TableTdBgColor" id="djr">
        <span class="w1 h"></span>
    </td>
  </tr>
  <tr>
    <td align="right" height="33" class="TableTdBorBot">年代：</td>
    <td class="TableTdBgColor" id="year"><span class="w1 h"></span></td>
    <td align="right" class="TableTdBorBot">业务人员：</td>
    <td colspan="3" class="TableTdBgColor" id="user">
        <span class="w1 h"></span>
    </td>
  </tr>
  <tr>
    <td align="right" height="33" class="TableTdBorBot">备注：</td>
    <td colspan="3" class="TableTdBgColor" id="beizhu">
       <span class="w1 h" style=""></span>
    </td>
  </tr>
  
  <tr>
     <td colspan="4" class="TableTdBorBot" height="28">
        <span style=" margin:0 35px;">跟进</span>
     </td>
  </tr>
  <tr>
     <td colspan="4" class="TableTdBorBot" height="33" bgcolor="#ffffff">
      <textarea type="text" placeholder="好记性不如多跟进" style=" margin-left:35px; width:600px;height:50px; padding:5px; resize:none; border:1px solid #b6b6b6; float:left;" id="conts" onkeyup="contsChange(this.value);" name="conts"></textarea><button id="btn_submit" style=" width:80px; height:33px;margin-top:28px; border:1px solid #dddddd; color:#333333; border-radius:0; float:left;margin-left:3px;" onclick="save();return false;" >提交</button>
     </td>
  </tr>
        <table class="TableMainLGj " width="100%">
          <tbody width="100%">
            <tr class="see_client_genjin list" style="display:none;">
              <td style="padding-left:40px;">
                $[conts]
                <p>$[dname] $[uname]<span style="float:right">$[addtime]</span></p>
              </td>
            </tr>
            
          </tbody>
        </table>
</table>
</form>
</body>
</html>
