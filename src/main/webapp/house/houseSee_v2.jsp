<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.youwei.zjb.house.entity.Agent"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.youwei.zjb.user.entity.Department"%>
<%@page import="java.util.Date"%>
<%@page import="com.youwei.zjb.user.entity.ViewHouseLog"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="com.youwei.zjb.house.SellState"%>
<%@page import="com.youwei.zjb.user.entity.User"%>
<%@page import="org.bc.sdak.utils.JSONHelper"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.youwei.zjb.house.entity.House"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="../inc/resource.jsp" />
<jsp:include page="../inc/top.jsp" />
<%
CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
String id = request.getParameter("id");
House h = dao.get(House.class, Integer.valueOf(id));
if(h==null){
	out.write("404");
	return ;
}
//mji zjia
String hql="select tt.conts as conts , d.dname , tt.uname , tt.addtime tt.ztai as ztai from (select gj.ztai as ztai, gj.id as id,gj.hid as houseId,gj.conts as conts,gj.did as did,u.uname as uname,"
					+"gj.addtime as addtime,gj.sh as sh,gj.chuzu as chuzu  from house_gj gj ,uc_user u "
					+" where gj.hid = ? and u.id=gj.uid  and gj.chuzu=?) tt "
					+" left join (select d.id as did, d.namea as dname, c.namea as cname from uc_comp c, uc_comp d where d.fid=c.id) d on d.did=tt.did order by tt.addtime desc";
			
List<Map> gjList = dao.listSqlAsMap(hql.toString(), Integer.valueOf(id) , 0);
request.setAttribute("house" , h);
request.setAttribute("gjList" , gjList);
User user = dao.get(User.class, h.uid);
Department dept = dao.get(Department.class, h.did);
request.setAttribute("fbr", (user==null || user.uname==null) ? "":user.uname);
request.setAttribute("dname", dept==null? "房主" : dept.namea);
SellState ztai = SellState.parse(h.ztai);
request.setAttribute("ztai", ztai==null ? "": ztai);
String favStr = "@"+ThreadSessionHelper.getUser().id+"|";
if(h.fav!=null && h.fav.contains(favStr)){
	request.setAttribute("fav", "1");
}else{
	request.setAttribute("fav", "0");
}
String tel =h.tel;
if(StringUtils.isNotEmpty(tel)){
	request.setAttribute("tel", tel);
	JSONObject agents = new JSONObject();
	JSONObject labelCounts = new JSONObject();
	for(String tmp : tel.split("/")){
		if(StringUtils.isEmpty(tmp)){
			continue;
		}
		Agent agent = dao.getUniqueByParams(Agent.class, new String[]{"tel" , "labelUid"}, new Object[]{tmp , ThreadSessionHelper.getUser().id});
		if(agent!=null){
			agents.put(tmp, "agent");
		}else{
			agents.put(tmp, "fangzhu");
		}
		long count = dao.countHql("select count(*) from Agent where tel=?", tmp);
		labelCounts.put(tmp, count);
	}
	if(agents.isEmpty()){
		request.setAttribute("agents", "{}");
	}else{
		request.setAttribute("agents", agents);	
	}
	if(labelCounts.isEmpty()){
		request.setAttribute("labelCounts", "{}");
	}else{
		request.setAttribute("labelCounts", labelCounts);	
	}
}else{
	request.setAttribute("tel", h.telImg);
}
ViewHouseLog vl = new ViewHouseLog();
vl.hid = h.id;
vl.uid = ThreadSessionHelper.getUser().id;
vl.isMobile = 0;
vl.viewTime = new Date();
dao.saveOrUpdate(vl);
%>

