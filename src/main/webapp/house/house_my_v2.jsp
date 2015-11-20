<%@page import="org.bc.web.ModelAndView"%>
<%@page import="com.youwei.zjb.sys.ConfigService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
ConfigService cs = new ConfigService();
ModelAndView mv = cs.getQueryOptions();
request.setAttribute("queryOptions", mv.data);
%>
<jsp:include page="../inc/top.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的</title>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<link rel="stylesheet" type="text/css" href="/style/font/iconfont.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" src="/js/pagination.js"></script>
<script type="text/javascript" src="/js/DatePicker/WdatePicker.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript">
var houseData;
var houseGJbox;
var searching=false;
var chuzu=getParam('chuzus');
var flags=getParam('flag'),
URL_ajax_house_list;
if(flags=='favShou'){
    URL_ajax_house_list='/c/house/listMyFav'
}else if(flags=='addShou'){
    URL_ajax_house_list='/c/house/listMyAdd'
}else if(flags=='favZu'){
    URL_ajax_house_list='/c/house/rent/listMyFav'
}else if(flags=='addZu'){
    URL_ajax_house_list='/c/house/rent/listMyAdd'
}
function doSearch(callback){
  if(searching){
    return;
  }
  if(event==undefined || $(event.srcElement).attr('action')!='page'){
    $('.pageInput').val(1);
  }
  searching=true;
  $('#searchBtn').attr('disabled','disabled');
  var a=$('form[name=form1]').serialize();
  // pushQueryToHistory($('form[name=form1]').serializeArray());
  YW.ajax({
    type: 'POST',
    url: URL_ajax_house_list,
    data:a,
    mysuccess: function(data){
        houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        if(callback){
          callback();
        }
    },
    complete:function(){
      searching=false;
      $('#searchBtn').removeAttr('disabled');
    }
  });
}

function doSearchAndSelectFirst(){
  doSearch(function(){
    if(houseData.page.data.length>0){
      $('.TableB').find('tr')[1].click();
    }
  });
}

function getShenHeText(lock){
  if(lock==1){
    return "审";
  }else{
    return "未";
  }
}

function getSider(id){
    if(id){
      if(chuzu=='1'){
        $('#sideCont').attr('src','/house/rent/view.jsp?id='+id+'&chuzu='+chuzu);
      }else{
        $('#sideCont').attr('src','/house/houseSee_v2.jsp?id='+id+'&chuzu='+chuzu);
      }
    }
}

function getShenHeText(lock){
  if(lock==1){
    return "审";
  }else{
    return "未";
  }
}

function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a');
    ThiA.removeClass('hover');
    var Vals=ThiA.eq(index).addClass('hover').attr('area');
    $('#search').val(Vals);
}

function favDelete(houseId){
  if(flags=='favShou') {
    YW.ajax({
        type: 'POST',
        url: '/c/house/fav/deleteSell?houseId='+houseId,
        mysuccess: function(data){
          doSearchAndSelectFirst();
        alert('已取消收藏');
      },complete:function(){}
    });
  }else{
    YW.ajax({
        type: 'POST',
        url: '/c/house/fav/deleteRent?houseId='+houseId,
        mysuccess: function(data){
          doSearchAndSelectFirst();
        alert('已取消收藏');
      },complete:function(){}
    });
  }
}

