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
<jsp:include page="../inc/top.jsp" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>房源</title>
<c:if test="${useLocalResource!=1}">
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<link rel="stylesheet" type="text/css" href="/style/font/iconfont.css" />
<link href="/style/animate.min.css" rel="stylesheet" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" src="/js/pagination.js"></script>
<script type="text/javascript" src="/js/DatePicker/WdatePicker.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
</c:if>
<c:if test="${useLocalResource==1}">
<link rel="stylesheet" type="text/css" href="file:///resources/style/css_ky.css" />
<link rel="stylesheet" type="text/css" href="file:///resources/style/font/iconfont.css" />
<link href="file:///resources/style/animate.min.css" rel="stylesheet" />
<script type="text/javascript" src="file:///resources/js/jquery.js"></script>
<script type="text/javascript" src="file:///resources/js/buildHtml.js"></script>
<script type="text/javascript" src="file:///resources/js/pagination.js"></script>
<script type="text/javascript" src="file:///resources/js/DatePicker/WdatePicker.js"></script>
<script src="file:///resources/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="file:///resources/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.j.tool.v2.js" type="text/javascript"></script>
</c:if>
<script type="text/javascript">
var houseData;
var houseGJbox;
var searching=false;
var formHtml=[];
var formPos=-1;
var chuzu=getParam('chuzu');
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
    url: '/c/house/listAll',
    data:a,
    mysuccess: function(data){
        houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        
        if(callback){
          callback();
        }
        //滚动到最上
        $('#KY_TableMain').parent().scrollTop(-2000);
    },
    complete:function(){
      searching=false;
      $('#searchBtn').removeAttr('disabled');
    }
  });
}

function doSearchAndSelectFirst(notFresh){
  //saveQuery();
  doSearch(function(){
    if(houseData.page.data.length>0){
      $('.TableB').find('tr')[1].click();
    }
  });
  if(notFresh==false || notFresh==undefined){
    formPos = formHtml.length-1;
  }
}

function saveQuery(){
  $('form input').each(function(index,obj){
    if(obj.type=='text' || obj.type=='hidden'){
      $(obj).attr('value',$(obj).val());
    }else if(obj.type=='checkbox' || obj.type=='radio'){
      if(obj.checked){
        $(obj).attr('checked','checked');
      }else{
        $(obj).removeAttr('checked');
      }
    }
  });
  formHtml.push($('form').html());
  // console.log($('form').html());
}

function goAhread(){
  if(formPos>=formHtml.length-1){
    return;
  }
  formPos++;
  $('form').html(formHtml[formPos]);
  doSearchAndSelectFirst(true);
  
}

function goBack(){
  if(formPos<=0 || formHtml.length<1){
    return;
  }
  formPos--;
  $('form').html(formHtml[formPos]);
  doSearchAndSelectFirst(true);
}

function getShenHeText(lock){
  if(lock==1){
    return "审";
  }else{
    return "未";
  }
}

function showAction(hcid,cid , seeGX){
  if(cid==1){
    return true;
  }else if(hcid==cid){
    if(seeGX==1){
      return false;
    }else{
      return true;
    }
  }else{
    return false;
  }
}

function shenheFy(id , obj){
  // event.cancelBubble=true;
  YW.ajax({
    type: 'POST',
    url: '/c/house/toggleShenHe?id='+id,
    mysuccess: function(data){
        var json = JSON.parse(data);
        if(json.sh==1){
          $(obj).text('审');
          $(obj).css('color','green');
        }else{
          $(obj).text('未');
          $(obj).css('color','red');
        }
    }
  });
}

function houseEdit(id){
  // event.cancelBubble=true;
  art.dialog.open("/house/house_edit.jsp?id="+id,{id:'seemap'})
}

function deletehouse(id){
  event.cancelBubble=true;
  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
  YW.ajax({
    type: 'POST',
    url: '/c/house/physicalDelete?id='+id,
    mysuccess: function(data){
      $(event.srcElement).attr('action','page');
      doSearchAndSelectFirst();
      alert('删除成功');
    }
  });
  },function(){},'warning');
}