<!DOCTYPE html>
<html>
<head>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<%-- <link href="${refPrefix}/style/css.css" rel="stylesheet"> --%>
<%-- <link href="${refPrefix}/bootstrap/css/bootstrap.css" rel="stylesheet"> --%>
<%-- <link href="${refPrefix}/style/style.css" rel="stylesheet"> --%>
<%-- <link href="${refPrefix}/style/css_ky.css" rel="stylesheet"> --%>
<script src="${refPrefix}/js/jquery.js" type="text/javascript"></script>
<%-- <script src="${refPrefix}/bootstrap/js/bootstrap.js" type="text/javascript"></script> --%>
<%-- <script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script> --%>
<%-- <script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script> --%>
<%-- <script src="${refPrefix}/js/jquery.input.js" type="text/javascript"></script> --%>
<%-- <script src="${refPrefix}/js/jquery.j.tool.js?2" type="text/javascript"></script> --%>
<%-- <script type="text/javascript" src="${refPrefix}/js/buildHtml.js"></script> --%>
<script type="text/javascript">
var houseGJbox;
var chuzu;
function replaceAlls(a){
  var aa=a;
  if(aa==null || aa==undefined){
	  return "";
  }
  aa=aa.replace(/ /g, '');
  aa=aa.replace(/　/g, '');
  aa=aa.replace(/，/g, ',');
  aa=aa.replace(/、/g, ',');
  aa=aa.replace(/\//g, ',');
  return aa;
}
function reload_genjin(){
  window.location.reload();
}
function selectArea(area){
  $(window.parent.document).find('#search').val(area);
  window.parent.doSearchAndSelectFirst();
}
function sideBtnFun(){
    $('.sideCont').on('click', '.btns', function(event) {
        var Thi=$(this),ThiHid=Thi.data('hid'),ThiType=Thi.data('type');
        if(ThiType=='addGenjin'){
            if(houseGJbox)houseGJbox.close();
            houseGJbox=art.dialog({
                follow: document.getElementById('addGenjinBtn'),
                content: '加载中...',
                title:'添加跟进',
                left:20,
                id:'houseGenjinBox',
                width:'250px',
                padding:'0'
            });
            // jQuery ajax   
            YW.ajax({
                url: '/v/house/house_genjin_add_v2.html',
                mysuccess: function (data) {
                    //blockAlert(data)
                    houseGJbox.content(data);
                },cache: false
            });
            return false;
        }else if(ThiType=='fav'){
            var ThiIsFav=Thi.attr('fav'),
            url,param={
                houseId:$('#dimHouseId').text(),
                chuzu:chuzu
            }
            
            if(ThiIsFav=='1'){
              //表示当前点击按钮的作用是收藏房源,因此此时房源是未收藏状态
                if(chuzu=='1'){
                  url='addRent';
                }else{
                  url='addSell';
                }
                
                alertStr="收藏成功";
                dimStr='<i class="iconfont">&#xe62e;</i> 已收藏';
                Thi.attr('fav',0).attr('title','已收藏');
            }else{
                if(chuzu=='1'){
                  url='deleteRent';
                }else{
                  url='deleteSell';
                }
                
                alertStr="已取消收藏";
                dimStr='<i class="iconfont">&#xe648;</i> 收藏';
                Thi.attr('fav',1).attr('title','点此收藏');
            }
            YW.ajax({
                type: 'POST',
                url: '/c/house/fav/'+url,
                data:param,
                mysuccess: function(data){
                    //var json = JSON.parse(data);
                    alert(alertStr);
                    Thi.html(dimStr);
                },complete:function(){
                    
                }
            });
            return false;
        }else if(ThiType=='map'){
            ThiArea=Thi.data('area');
            art.dialog.open('/v/house/baidu_map.html?area='+ThiArea,{id:'seemap',width:800,height:600});
            return false;
        }else if(ThiType=='copy'){
            alert('不给复制');
            return false;
        }else{
            return false;
        }
    });
}

function telMouseover(tel,type){
	if(type=='agent'){
		return;
	}
	$('#'+tel).show();
}
function telMouseout(tel,type){
	if(type=='agent'){
		return;
	}
	$('#'+tel).hide();
}
function labelAgent(obj){
	event.cancelBubble=true;
	event.preventDefault();
	var tel = $(obj).attr("id");
	var type = $(obj).attr("type");
	var num = $(obj).find('b').text();
	if(num==null || num==undefined || num==''){
		num = 0;
	}else{
		num = Number.parseInt(num);
	}
	
	if(type=='fangzhu'){
		YW.ajax({
	        type: 'get',
	        url: '/c/user/labelAgent?tel='+tel+'&hid=${house.id}',
	        mysuccess: function(data){
	            //$('#'+tel).attr('title' , '点击取消标注中介');
	            num++;
	            $('#'+tel).attr('type' , 'agent');
	            $('#'+tel).html('<i class=" red" title="点击取消标注中介">中介</i><b title="已被'+num+'人标记为中介">'+num+'</b>');
	        }
	    });	
	}else{
		YW.ajax({
	        type: 'get',
	        url: '/c/user/revokeLabelAgent?tel='+tel,
	        mysuccess: function(data){
	        	//$('#'+tel).attr('title' , '点击标注为中介');
	        	num--;
	        	var tmp='<b></b>';
	        	if(num>0){
	        		tmp='<b title="已被'+num+'人标记为中介">'+num+'</b>';
	        	}
	        	$('#'+tel).attr('type' , 'fangzhu');
	            $('#'+tel).html('<i class=" black" title="点击标记为中介">标注为中介</i>'+tmp);
	        }
	    });
	}
}

$(document).ready(function() {
});

function init(){

	//chuzu = getParam('chuzu');
	if($('#sourceLink').attr('link')==''){
		$('#sourceLinkTr').css('display','none');
	}
    sideBtnFun();
    $('[data-toggle=tooltip]').tooltip();
        var lxr='${house.lxr}',
        tel='${tel}';
   	if($('.telBox').length>0){
        var telArr=new Array(),
        lxrArr=new Array();
        if(tel.indexOf('http')<0){
        	tel=replaceAlls(tel);
        }
        telArr=tel.split(',');
        lxr=replaceAlls(lxr);
        lxrArr=lxr.split(',');
        var ThiBox=$('.telBox');
        // blockAlert(ThiBox.attr('seeHM'));
        ThiBox.html('');
        ThiBox.prepend('<div class="alert alert-success btn-xs"></div>');
        var ThiBoxBtn=ThiBox.find('.telSee'),
        ThiBoxDiv=ThiBox.find('div')
        ThiBoxDiv.css({'display':'block','overflow':'hidden'});
        $('.telBox').find('p:last-child').css({'margin':'0'});
        if(ThiBox.attr('seeHM')=="0"){
          ThiBoxDiv.removeClass('alert-success');
          ThiBoxDiv.addClass('alert-danger');
          ThiBoxDiv.html('未共享，联系业务员');
        }else{
          
          var TelBoxStr='';
          var agents = JSON.parse('{}');
          var labelCounts=JSON.parse('{}');
          try{
            agents = JSON.parse('${agents}');
            labelCounts=JSON.parse('${labelCounts}');
          }catch(e){
            
          }
          
          $.each(telArr, function(index, val) {
          		var thiTel=telArr[index],thiLxr='';
              	if(lxrArr.length>index){
                  thiLxr=lxrArr[index];
	              }else{
	                  thiLxr=lxrArr[0];
	              }
              	
	              if(thiTel.indexOf('http')>-1){
	            	  TelBoxStr=TelBoxStr+'<p class="onselect"><span class="lxr">'+ thiLxr +'</span> <img src="'+thiTel+'"/> </p>';
	              }else{
	            	  var type=agents[thiTel],title="",text="";
	            	  var num=labelCounts[thiTel];
	            	  var show="";
	            	  var mouseover="";
	            	  var mouseout="";
	            	  var bHtml="<b></b>";
	            	  if(num>0){
	            		  bHtml='<b title="已被'+num+'人标记为中介">'+num+'</b>';
	            	  }
	            	  if(type=='agent'){
	            		  //title="点击取消标记为中介";
	            		  text='<i class=" red"  title="点击取消标记为中介">中介</i>'+bHtml;
	            		  //color="red";
	            	  }else{
	            		  //title="点击标记为中介";
	            		  text='<i class=" black" title="点击标记为中介">标注为中介</i>'+bHtml;
	            		  //show="noshow";
	            		  //mouseover = "telMouseover("+thiTel+",'"+type+"')";
	            		  //mouseout = "telMouseout("+thiTel+",'"+type+"')";
	            	  }
	            	  TelBoxStr=TelBoxStr+'<p class="onselect" onmouseover="'+mouseover+'" onmouseout="'+mouseout+'" ><span class="lxr">'+ thiLxr +'</span> <span onclick="searchTel(this)" class="tel click">'+ thiTel +'</span> '
	            	  	+'<span class="telFrom"></span> <span class="baidu iconfont" data-toggle="tooltip" style="cursor:pointer" title="百度">&#xe64a;</span>'
	            	  	+'<span id="'+thiTel+'" onclick="labelAgent(this)" title="'+title+'" type="'+type+'" class="fangzhu click '+show+' ">'+text+'</span></p>';  
	              }
              
          });
          TelBoxStr=TelBoxStr+'<i>^</i>';
          ThiBoxDiv.html('点此查看房主资料').addClass('onOpen');
        };
        ThiBox.on('click', 'i,.onOpen', function(event) {
//          alert(ThiBox.find('p').length)
            if(ThiBox.find('p').length<1){
                ThiBoxDiv.html(TelBoxStr).removeClass('onOpen');
                ThiBoxDiv.find('p').each(function(index, el) {
                    var Thisa=$(this),
                    ThiTel=Thisa.find('.tel').text(),
                    ThiTelFrom=Thisa.find('.telFrom');
//                     getTelFrom(ThiTel,Thisa,function(e){
//                         ThiTelFrom.html('['+ e +']');
//                     });
                });
                $('[data-toggle=tooltip]').tooltip();
            }else{
              //blockAlert($(this).find('p').length)
                ThiBoxDiv.html('点此查看房主资料').addClass('onOpen');
            }
            return false;
        });
        ThiBox.on('click', 'span.baidu', function(event) {
            var ThiTel=$(this).parent().find('.tel').html();
            // art.dialog.open('http://www.ip138.com:8080/search.asp?mobile='+ThiTel+'&action=mobile',{
            //     title:'手机号码归属搜索',
            //     height:230,
            //     width:400
            // });
            window.top.gui.Shell.openExternal('http://www.baidu.com/s?wd='+ThiTel);
            // art.dialog.open('http://www.baidu.com/s?wd='+ThiTel,{
            //   title:'百度搜索',
            //   height:'80%',
            //   width:'80%'
            // });
            return false;
        });
    }
    var GenjinTbody=$('.see_house_genjin').find('tbody');
    var GenjinTbodyHtml=GenjinTbody.html();
    if(GenjinTbodyHtml){
    	GenjinTbodyHtml = GenjinTbodyHtml.replace(/(\n)+|(\r\n)+/g, "");
        GenjinTbodyHtml =GenjinTbodyHtml.replace(/ /g,'');	
    }

    if(${gjList.size()==0}){
      $('.see_house_genjin').find('tbody').html('<tr><td class="side_noThing">暂无跟进...</td></tr>');
      //$('.see_house_genjin').find('tbody').html('<tr><td style="padding:3px;"><img src="../../style/images/zjb.png" style="width:240px;"/></td></tr>');
      $('#addGenjinBtn').tooltip('show');
    }
}
$('#area').on('click',function(){
  var Thi=$(this),
  ThiVal=Thi.find('b').text();
  $('#search').val(ThiVal);
  $('#searchBtn').click();
});

function junjiaTJ(area){
	 art.dialog.open('/house/areaPrice.jsp?area='+area,{id:'areaPrice',title:'统计',width:1000,height:550});
}

function searchTel(span){
	$(window.parent.document).find('#tel').val($(span).text());
	window.parent.doSearchAndSelectFirst();
}
</script>
<script type="text/javascript">
loadCss('${refPrefix}/style/css.css');
loadCss('${refPrefix}/bootstrap/css/bootstrap.css');
loadCss('${refPrefix}/style/style.css');
loadCss('${refPrefix}/style/css_ky.css');

loadJs('${refPrefix}/js/buildHtml.js');
loadJs('${refPrefix}/js/jquery.j.tool.js');
loadJs('${refPrefix}/bootstrap/js/bootstrap.js');
loadJs('${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s');
loadJs('${refPrefix}/js/dialog/plugins/iframeTools.source.js');
//loadJs('${refPrefix}/js/house/houseSee_v2.js');
setTimeout(init , 200);
//init();
</script>


<style type="text/css">
.click{cursor: pointer; color:#06C; text-decoration: underline;}
.fangzhu{position:absolute;top:2px;right:4px;color: #FFF; background: rgba(0,0,0,0.1);border-radius: 1px;display: inline-block;padding: 0 3px;font-size: 12px;}
.onselect{position:relative;}
.red{color:red;}
.black{color:#3c763d;font-size:12px;}
.noshow{display:none;}

body .telTable td{ padding: 0px;}
body .telTable .telBox{ border: 0; }
body .telTable .telBox div{ border-radius: 0; }
body .telTable .telBox div p{ padding-left: 5px; }
body .telTable .telBox div p .lxr{ width: 50px;display: inline-block;text-align: right;}
body .telTable .telBox .alert {text-align: left;}
body .telTable .telBox .alert>i,
body .telTable .telBox .onOpen{ text-align: center; }

.telBox i { height:15px;font-size:14px; }
.fangzhu i{  font-size: 12px; display: inline-block;background: none; margin-right: 2px;}

.telBox b{display: inline-block;    font-weight: normal;}

.bottomAction{ border-right: none; display: -moz-box;display: -webkit-box;display: box;-moz-box-orient: horizontal;-webkit-box-orient: horizontal;box-orient: horizontal;/*white-space:nowrap;word-break: keep-all;*/}
.bottomAction li{-webkit-box-flex: 1;display: block; border-left: 1px solid #CFCFCF;}
.bottomAction li:first-child{border: none;}
.bottomAction li a{ width: 100%; border: none; }
</style>

</head>
<body >
<div class="sideCont" style="width:100%; display:table; height:100%;-webkit-user-select: text;">

     <div style="display:table-row;">   
        <div class="sideHead TableMainLeftTit" style="display:table-cell;">
            房源详细
        </div>
     </div>
        
        
     <div style="display:table-row;">     
        <div style="display:table-cell; width:100%;">
        <div style="height:100%; overflow:hidden; overflow-y:auto;">
        <table width="100%" class="TableMainL" id="seeHouse ">
        <tbody>
          <tr>
            <td class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">编号：</td>
                  <td class="neirong TextColor1" id="dimHouseId">${house.id}</td>
                </tr>
              </table>
            </td>
            <td class="You">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">状态：</td>
                  <td class="neirong TextColor1">${ztai} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti biaotiMax">发布人员：</td>
                  <td class="neirong TextColor1">${dname}${fbr}</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti biaotiMax">发布时间：</td>
                  <td class="neirong TextColor1"><fmt:formatDate value="${house.dateadd}" pattern="yyyy-MM-dd HH:mm:ss"/>&nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti" selectstart="yes">楼盘：</td>
                  <td class="neirong">
                  <span onclick="selectArea('${house.area}')" class="onselect" id="area" style=" cursor: pointer; color:#06C; font-weight:bold; font-size:14px;text-decoration: underline;">
                  	<b>${house.area}</b> <c:if test="${house.seeFH==1 }">${house.dhao}#${house.fhao }</c:if> </span> 
                  <span style="float:right;cursor: pointer; color:#06C;  font-size:14px;text-decoration: underline;" href="#" onclick="junjiaTJ('${house.area}');"><i class="iconfont">&#xe664;</i></span>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">面积：</td>
                  <td class="neirong TextColor1"><span style=" color:#F00; font-weight:bold;"><fmt:formatNumber  value="${house.mji}"  type="number"  pattern="###.##" /></span> ㎡</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr extend_id="djia">
                  <td class="biaoti">单价：</td>
                  <td class="neirong TextColor1">
                  <fmt:formatNumber  value="${house.djia}"  type="number"  pattern="#####" />元
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">楼型：</td>
                  <td class="neirong TextColor1">${house.lxing} &nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">装潢：</td>
                  <td class="neirong TextColor1">${house.zxiu} &nbsp;</td>
                </tr>
              </table></td>
            <td class="You">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr extend_id="zjia">
                  <td class="biaoti">总价：</td>
                  <td class="neirong TextColor1">
                  <span style=" color:#F00; font-weight:bold;"><fmt:formatNumber  value="${house.zjia}"  type="number"  pattern="###.#" /></span> 万元
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">楼层：</td>
                  <td class="neirong TextColor1">${house.lceng}/${house.zceng} &nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">户型：</td>
                  <td class="neirong TextColor1">${house.hxf}室${house.hxt}厅${house.hxw}卫 &nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">建于：</td>
                  <td class="neirong TextColor1">${house.dateyear} 年 &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">区域：</td>
                  <td class="neirong TextColor1">${house.quyu} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">地址：</td>
                  <td class="neirong TextColor1">${house.address} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">其它：</td>
                  <td style="word-break: break-all; word-wrap:break-word;" class="neirong TextColor1" style="padding-right:10px;">${house.beizhu} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
            
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao telTable">
                <tr>
                  <td>
                    <table width="100%">
                      <tr>
<!--                         <td class="biaoti">房主：</td> -->
                        <td class="neirong telBox" seeHM="${house.seeHM}"></td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti biaotiMax">业务人员：</td>
                  <td class="neirong TextColor1">${house.forlxr} ${house.fortel}&nbsp;</td>
                </tr>
                <c:if test="${authNames.contains('fy_sh') }">
                <tr id="sourceLinkTr">
                  <td class="biaoti biaotiMax"></td>
                  <td class="neirong TextColor1"><a id="sourceLink" link="${house.href}" href="javascript:void(0)" onclick="window.top.gui.Shell.openExternal('${house.href}');">原链接</a></td>
                </tr>
                </c:if>
              </table>
            </td>
          </tr>
        </tbody>
        </table>
        
        <table class="TableMainLGj see_house_genjin" width="100%">
          <thead>
            <tr>
              <th><h2>跟进列表</h2></th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${gjList }" var="gj">
            <tr class="list">
              <td style="padding-left:5px;padding-right:5px">
              	<div style="margin-bottom:4px;max-width:248px;">${gj.ztai}</div>
                <div style="margin-bottom:4px;max-width:248px;">${gj.conts}</div>
                <p style="display:inline-block;width:110px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden"><span title="${gj.uname}">${gj.dname}-${gj.uname}</span></p>
                 <span style="float:right;color:#999999"><fmt:formatDate value="${gj.addtime}" pattern="yyyy-MM-dd HH:mm"/></span>
              </td>
            </tr>
            </c:forEach>
          </tbody>
        </table>
        </div>
        </div>
     </div>
        
        
        
        
     <div style="display:table-row; height:30px; overflow:hidden;">     
        <div class="sideFoot" style="display:table-cell; height:32px; overflow:hidden;">
        
          <ul class="bottomAction" style="margin-bottom:-3px;">
            <li>
              <a href="#" data-toggle="tooltip" title="添加跟进" class="btns" data-type="addGenjin" id="addGenjinBtn"><i class="iconfont">&#xe640;</i> 添加跟进</a>
            </li>
            <li>
              <c:if test="${fav==0 }">
              <a href="#" data-toggle="tooltip" title="收藏房源" class="btns" data-type="fav" fav="1"><i fav="1" class="iconfont">&#xe648;</i> 收藏</a>
              </c:if>
              <c:if test="${fav==1 }">
              <a href="#" data-toggle="tooltip" title="已收藏房源" class="btns" data-type="fav" fav="0"><i fav="0" class="iconfont">&#xe62e;</i> 已收藏</a>
              </c:if>
            </li>
            <li>
              <a href="#" data-toggle="tooltip" title="地图查看" class="btns" data-type="map" data-area="${house.area}"><i class="iconfont">&#xe60e;</i> 地图</a>
            </li>
          </ul>
        </div>
     </div>


</div>
</body>
</html>