$(document).ready(function(){
  ChangeW();
    initTopMenu();
    Page.Init();
    autoComplete($('#search'));
    if (flags=='favZu' ||flags=='addZu') {
      $('li.chushou').remove();
        $('td.chushou').remove();
        $('th.chushou').remove();
      if(flags=='addZu'){
        $('td.shc').remove();
        $('th.shc').remove();
      }
    }else {
      $('li.chuzu').remove();
        $('td.chuzu').remove();
        $('th.chuzu').remove();
      if(flags=='addShou'){
        $('td.shc').remove();
        $('th.shc').remove();
      }
    }
//     $.get('/c/config/getQueryOptions', function(data) {
//       queryOptions=JSON.parse(data);
//       buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
//       buildHtmlWithJsonArray("id_fxing",queryOptions['fxing'],true);
//       buildHtmlWithJsonArray("id_zhuangxiu",queryOptions['zhuangxiu'],true);
//       buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
//         if(flags=='favZu' || flags=='addZu'){
//             buildHtmlWithJsonArray("id_zhuangtai",queryOptions['ztai_rent'],true, true);
//             buildHtmlWithJsonArray("id_fangshi",queryOptions['rent_type'],true , true);
//         }else{
//             buildHtmlWithJsonArray("id_zhuangtai",queryOptions['ztai_sell'],true, true);
//         }
//       doSearchAndSelectFirst();
//       Page.Init();
//     });
    buildHtmlWithJsonArray("id_lxing",${queryOptions.lxing},true);
    buildHtmlWithJsonArray("id_fxing",${queryOptions.fxing} ,true);
    buildHtmlWithJsonArray("id_zhuangxiu",${queryOptions.zhuangxiu},true);
    buildHtmlWithJsonArray("id_quyu",${queryOptions.quyu},true);
      if(flags=='favZu' || flags=='addZu'){
          buildHtmlWithJsonArray("id_zhuangtai",${queryOptions.ztai_rent},true, true);
          buildHtmlWithJsonArray("id_fangshi",${queryOptions.rent_type},true , true);
      }else{
          buildHtmlWithJsonArray("id_zhuangtai",${queryOptions.ztai_sell},true, true);
      }
    doSearchAndSelectFirst();
    Page.Init();
    $("button.selectMethod").parent().hover(function(){
      $(this).siblings().find("div.ulErMenu").hide();
      $(this).find("div.ulErMenu").show();
    },function(){
         $(this).find("div.ulErMenu").hide(); 
    });
    $('.id_House_list').on('click', 'a', function(event) {
        var Thi=$(this),
        rel=Thi.data('rel'),
        this_hid=Thi.data('hid');
    });
    $('.TableB').on('click', 'tr', function(event) {
        var Thi=$(this),
        ThiHid=Thi.data('hid');
        if(houseGJbox)houseGJbox.close();
        getSider(ThiHid);
        // Thi.toggleClass('selected');
        return false;
    });
    // tableHover();
});

</script>
<style type="text/css">
  .selected{
    background-color: blue;
  }

.shenhe_0{color:red;}
.shenhe_1{color:green;}
.ztai_已售{color:red;}
.ztai_在售{color:blue;}
.ztai_停售{color:orange;}
</style>
</head>

<body>