function getSider(id){
    if(id){
        $('#sideCont').attr('src','/house/houseSee_v2.jsp?id='+id+'&chuzu='+chuzu);
    }
}

function setMapSearch(lngStart , lngEnd , latStart , latEnd){
	$('#lngStart').val(lngStart);
	$('#lngEnd').val(lngEnd);
	$('#latStart').val(latStart);
	$('#latEnd').val(latEnd);
	
	var str = '经度:'+lngStart+'-'+lngEnd+',维度:'+latStart+'-'+latEnd;
	//设置查询条件提示 
	if($('#mapSearch').length==0){
		var xx = '<li id="mapSearch" name=map>地图</li>';
	    $('#query_str').find('span').append(xx);	
	}
	doSearchAndSelectFirst(true);
}

function openMapSearch(xuequ){
	var lngStart = $('#lngStart').val();
	var lngEnd = $('#lngEnd').val();
	var latStart = $('#latStart').val();
	var latEnd = $('#latEnd').val();
	art.dialog.open('/house/mapSearch.jsp?xuequ='+xuequ+'&latStart='+latStart+'&latEnd='+latEnd+'&lngStart='+lngStart+'&lngEnd='+lngEnd,{id:'mapSearch',title:'地图框选找房',width:1200,height:550});
}

function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a');
    ThiA.removeClass('hover');
    var Vals=ThiA.eq(index).addClass('hover').attr('area');
    $('#search').val(Vals);
}

function updateHouse(id,data){
  getSider(id);
  var json = JSON.parse(data);
  var tr = $('tr[data-hid='+json['house']['id']+']');
  var a = tr.children()[2]
  var b = tr.children()[3]
  var c = tr.children()[4]
  var d = tr.children()[5]
  var e = tr.children()[6]
  var f = tr.children()[7]
  var g = tr.children()[8]
  var h = tr.children()[9]
  var i = tr.children()[10]
  var j = tr.children()[11]
  var k = tr.children()[12]
  $(a).html(json['house']['ztai']);
  $(b).html(json['house']['quyu']);
  $(c).html(json['house']['area']+' '+json['house']['dhao']+'-'+json['house']['fhao']);
  $(d).html(json['house']['lxing']);
  $(e).html(json['house']['hxf']+'-'+json['house']['hxt']+'-'+json['house']['hxw']);
  $(f).html(json['house']['mji']);
  $(g).html(json['house']['zjia']);
  $(h).html(json['house']['djia']);
  $(i).html(json['house']['lceng']+'/'+json['house']['zceng']);
  $(j).html(json['house']['zxiu']);
  $(k).html(json['house']['dateadd']);
}

    $(document).on('click', '.adboxs', function(event) {
        var Thi=$(this),
        ThiImg=Thi.find('img');
            ThiImg.removeClass('fadeInDown').addClass('fadeOutUp');
        var tms=setTimeout(function(){
            Thi.hide();
        },700);
        event.preventDefault();
    });

$(document).ready(function(){
	initTopMenu();
	autoComplete($('#search'));
	
  ChangeW();
  
    Page.Init();
//     $.get('/c/config/getQueryOptions', function(data) {
//       queryOptions=JSON.parse(data);
//       buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
//       buildHtmlWithJsonArray("id_fxing",queryOptions['fxing'],true);
//       buildHtmlWithJsonArray("id_zhuangxiu",queryOptions['zhuangxiu'],true);
//       buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
//       buildHtmlWithJsonArray("id_zhuangtai",queryOptions['ztai_sell'],true, true);
//       doSearchAndSelectFirst();
//     });
    buildHtmlWithJsonArray("id_lxing",${queryOptions.lxing},true);
    buildHtmlWithJsonArray("id_fxing", ${queryOptions.fxing},true);
    buildHtmlWithJsonArray("id_zhuangxiu",${queryOptions.zhuangxiu} ,true);
    buildHtmlWithJsonArray("id_quyu", ${queryOptions.quyu},true);
    buildHtmlWithJsonArray("id_zhuangtai", ${queryOptions.ztai_sell},true, true);
    doSearchAndSelectFirst();
    
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
    if(!window.top.hasShowAds){
      //if($${seeAds}){
//         if(window.screen.height<768){
//           art.dialog.open("/ad/leshi.html",{id:'ads',width:406,height:406,title:'',lock:true,padding:0,top:50});   
//         }else{
//           art.dialog.open("/ad/leshi.html",{id:'ads',width:406,height:406,title:'',lock:true,padding:0}); 
//         }
        $('#ad_container').show();
        window.top.hasShowAds=true;
      //}
    }
	 
	$(document).bind("contextmenu",function(e){
			  return false;
	});
	
	 
	// $(document).bind("mousedown",function(e){
	// 	if(e.which == 3){
 //  		$("#goAhread").css('display','');
 //  		$("#goBack").css('display','');
 //      if(formPos<=0){
 //        $("#goBack").css('display','none');
 //      }
 //      if(formPos<0 || formPos>=formHtml.length-1){
 //        $("#goAhread").css('display','none');
 //      }
 //      $("#GoAndBack").css({"display":"block","top":e.pageY+5,"left":e.pageX+5});
	// 	}
		
	// });
	
 //  $(document).bind("mouseup",function(e){
 //    if(e.which == 1){
 //      $("#GoAndBack").hide();
 //    }
    
 //  });
		 
});

