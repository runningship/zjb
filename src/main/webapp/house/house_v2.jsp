<%@page import="org.bc.web.ModelAndView"%>
<%@page import="com.youwei.zjb.sys.ConfigService"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="com.youwei.zjb.entity.Role"%>
<%@page import="java.util.List"%>
<%@page import="com.youwei.zjb.user.UserHelper"%>
<%@page import="com.youwei.zjb.user.entity.Department"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="com.youwei.zjb.user.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
User u = ThreadSessionHelper.getUser();

Department comp = u.Company();
if(comp==null || comp.share!=1){
	request.setAttribute("seeAll", false);
	request.setAttribute("seeGX", false);
	request.setAttribute("seeComp", true);
}else{
	request.setAttribute("seeAll", true);
	request.setAttribute("seeGX", true);
	request.setAttribute("seeComp", false);
}
request.setAttribute("cid", comp.id);
if(UserHelper.hasAuthority(u, "fy_sh")){
	request.setAttribute("fy_sh", "");
}else{
	//没有审核权的用户只能看到审核通过对数据
	request.setAttribute("fy_sh", "1");
}

// if(u.cid!=1){
// 	List<Role> role = SimpDaoTool.getGlobalCommonDaoService().listByParams(Role.class, "from Role where title='管理员' and cid<>1 and cid=?", u.cid);
// 	if(role!=null && role.size()>0){
// 		request.setAttribute("seeAds", "0");
// 	}else{
// 		request.setAttribute("seeAds", "1");
// 	}
// }else{
// 	request.setAttribute("seeAds", "1");
// }
//request.setAttribute("cityPy", ThreadSessionHelper.getCityPinyin());
ConfigService cs = new ConfigService();
ModelAndView mv = cs.getQueryOptions();
request.setAttribute("queryOptions", mv.data);
%>
<jsp:include page="../inc/resource.jsp" />
<jsp:include page="../inc/top.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>房源</title>
<%-- <link rel="stylesheet" type="text/css" href="${refPrefix}/style/css_ky.css" /> --%>
<%-- <link rel="stylesheet" type="text/css" href="${refPrefix}/style/font/iconfont.css" /> --%>
<%-- <link href="${refPrefix}/style/animate.min.css" rel="stylesheet" /> --%>
<script type="text/javascript" src="${refPrefix}/js/jquery.js"></script>
<%-- <script type="text/javascript" src="${refPrefix}/js/buildHtml.js"></script> --%>
<%-- <script type="text/javascript" src="${refPrefix}/js/pagination.js"></script> --%>
<%-- <script type="text/javascript" src="${refPrefix}/js/DatePicker/WdatePicker.js"></script> --%>
<%-- <script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script> --%>
<%-- <script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script> --%>
<%-- <script src="${refPrefix}/js/jquery.j.tool.v2.js" type="text/javascript"></script> --%>
<script type="text/javascript">
loadCss('${refPrefix}/style/css_ky.css');
loadCss('${refPrefix}/style/font/iconfont.css');
loadCss('${refPrefix}/style/animate.min.css');

loadJs('${refPrefix}/js/buildHtml.js');
loadJs('${refPrefix}/js/pagination.js');
loadJs('${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s');
loadJs('${refPrefix}/js/dialog/plugins/iframeTools.source.js');
loadJs('${refPrefix}/js/DatePicker/WdatePicker.js');
loadJs('${refPrefix}/js/jquery.j.tool.v2.js');
loadJs('${refPrefix}/js/house/house_v2.js?16');
window.top.iframeChanged('iframe_house');
</script>
<script type="text/javascript" >
function buildQueryOptions(){
	buildHtmlWithJsonArray("id_lxing",${queryOptions.lxing},true);
    buildHtmlWithJsonArray("id_fxing", ${queryOptions.fxing},true);
    buildHtmlWithJsonArray("id_zhuangxiu",${queryOptions.zhuangxiu} ,true);
    buildHtmlWithJsonArray("id_quyu", ${queryOptions.quyu},true);
    buildHtmlWithJsonArray("id_zhuangtai", ${queryOptions.ztai_sell},true, true);
}
</script>
<!--     <script>

        (function($){

            $(window).load(function(){
$(".sideMainer").mCustomScrollbar({
        theme:"minimal"
    });

                

            });

        })(jQuery);

    </script> -->


<style type="text/css">
  .selected{
    background-color: blue;
  }

.order{margin-left:2px;}
.KY_TableMain td a.shenhe_0{color:red;}
.ztai_已售{color:red;}
.ztai_在售{color:blue;}
.ztai_停售{color:orange;}

