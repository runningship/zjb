<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="../inc/top.jsp" />
<title>中介宝房源软件系统</title>
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" src="/js/pagination.js"></script>
<script type="text/javascript" src="/js/user.js"></script>
<script type="text/javascript" src="/js/DatePicker/WdatePicker.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript">
var cid=${me.cid};
var rowId;
var dataScope="ky";
function doSearch(callback,isdel){
  if(event==undefined || $(event.srcElement).attr('action')!='page'){
    if(isdel==false){
      $('.pageInput').val(1);
    }
  }
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/client/list',
    data:a,
    mysuccess: function(data){
        houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_client_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        if(callback){
          callback();
        }
    }
  });
}

function updateClient(id,data){
  var json = JSON.parse(data);
  var tr = $('tr[data-cid='+json['id']+']');
  var a = tr.children()[0]
  var b = tr.children()[1]
  var c = tr.children()[2]
  var d = tr.children()[3]
  var e = tr.children()[4]
  var f = tr.children()[5]
  $(a).html(json['name']);
  $(b).html(json['tels']);
  $(c).html(getmianji2(json['mjiFrom'],json['mjiTo']));
  $(d).html(getzongjia2(json['jiageFrom'],json['jiageTo']));
  $(e).html(getlouceng2(json['lcengFrom'],json['lcengTo']));
  $(f).html(json['dname']+'-'+json['uname']);
}

function deleteClient(){
  doSearch(function(){
    if(houseData.page.data.length>rowId){
      $('#KY_TableListKy').find('tr')[rowId+1].click();
    }else {
      $('#KY_TableListKy').find('tr')[rowId].click();
    }
  },true);
}

function getmianji(a,b){
  if (a==0&&b==100000) {
    return '不限';
  }else if (a==0) {
    return '小于'+b;
  }else if (b==100000) {
    return '大于'+a;
  }else {
    return a+'-'+b;
  }
}

function getmianji2(a,b){
  if (a==0&&b==0) {
    return '不限';
  }else if (a==0||a==""||a==undefined) {
    return '小于'+b;
  }else if (b==0||b==""||b==undefined) {
    return '大于'+a;
  }else {
    return a+'-'+b;
  }
}

function getzongjia(a,b){
  if (a==0&&b==100000) {
    return '不限';
  }else if (a==0) {
    return b+'万以下';
  }else if (b==100000) {
    return a+'万以上';
  }else {
    return a+'万-'+b+'万';
  }
}

function getzongjia2(a,b){
  if (a==0&&b==0) {
    return '不限';
  }else if (a==0||a==""||a==undefined) {
    return b+'万以下';
  }else if (b==0||b==""||b==undefined) {
    return a+'万以上';
  }else {
    return a+'万-'+b+'万';
  }
}

function getlouceng(a,b){
  if (a==0&&b==100000) {
    return '不限';
  }else if (a==0) {
    return '高于'+b;
  }else if (b==100000) {
    return '低于'+a;
  }else {
    return a+'-'+b;
  }
}

function getlouceng2(a,b){
  if (a==0&&b==0) {
    return '不限';
  }else if (a==0||a==""||a==undefined) {
    return '小于'+b;
  }else if (b==0||b==""||b==undefined) {
    return '大于'+a;
  }else {
    return a+'-'+b;
  }
}

function getTel(tels){
  if (tels=='') {
    return '';
  }else{
    var a = tels.split(',');
    if (a.length==1) {
      return '<div class="KY-tel">'+a[0]+'</div>';
    }else{
      return '<div class="KY-tel">'+a[0]+'<span>+</span></div>';
    }
  }
}

function openAdd(){
  art.dialog.open("/client/client_add.jsp",{id:'add_client',width:800,height:578,title:'添加客源'});
}

function openDetail(id){
  art.dialog.open("/client/client_Detail.jsp?id="+id,{id:'client_Detail',width:800,height:490,title:'客源信息'});
}

function openEdit(id,row){
  // page
  rowId = row;
  art.dialog.open("/client/client_edit.jsp?id="+id,{id:'client_edit',width:800,height:575,title:'客源修改'});
}

function needHouse(id){
  YW.ajax({
    type: 'POST',
    url: '/c/client/hasMatchResult?cid='+id,
    mysuccess: function(data){
    var json = JSON.parse(data);
    if(json.count>0){
      art.dialog.open("/client/house.jsp?id="+id,{width:1100,height:600,title:'房配客'});
    }else {
      infoAlert('没有找到匹配的房源');
    }
    }
  });
}