function showAds(img){
	if(img){
		$('#ad_container img').attr('src',img);
	}
	$('#ad_container img').removeClass('fadeOutUp').addClass('fadeInDown');
	$('#ad_container').show();
}

function openGongGao(){
	art.dialog.open("/ad/gonggao.html",{id:'gonggao',width:960,height:615,title:'',lock:true,padding:0}); 
}			 

function setPageOrder(col){
	var order = $('#'+col +'Order').attr('next');
	$('#orderBy').val(col);
	$('#order').val(order);
	$('.order').text('');
	if(order=='desc'){
		//↑↓
		$('#'+col +'Order').text('↓');
		$('#'+col +'Order').attr('next' , 'asc');
	}else{
		$('#'+col +'Order').text('↑');
		$('#'+col +'Order').attr('next' , 'desc');
	}
	$('#searchBtn').click();
}

function onSetDateStart(){
	var timeend=$dp.$('idTime');
	var wp =WdatePicker({lang:'zh-cn',
		startDate:'%y-%M-%d 00:00:00',
		dateFmt:'yyyy-MM-dd',
		onpicked:function(){
			idTime.focus();
			},
		maxDate:'#F{$dp.$D(\'idTime\')}'
		});
}

function onSetDataEnd(){
	var wp2 = WdatePicker({lang:'zh-cn',startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'idTimes\')}'});
}
</script>
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

.adboxs { position: absolute; top: 0; right: 0; bottom: 0; left: 0; text-align: center;}
.adboxs .adboxitem{ margin:8% auto 0;}
</style>
</head>

<body>

<div id="GoAndBack" class="GaB">

     <a href="#" id="goAhread" disabled=true onclick="goAhread();"><span>前进</span></a>
     <a href="#" id="goBack" onclick="goBack();"><span>后退</span></a>

</div>