<div class="KY_Main KY_W">

     
     <div class="MainRight">
          
          <div style="display:table; width:100%; height:100%; overflow:hidden;">
            <div id="menuTop" style="display:inline-block; width:100%;">
              <ul class="MainRightTop KY_W" onselectstart="return false;">
                  <li onclick="window.location='/house/house_v2.jsp'"><i class="i1"></i>出售</li>
                  <li class="line"></li>
                  <li onclick="window.location='/house/house_rent_v2.jsp'"><i class="i2"></i>出租</li>
                  <li class="line"></li>
                  <li class="MenuBox" style="position:relative;">
                       <i class="i3"></i>登记
                       <div class="topMenuChid">
                            <span></span>
                            <a href="javascript:void(0)" onclick="openAddHouse('/house/house_add.jsp?act=add&chuzu=0')">出售登记</a> 
                            <a href="javascript:void(0)" onclick="openAddHouse('/v/house/house_rent_add.html?act=add&chuzu=1')">出租登记</a> 
                       </div>
                  </li>
                  <li class="line"></li>
                  <li class="MenuBox slect" style="position:relative;">
                       <i class="i4"></i>我的
                       <div class="topMenuChid">
                            <span></span>
                            <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=favShou&chuzus=0'">我收藏的出售</a> 
                            <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=favZu&chuzus=1'">我收藏的出租</a>
                            <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=addShou&chuzus=0'">我发布的出售</a> 
                            <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=addZu&chuzus=1'">我发布的出租</a>  
                       </div>
                  </li>
                  <li class="line"></li>
              </ul>
              </div>
              <div class="MainRightInputMain KY_W" style="margin-bottom:5px;">
                <form class="form-horizontal form1" onsubmit="doSearch();return false;" role="form" name="form1">
                   <ul class="InputMainLine KY_W" style=" margin-bottom:8px;">
                   
                        <li><input class="input-sm w110" type="text" placeholder="电话" name="tel" /></li>
                        <li>
                            <span class="SpanName">楼盘</span>
                            <input id="search" class="input-sm input-left w208" type="text" placeholder="" autocomplete="off" name="area" />
                        </li>
                        <li class="chushou">
                            <input class="input-sm input-right w45" type="text" placeholder="栋号" name="dhao" />
                            <span class="LRborNone SpanName">-</span>
                            <input class="input-sm input-left w46" type="text" placeholder="房号" name="fhao" />
                        </li>
                        <li>
                            <button class="selectMethod hand"  type="button">区域<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('quyus')" class="id_quyu"><label><input class="hand" text="$[name]" type="checkbox" name="quyus" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand" type="button">楼型<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('lxing')" class="id_lxing"><label><input class="hand" text="$[name]" type="checkbox" name="lxing" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand" type="button">房型<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('fxing')" class="id_fxing"><label><input class="hand" text="$[name]" type="checkbox" name="fxing" value="$[value]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand" type="button">装潢<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('zxiu')" class="id_zhuangxiu"><label><input class="hand" text="$[name]" type="checkbox" name="zxiu" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li class="chuzu">
                            <button class="selectMethod" type="button">方式<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('fangshi')"><label><input type="radio" name="fangshi" text="" value="" checked="checked" > 不限</label> </p> 
                                <p onclick="addQueryStr('fangshi')" class="id_fangshi"  ><label><input text="$[name]" type="radio" name="fangshi" value="$[code]"> $[name]</p>
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod" type="button">综合<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('scope')"><label><input type="radio" text="" name="scope" value="all" checked="checked" > 所有</label> </p> 
                                <p onclick="addQueryStr('scope')"><label><input type="radio" text="公司" name="scope" value="comp" >公司</label> </p> 
                                <p onclick="addQueryStr('scope')"><label><input type="radio" text="共享" name="scope" value="seeGX" > 共享</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod" type="button" >状态<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('ztai')"><label><input type="radio" name="ztai" text="" value="" checked="checked"> 所有</label> </p> 
                                <p onclick="addQueryStr('ztai')" class="id_zhuangtai"  ><label><input text="$[name]" type="radio" name="ztai" value="$[code]"> $[name]</p>
                            </div>
                        </li>
                        <c:if test="${authNames.contains('fy_sh') }">
                        <li>
                            <button class="selectMethod" type="button" >审核<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('shehe')"><label><input text="" name="shehe" type="radio" value="" checked="checked" onclick="$('#sh').val(this.value);">全部</label> </p> 
                                <p onclick="addQueryStr('shehe')"><label><input text="已审" name="shehe" type="radio" value="1" onclick="$('#sh').val(this.value);">已审</label> </p> 
                                <p onclick="addQueryStr('shehe')"><label><input text="未审" name="shehe" type="radio" value="0" onclick="$('#sh').val(this.value);">未审</label> </p> 
                            </div>
                        </li>
                        </c:if>
                   </ul>
                   
                   <ul class="InputMainLine KY_W">
                   
                        <li style="width:122px;"><input class="input-sm w110" type="text" placeholder="编号" name="id"  desc="编号"/>
                        <input class="input-sm w110" type="text" placeholder="路段" name="address" /></li>
                        <li>
                            <span class="SpanNameH2"><em>面积</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="面积" autocomplete="off" name="mjiStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="面积" autocomplete="off" name="mjiEnd" />
                            </div>
                        </li>
                        <li class="chushou">
                            <span class="SpanNameH2"><em>总价</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="总价" autocomplete="off" name="zjiaStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="总价" autocomplete="off" name="zjiaEnd" />
                            </div>
                        </li>
                        <li class="chushou">
                            <span class="SpanNameH2"><em>单价</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="单价" autocomplete="off" name="djiaStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="单价" autocomplete="off" name="djiaEnd" />
                            </div>
                        </li>
                        <li class="chuzu">
                            <span class="SpanNameH2"><em>租金</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="租金" autocomplete="off" name="zjiaStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="租金" autocomplete="off" name="zjiaEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2"><em>楼层</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="楼层" autocomplete="off" name="lcengStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="楼层" autocomplete="off" name="lcengEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2"><em>建筑<br />年代</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" autocomplete="off" name="yearStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" autocomplete="off" name="yearEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2"><em>发布<br />日期</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input type="text" class="form-control input-sm" name="dateStart" id="idTimes" onFocus="var timeend=$dp.$('idTime');WdatePicker({lang:'zh-cn',onpicked:function(){idTime.focus();},maxDate:'#F{$dp.$D(\'idTime\')}'})" style="width:78px">
        <input type="text" class="form-control input-sm" name="dateEnd" id="idTime" onFocus="WdatePicker({lang:'zh-cn',minDate:'#F{$dp.$D(\'idTimes\')}'})" style="width:78px">
                            </div>
                        </li>
                        
                        <li class="LiBoxW1">
                             <button id="searchBtn" class="ButtonW1 ButtonS hand" type="submit">搜索</button>
                             <button class="ButtonW1 ButtonQ hand" type="button">清空</button>
                        </li>
                        
                   </ul>
                   
                </form>
              </div>
              
              
              
              <div class="MainRightConMain KY_W" style="display: table-row; overflow:hidden;">

                <div class="MainRightConL" style=" height:100%; display:table-cell; overflow:hidden; overflow-y:auto;margin-top:0px;">
                  <!-- <div class="sideCont" id="sideCont"></div> -->
                    <iframe id="sideCont" src="" style="width:100%;height:100%;border:none"></iframe>
                </div>

                <div class="MainRightConR" id="FY_RCon" style="margin:0px;overflow:hidden; overflow-y:hidden; display:table-cell; float:left;">
                  <div style="width:100%; display:table; height:100%;">
                    <div style="display:table-row;">
                      <div class="MR_ConTit KY_W MR_ConTitW2" style=" display:table-cell; position:inherit;">
                        <ul id="query_str">
                          <li class="tit">筛选条件：</li>
                          <span></span>
                        </ul>
                      </div>
                    </div>
                  <div style="display:table-row;">  
                    <div style=" width:100%; display:table-cell;">
                      <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain" id="FY_TableTit">
                        <tr>
                          <th width="60">编号</th>
                          <th class="shc" width="75">操作</th>
                          <th width="50">状态</th>
                          <th width="50">审核</th>
                          <th width="60">区域</th>
                          <th style=" width:200px; min-width:50px;">楼盘名称</th>
                          <th width="50">楼型</th>
                          <th width="60">户型</th>
                          <th width="50">面积</th>
                          <th width="50" class="chuzu">租金</th>
                          <th width="50" class="chushou">总价(万)</th>
                          <th width="50" class="chushou">单价</th>
                          <th width="50">楼层</th>
                          <th width="50">装潢</th>
                          <th width="90" class="sy">发布时间</th>
                        </tr>
                      </table>
                    </div>
                  </div>
                  <div style="display:table-row;">           
                    <div class="FY_RCon" style=" width:100%; display:table-cell;">
                      <div style="height:100%; float:left; overflow:hidden; overflow-y:auto;">
                        <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain TableB table-hover" id="KY_TableMain">
                          <tr data-hid="$[id]" style="display:none;" class="id_House_list" >
                            <td width="60"><span class="piliang hidden"><input type="checkbox" name="ids" value="$[id]" style="display:none"> </span>$[id]</td>
                            <td  width="75" class="shc">
                              <a href="javascript.void(0)" class="delete" data-hid="$[id]" data-rel="del" onclick="favDelete($[id]);">取消收藏</a></td>
                            <td width="50" class="ztai_$[ztai]">$[ztai]</td>
                            <td width="50" runscript="true" class="shenhe_$[sh]">getShenHeText($[sh])</td>
                            <td width="60">$[quyu]</td>
                            <!-- <td class="br_area">$[area] <span show="$[seeFH]==1">$[dhao]-$[fhao]</span></td> -->
                            <td class="br_area" style=" width:200px; min-width:50px;" align="left"><div style="padding:0 8px;">$[area] <span show="$[seeFH]==1">$[dhao]-$[fhao]</span></div></td>
                            <td width="50">$[lxing]</td>
                            <td width="60">$[hxf]-$[hxt]-$[hxw]</td>
                            <td width="50">$[mji]</td>
                            <td width="50">$[zjia]</td>
                            <td width="50" class="chushou">$[djia]</td>
                            <td width="50">$[lceng]/$[zceng]</td>
                            <td width="50">$[zxiu]</td>
                            <td width="90" class="sy" runscript="true" title="$[dateadd]">'$[dateadd]'.split(' ')[0]</td>
                          </tr>
                        </table>
                       </div>
                      </div>
                    </div>

                                <div style="display:table-row; height:35px; overflow:hidden;">     
                                     <div class="divPage"  style=" height:35px; margin:0; background:none;">  
                                        <div class="pageMain footer">
                                          <div class="maxHW mainCont ymx_page foot_page_box"></div>
                                        </div>
                                     </div>     
                                </div>
                        </div>
                   </div>

              </div>
              
           </div>
     
     </div>
     
     
     

</div>

</body>
</html>