function setSearchValue(index , click){
    var ThiA=$('#autoCompleteBox').find('a');
    ThiA.removeClass('hover');
    var Vals=ThiA.eq(index).addClass('hover').attr('area');
    $('#search').val(Vals);
    if(click){
      // houseAddMain();
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
    
      $("#search").css({
      "padding-left":pleft + 5,
      "width":wInput
      });
      
      $("#search").focus();

      });  
    }
    
}

$(document).ready(function() {
  ChangeW();
  Page.Init();
  autoComplete($('#search'));
  $("button.selectMethod").parent().hover(function(){
    $(this).siblings().find("div.ulErMenu").hide();
    $(this).find("div.ulErMenu").show();
  },function(){
    $(this).find("div.ulErMenu").hide(); 
  });
  $.get('/c/config/getQueryOptions', function(data) {
      queryOptions=JSON.parse(data);
      buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
      buildHtmlWithJsonArray("id_fxing",queryOptions['fxing'],true);
      buildHtmlWithJsonArray("id_zhuangxiu",queryOptions['zhuangxiu'],true);
      buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
      doSearch();
  });
  
});
</script>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<style type="text/css">
input:not([type="image"]), textarea {
    box-sizing: border-box;
}
  .aui_content{padding:10px 15px; font-size: 14px;}
</style>
</head>
<body>