<div class="KY_Main KY_W">

     
     <div class="MainRight">
          
          <div  style="display:table; width:100%; height:100%; overflow:hidden;" class="not-select">
            <div id="menuTop" style="display:inline-block;">
              <ul class="MainRightTop KY_W titlebar">
                  <li class="slect nobar" onclick="window.location='/house/house_v2.jsp'"><i class="i1"></i>出售</li>
                  <li class="line"></li>
                  <li class="nobar" onclick="window.location='/house/house_rent_v2.jsp'"><i class="i2"></i>出租</li>
                  <li class="line"></li>
                  <li class="MenuBox nobar" style="position:relative;" onclick="openAddHouse('/v/house/house_add.html?act=add&chuzu=0')">
                       <i class="i3" ></i>登记
                       <div class="topMenuChid">
                            <span></span>
                            <a href="javascript:void(0)" onclick="openAddHouse('/house/house_add.jsp?act=add&chuzu=0')">出售登记</a> 
                            <a href="javascript:void(0)" onclick="openAddHouse('/v/house/house_rent_add.html?act=add&chuzu=1')">出租登记</a> 
                       </div>
                  </li>
                  <li class="line"></li>
                  <li class="MenuBox nobar" style="position:relative;">
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
              
              <div class="MainRightInputMain KY_W not-select" style="margin-bottom:5px;">
                <form class="form-horizontal form1" onsubmit="doSearchAndSelectFirst();return false;" role="form" name="form1">
                    <input type="hidden" id="sh" name="sh" value="${fy_sh }">
                    <input type="hidden" name="order" id="order" />
                    <input type="hidden" name="orderBy" id="orderBy" />
                    <input type="hidden" name="latStart" id="latStart"  value=""/>
                    <input type="hidden" name="latEnd" id="latEnd"  value=""/>
                    <input type="hidden" name="lngStart" id="lngStart" />
                    <input type="hidden" name="lngEnd" id="lngEnd" />
                   <ul class="InputMainLine KY_W not-select" style=" margin-bottom:8px;">
                   
                        <li><input class="input-sm w110" type="text" placeholder="电话" id="tel" name="tel" /></li>
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
                            <button class="selectMethod not-select"   type="button"  style="background-color:#6CEBC5">地图<i class="caret"></i></button>
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
                   
                        <li style="width:122px;"><input class="input-sm w110" type="text" placeholder="编号" name="id"  desc="编号"/>
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
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="单价" autocomplete="off" name="djiaStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="单价" autocomplete="off" name="djiaEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>楼层</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="楼层" autocomplete="off" name="lcengStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" desc="楼层" autocomplete="off" name="lcengEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>建筑<br />年代</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input class="input-sm input-left inputBox" type="text" placeholder="" autocomplete="off" name="yearStart" />
                            <input class="input-sm input-left inputBox" type="text" placeholder="" autocomplete="off" name="yearEnd" />
                            </div>
                        </li>
                        <li>
                            <span class="SpanNameH2 not-select"><em>发布<br />日期</em></span>
                            <div class="DivBoxW1 Fleft">
                            <input type="text" class="form-control input-left input-sm" name="dateStart" id="idTimes" onFocus="onSetDateStart();" style="width:70px">
        <input type="text" class="form-control input-left input-sm" name="dateEnd" id="idTime" onFocus="onSetDataEnd();" style="width:70px">
                            </div>
                        </li>
                        
                        <li class="LiBoxW1">
                             <button id="searchBtn" class="ButtonW1 ButtonS hand not-select" type="submit">搜索</button>
                             <button class="ButtonW1 ButtonQ hand not-select" type="button" onclick="window.location.reload();">清空</button>
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
                                                    </tr>
                                              </table>
                                             
                                     
                                      </div>
                                  
                                  </div>
                                    
                                 <div style="display:table-row;" >           

                                            <div class="FY_RCon" style=" width:100%; display:table-cell;">
                                                <div style="height:100%; float:left; overflow:hidden; overflow-y:auto;">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain TableB table-hover" id="KY_TableMain">
                                                        <tr data-hid="$[id]" style="display:none;" class="id_House_list" >
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
                                                          <td width="50" class="ztai_$[ztai]">$[ztai]</td>
                                                          <td width="60">$[quyu]</td>
                                                          <td class="br_area" style=" width:200px; min-width:50px;" align="left"><div style="padding:0 8px;">$[area] <span show="$[seeFH]==1">$[dhao]-$[fhao]</span></div></td>
                                                          <td width="50">$[lxing]</td>
                                                          <td width="60">$[hxf]-$[hxt]-$[hxw]</td>
                                                          <td width="50">$[mji]</td>
                                                          <td width="50">$[zjia]</td>
                                                          <td width="50" class="cs">$[djia]</td>
                                                          <td width="50">$[lceng]/$[zceng]</td>
                                                          <td width="50">$[zxiu]</td>
                                                          <td width="90" class="sy" runscript="true" title="$[dateadd]" style="padding-right:7px;">'$[dateadd]'.split(' ')[0]</td>
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

<div id="ad_container" style="display:none" class="adboxs">
    <div class="adboxitem">
        <img src="/ad/img/zjb/baidu.png" alt="" class=" animated fadeInDown" >
    </div>
</div>
</body>
</html>