.GaB{ position:absolute; width:100px; display:none; top:100px; left:100px; z-index:99999999; background-color:#ffffff;box-shadow: #666666 0px 0px 8px;}
.GaB a{ display:block; width:100%; float:left; padding:8px 0; color:#05684c; font-family:"宋体"; text-align:center; border-bottom:1px solid #cccccc; font-size:12px;}
.GaB a:hover{ background-color:#05684c; color:#ffffff;}
.GaB a span{ margin:0 15px;}

.adboxs { position: absolute; top: 0; right: 0; bottom: 0; left: 0; text-align: center; z-index: 9999999;}
.adboxs .adboxitem{ margin:8% auto 0;}

@media screen and (max-width:) {
    
}
.MainRightTop li{}
*{-webkit-app-region:drag;}

</style>
</head>

<body class=" titlebar">

<div id="GoAndBack" class="GaB">

     <a href="#" id="goAhread" disabled=true onclick="goAhread();"><span>前进</span></a>
     <a href="#" id="goBack" onclick="goBack();"><span>后退</span></a>

</div>

<div class="KY_Main KY_W">

     
     <div class="MainRight">
          
          <div  style="display:table; width:100%; height:100%; overflow:hidden;" class="not-select">
				<jsp:include page="menuTop.jsp?type=chushou" />
              <div class="MainRightInputMain KY_W not-select" style="margin-bottom:5px;">
                <form class="form-horizontal form1" onsubmit="doSearchAndSelectFirst();return false;" role="form" name="form1">
                    <input type="hidden" id="sh" name="sh" value="${fy_sh }">
                    <input type="hidden" name="order" id="order" />
                    <input type="hidden" name="orderBy" id="orderBy" />
                    <input type="hidden" name="latStart" id="latStart"  value=""/>
                    <input type="hidden" name="latEnd" id="latEnd"  value=""/>
                    <input type="hidden" name="lngStart" id="lngStart" />
                    <input type="hidden" name="lngEnd" id="lngEnd" />
                    <input type="hidden" name="pageSize" id="pageSize" value="25"/>
                   <ul class="InputMainLine KY_W not-select" style=" margin-bottom:8px;">
                   
                        <li><input class="input-sm w110" type="text" placeholder="编号" id="id" name="id" /></li>
                        <li>
                            <span class="SpanName not-select">楼盘</span>
                            <input id="search" class="input-sm input-left w208" type="text" placeholder="" autocomplete="off" name="area" />
                        </li>
                        <li>
                            <input class="input-sm input-right w45" type="text" placeholder="栋号" name="dhao" />
                            <span class="LRborNone SpanName not-select">-</span>
                            <input class="input-sm input-left w46" type="text" placeholder="房号" name="fhao" />
                        </li>
                        <li>
                            <button class="selectMethod hand not-select"  type="button">区域<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('quyus')" class="id_quyu"><label><input class="hand" text="$[name]" type="checkbox" name="quyus" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand not-select" type="button">楼型<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('lxing')" class="id_lxing"><label><input class="hand" text="$[name]" type="checkbox" name="lxing" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand not-select" type="button">房型<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('fxing')" class="id_fxing"><label><input class="hand" text="$[value]" type="checkbox" name="fxing" value="$[value]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod hand not-select" type="button">装潢<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('zxiu')" class="id_zhuangxiu"><label><input class="hand" text="$[name]" type="checkbox" name="zxiu" value="$[name]"> $[name]</label> </p> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod not-select zonghe"  type="button" >综合<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                            	<c:if test="${seeAll }">
                            		<p onclick="addQueryStr('scope')"  id="seeAll"><label><input type="radio" text="" name="scope" value="all" checked="checked" > 所有</label> </p>
                            	</c:if>
                                <p onclick="addQueryStr('scope')" id="seeComp"><label>
                                	<input type="radio" text="公司" name="scope" value="comp"  <c:if test="${seeComp }">   checked="checked" </c:if> >公司</label> 
                                </p>
                                <c:if test="${seeGX }"> 
                                	<p onclick="addQueryStr('scope')"  id="seeGX"><label><input type="radio" text="共享" name="scope" value="seeGX" > 共享</label> </p>
                                </c:if> 
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod not-select" type="button" >状态<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('ztai')"><label><input type="radio" name="ztai" value="" text="" checked="" > 所有</label> </p> 
                                <p onclick="addQueryStr('ztai')" class="id_zhuangtai"  ><label><input type="radio" text="$[name]" name="ztai" value="$[code]"> $[name]</p>
                            </div>
                        </li>
                        <li>
                            <button class="selectMethod not-select ditu downMore" type="button" >地图<i class="caret"></i></button>
                            <div class="ulErMenu"> 
                                <p ><label style="cursor:pointer" onclick="openMapSearch(0);"> 框 选</label></p>
                                <p><label style="cursor:pointer" onclick="openMapSearch(1)"> 学 区</label></p>
                            </div>
                        </li>
                        <c:if test="${authNames.contains('fy_sh') }">
                        <li>
                            <button class="selectMethod not-select" type="button" >审核<i class="caret"></i> </button>
                            <div class="ulErMenu"> 
                                <p onclick="addQueryStr('shehe')"><label><input text="" name="shehe" type="radio" value="" checked="checked" onclick="$('#sh').val(this.value);">全部</label> </p> 
                                <p onclick="addQueryStr('shehe')"><label><input text="已审" name="shehe" type="radio" value="1" onclick="$('#sh').val(this.value);">已审</label> </p> 
                                <p onclick="addQueryStr('shehe')"><label><input text="未审" name="shehe" type="radio" value="0" onclick="$('#sh').val(this.value);">未审</label> </p> 
                            </div>
                        </li>
                        </c:if>
                   </ul>
                   
                   <ul class="InputMainLine KY_W not-select">
                   
                        <li style="width:110px;"><input class="input-sm w110" type="text" placeholder="电话" name="tel"  id="tel" desc="编号"/>
                        <input class="input-sm w110" type="text" placeholder="路段" name="address" /></li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>面积</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="面积" autocomplete="off" name="mjiStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="面积" autocomplete="off" name="mjiEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>总价</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="总价" autocomplete="off" name="zjiaStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="总价" autocomplete="off" name="zjiaEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>单价</em></span>
                            <div class="DivBoxW1 Fleft" style="width: 61px;">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="单价" autocomplete="off" name="djiaStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="单价" autocomplete="off" name="djiaEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>楼层</em></span>
                            <div class="DivBoxW1 Fleft" style="width: 83px;">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="楼层" autocomplete="off" name="lcengStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="楼层" autocomplete="off" name="lcengEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>建筑<br />年代</em></span>
                            <div class="DivBoxW1 Fleft" style="width: 83px;">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" autocomplete="off" name="dateyearStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" autocomplete="off" name="dateyearEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>发布<br />日期</em></span>
                            <div class="DivBoxW1 Fleft" style="width: 80px;">
                            <input type="text" class="form-control input-left input-sm" name="dateStart" id="idTimes" onFocus="onSetDateStart();" style="width:100%">
                            <input type="text" class="form-control input-left input-sm" name="dateEnd" id="idTime" onFocus="onSetDataEnd();" style="width:100%">
                            </div>
                        </li>
                        
                        <li class="LiBoxW1">
                             <button id="searchBtn" class="ButtonW1 ButtonS hand not-select titlebar" type="submit">搜索</button>
                             <button class="ButtonW1 ButtonQ hand not-select titlebar" type="button" onclick="_open('/house/house_v2.jsp?type=chushou')">清空</button>
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
                                  <div class="MR_ConTit KY_W MR_ConTitW2 not-select" style=" display:table-cell; position:inherit;">
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
                                            	<c:if test="${authNames.contains('fy_batch_del') }"><th width="40"><input type="checkbox" onclick="selectAll(this);"/><a href="javascript:void(0)"  onclick="batchDeletehouse();";>删除</a></th></c:if>
                                              <th width="60">编号</th>
                                              <c:if test="${authNames.contains('fy_sh') || authNames.contains('fy_edit') || authNames.contains('fy_del') }">
                                              <th width="75">操作</th>
                                              </c:if>
                                              <th width="50">状态</th>
                                              <th width="60">区域</th>
                                              <th style=" width:200px; min-width:50px;">楼盘名称</th>
                                              <th width="50">楼型</th>
                                              <th width="60">户型</th>
                                              <th width="50"><a href="javascript:void(0)" onclick="setPageOrder('mji')">面积</a><span id="mjiOrder" next="asc" class="order"></span></th>
                                              <th width="50"><a href="javascript:void(0)" onclick="setPageOrder('zjia')">总价(万)</a><span id="zjiaOrder" next="asc" class="order"></span></th>
                                              <th width="50"><a href="javascript:void(0)" onclick="setPageOrder('djia')">单价</a><span id="djiaOrder" next="asc" class="order"></span></th>
                                              <th width="50">楼层</th>
                                              <th width="50">装潢</th>
                                              <th width="90" style="padding-right:7px;"><a href="javascript:void(0)" onclick="setPageOrder('dateadd')">发布时间</a><span id="dateaddOrder" next="asc" class="order">↓</span></th>
                                              <c:if test="${authNames.contains('fy_show_updatetime')}">
                                              <th width="90" style="padding-right:7px;"><a href="javascript:void(0)" onclick="setPageOrder('updatetime')">更新时间</a><span id="updatetimeOrder" next="asc" class="order"></span></th>
                                              </c:if>
                                            </tr>
                                        </table>
                                             
                                     
                                    </div>
                                  
                                  </div>
                                    
                                 <div id="contentTable" style="display:table-row;" >           

                                    <div class="FY_RCon" style=" width:100%; display:table-cell;">
                                        <div style="height:100%; float:left; overflow:hidden; overflow-y:auto;">
                                            <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain TableB table-hover" id="KY_TableMain">
                                                <tr data-hid="$[id]" style="display:none;" class="id_House_list" >
                                                	<c:if test="${authNames.contains('fy_batch_del') }"><td width="20"><input class="checkbox" type="checkbox"  data-id="$[id]" onclick="chooseHouse();" /></td></c:if>
                                                  <td width="60"><span class="piliang hidden"><input type="checkbox" name="ids" value="$[id]" style="display:none"> </span>$[id]</td>
                                                  <c:if test="${authNames.contains('fy_sh') || authNames.contains('fy_edit') || authNames.contains('fy_del') }">
                                                      <td  width="75" >
                                                      	<c:if test="${authNames.contains('fy_sh') }">
                                                        	<a href="##" show="showAction($[cid],${cid},$[seeGX])" class="shenhe_$[sh]" data-hid="$[id]" runscript="true" data-rel="del" onclick="shenheFy($[id],this)">getShenHeText($[sh])</a>
                                                        </c:if>
                                                        <c:if test="${authNames.contains('fy_edit') }">
                                                        	<a href="#" auth="fy_edit" show="showAction($[cid],${cid},$[seeGX])" class="edit" data-hid="$[id]" onclick="houseEdit($[id])" data-rel="edit">改</a>
                                                        </c:if>
                                                        <c:if test="${authNames.contains('fy_del') }">
                                                        	<a href="##" show="showAction($[cid],${cid},$[seeGX])" class="del" data-hid="$[id]" data-rel="del" onclick="deletehouse($[id])">删</a>
                                                        </c:if>
                                                      </td>
                                                  </c:if>
                                                  
                                                  <td width="50" class="d_ztai ztai_$[ztai]">$[ztai]</td>
                                                  <td width="60" class="d_quyu">$[quyu]</td>
                                                  <td class="d_area br_area" style=" width:200px; min-width:50px;" align="left"><div style="padding:0 8px;">$[area] <span show="$[seeFH]==1">$[dhao]-$[fhao]</span></div></td>
                                                  <td width="50" class="d_lxing">$[lxing]</td>
                                                  <td width="60" class="d_hxing">$[hxf]-$[hxt]-$[hxw]</td>
                                                  <td width="50" class="d_mji">$[mji]</td>
                                                  <td width="50"  runscript="true"  class="d_zjia">getZjia($[zjia])</td>
                                                  <td width="50" class="d_djia cs">$[djia]</td>
                                                  <td width="50" class="d_lceng">$[lceng]/$[zceng]</td>
                                                  <td width="50" class="d_zxiu">$[zxiu]</td>
                                                  <td width="90" class="d_adate sy" runscript="true" title="$[dateadd]" style="padding-right:7px;">'$[dateadd]'.split(' ')[0]</td>
                                                  <c:if test="${authNames.contains('fy_show_updatetime')}">
                                                  	<td width="90" class="d_udate sy" style="padding-right:7px;">$[updatetime]</td>
                                                  </c:if>
                                              </tr>
                                            </table>
                                        </div>
                                    </div>
                                  </div>

                                <div style="display:table-row; height:35px; overflow:hidden;">     
                                     <div class="divPage"  style=" height:35px; margin:0; background:none;">  
                                        <div class="pageMain footer">
                                          <div class="maxHW ymx_page foot_page_box"></div>
                                        </div>
                                     </div>     
                                </div>
                        </div>
                   </div>

              </div>
              
           </div>
     
     </div>
     
     
     

</div>

<!-- <div id="ad_container" style="display:none" class="adboxs"> -->
<!--     <div class="adboxitem"> -->
<!--         <img src="/ad/img/zjb/ad_what_we_do.jpg" alt="" class=" animated fadeInDown" > -->
<!--     </div> -->
<!-- </div> -->


</body>
</html>