<div class="KY_Main KY_W">
     <div class="MainRight">
          
          <div style="display:table; width:100%; height:100%; overflow:hidden;" class="not-select">
            <div id="menuTop" >
              <ul class="MainRightTop KY_W">
                  <li onclick="window.location='/client/index.jsp'" class="slect"><i class="i5"></i>求购</li>
                  <li class="line"></li>
                  <li onclick="openAdd()"><i class="i3"></i>登记</li>
                  <li class="line"></li>
              </ul>
            </div>
              <div class="MainRightInputMain KY_W not-select">
              
      <form class="form-horizontal form1" onsubmit="doSearch();return false;" role="form" name="form1">
                   <ul class="InputMainLine KY_W not-select" style="margin-bottom:8px;">
                        <li><input class="input-sm w110" type="text" placeholder="姓名" name="name"/></li>
                        <li><input class="input-sm w110" type="text" placeholder="电话" name="tel"/></li>
                        <li>
                            <input id="search" class="input-sm w110" type="text" placeholder="楼盘" autocomplete="off" name="area" />
                        </li>
                        
                        <li>
                            <button class="selectMethod hand not-select"  type="button">区域<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('quyus')" class="id_quyu"><label><input text="$[name]" class="hand" type="checkbox" name="quyus" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand not-select" type="button">楼型<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('lxing')" class="id_lxing"><label><input text="$[name]" class="hand" type="checkbox" name="lxing" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand not-select" type="button">房型<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('fxing')" class="id_fxing"><label><input text="$[name]" class="hand" type="checkbox" name="fxing" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand not-select" type="button">装潢<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('zxiu')" class="id_zhuangxiu"><label><input text="$[name]" class="hand" type="checkbox" name="zxiu" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <span class="SpanName not-select">业务员</span>
                            <select name="did" id="djrDid" class="input-sm input-rightB w102 dept_select" >
                            </select>
                            <select name="uid" id="djrId" class="input-sm input-left w102 user_select" >
                            </select>
                        </li>
                   </ul>
                   
                   <ul class="InputMainLine KY_W not-select">
                        <li>
                            <span class="SpanNameH2 not-select"><em>面积</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="面积" placeholder="" name="mjiStart" />
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="面积" placeholder="" name="mjiEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>总价</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="总价" placeholder="" name="zjiaStart" />
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="总价" placeholder="" name="zjiaEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>单价</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="单价" placeholder="" name="djiaStart" />
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="单价" placeholder="" name="djiaEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>楼层</em></span>
                            <div class="DivBoxW1 Fleft" style="width: 83px;">
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="楼层" placeholder="" name="lcengStart" />
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="楼层" placeholder="" name="lcengEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>建筑<br />年代</em></span>
                            <div class="DivBoxW1 Fleft" style="width: 83px;">
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="年代" placeholder="" name="yearFrom" />
                            <input id="search" class="input-sm input-left inputBox" type="text" desc="年代" placeholder="" name="yearTo" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>登记<br />日期</em></span>
                            <div class="DivBoxW1 Fleft">
        <input type="text" class="form-control input-sm input-left" name="dateStart" id="idTimes" onFocus="var timeend=$dp.$('idTime');WdatePicker({lang:'zh-cn',onpicked:function(){idTime.focus();},maxDate:'#F{$dp.$D(\'idTime\')}'})" style="width:70px">
        <input type="text" class="form-control input-sm input-left" name="dateEnd" id="idTime" onFocus="WdatePicker({lang:'zh-cn',minDate:'#F{$dp.$D(\'idTimes\')}'})" style="width:70px">
                            </div>
                        </li>
                        
                        <li class="LiBoxW1">
                             <button class="ButtonW1 ButtonS hand not-select" type="submit" onclick="doSearch();return false;" value="enter">搜索</button>
                             <button class="ButtonW1 ButtonQ hand not-select" type="button" onclick="window.location.reload();">清空</button>
                        </li>
                   </ul>
              </form>
              </div>
              <div style="width:100%; float:left; border-top:1px solid #f2f2f2;">
                           
                               <div  class="MR_ConTit KY_W" style=" position:inherit; margin:0 0.5%; width:99%; margin-top:5px; float:left; border:1px solid #d1d1d1;">
                                    <ul id="query_str" style="">
                                        <li class="tit">筛选条件：</li>
                                        <span>
                                        </span>
                                    </ul>
                               </div>
                              
                               
              </div>
               
               
              <div class="KY_W" style="display:table-row;overflow:hidden;">            
                   <div style="margin:0 0.5%; width:99%; height:31px; margin-bottom:-1px; border-bottom:1px solid #dddddd; border-left:1px solid #d1d1d1; float:left; border-right:1px solid #d1d1d1; background-color:#e9e9e9; overflow-y:scroll;">
                           
                                   <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain" style=" margin-bottom:-1px;">
                                    <tr>
                                            <th width="60">客户姓名</th>
                                            <th width="160">客户电话</th>
                                            <!--<th style="width:200px; min-width:50px;">楼盘名称</th>-->
                                            <!-- <th width="50">楼型</th> -->
                                            <!-- <th width="60">户型</th> -->
                                            <th width="50">面积(㎡)</th>
                                            <th width="50">总价</th>
                                            <!-- <th width="50">单价</th> -->
                                            <th width="50">楼层(层)</th>
                                            <!-- <th width="50">装潢</th> -->
                                            <th width="80">业务员</th>
                                            <th width="70">登记时间</th>
                                            <th width="120">操作</th>
                                            
                                     </tr>
                                          
                                    </table>
                           
                    </div>   
              </div>      
            
              <div class="MainRightConMain KY_W" style="display:table-row; position:relative; cursor:default;">
                <div class="KY_MainRCon" style="height:100%; margin:0 0.5%; width:99%; display:table-cell; border-bottom:none; border-top:none; overflow:hidden; overflow-y:scroll;">
                  <table id="KY_TableListKy" border="0" cellspacing="0" cellpadding="0" class="KY_TableMain table-hover">
                    <tr data-cid="$[id]" style="display:none;" class="id_client_list"  >
                      <td width="60"><strong>$[name]</strong></td>
                      <td width="160" runscript = "true">getTel('$[tels]')</td>
                      <!--<td style="width:200px; min-width:50px;" align="left"><div style="padding:0 8px;">$[areas]</div></td>-->
                      <!-- <td width="50">$[lxings]</td> -->
                      <!-- <td width="60">$[hxings]</td> -->
                      <td width="50" runscript="true">getmianji($[mjiFrom],$[mjiTo])</td>
                      <td width="50" runscript="true">getzongjia($[jiageFrom],$[jiageTo])</td>
                      <!-- <td width="50">7419</td> -->
                      <td width="50" runscript="true">getlouceng($[lcengFrom],$[lcengTo])</td>
                      <!-- <td width="50">$[zxius]</td> -->
                      <td width="80">$[dname]-$[uname]</td>
                      <td width="70">$[addtime]</td>
                      <td width="120" style="text-align:left"> 
                        <a style="padding-left:35%" href="##" onclick="needHouse($[id]);">客配房</a>
                        <a href="###" onclick="openDetail($[id])">查看</a>
                       <a href="###" onclick="openEdit($[id],$[rowIndex])">编辑</a>
                    </tr>
                  </table>
                </div>
              </div>  
              
              
              <div style="display:table-row; height:33px;">
                                
                   <div class="divPage"  style=" width:99%; margin:0 0.5%;">
                   
                        <div class="pageMain footer">
                          <div class="maxHW mainCont ymx_page foot_page_box"></div>
                        </div>
                   
                   </div>              
                                
              </div>
              
               
           </div>
     </div>


</div>

</body>

</html>
