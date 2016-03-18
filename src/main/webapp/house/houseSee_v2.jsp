<%@page import="com.youwei.zjb.cache.HouseCacheVO"%>
<%@page import="com.youwei.zjb.cache.HouseViewCache"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.youwei.zjb.house.entity.HouseImageGJ"%>
<%@page import="com.youwei.zjb.cache.ConfigCache"%>
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
String hql="select tt.conts as conts , d.dname , tt.uname , tt.addtime ,tt.ztai as ztai from (select gj.ztai as ztai, gj.id as id,gj.hid as houseId,gj.conts as conts,gj.did as did,u.uname as uname,"
		+"gj.addtime as addtime,gj.sh as sh,gj.chuzu as chuzu from house_gj gj ,uc_user u "
		+" where gj.hid = ? and u.id=gj.uid  and gj.chuzu=?) tt "
		+" left join (select d.id as did, d.namea as dname, c.namea as cname from uc_comp c, uc_comp d where d.fid=c.id) d on d.did=tt.did order by tt.addtime desc";
List<Map> gjList = null;
HouseCacheVO cachePO = HouseViewCache.getInstance().loadHouse(h.id);
if(cachePO==null){
	gjList = dao.listSqlAsMap(hql.toString(), Integer.valueOf(id) , 0);
 	cachePO = new HouseCacheVO();
 	cachePO.gjList = gjList;
 	cachePO.hid = h.id;
 	HouseViewCache.getInstance().putHouse(cachePO);
}else{
	gjList = cachePO.gjList;
}
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

User u = ThreadSessionHelper.getUser();
String sql = "select hi.id as hiid , hi.uid as uid , hi.hid as hid, hi.path as path, u.uname as uname ,u.tel as tel,hi.zanCount as zanCount , hi.shitCount as shitCount  from HouseImage hi , uc_user u "
		+"where u.id=hi.uid and hid=? and chuzu=0 and isPrivate=0";
List<Map> list = dao.listSqlAsMap(sql, h.id);

List<HouseImageGJ> zanList = dao.listByParams(HouseImageGJ.class, "from HouseImageGJ where hid=? and uid=? ", h.id , u.id);
JSONObject zanObj = new JSONObject();
for(HouseImageGJ gj : zanList){
	JSONObject tmp = new JSONObject();
	tmp.put("zan", gj.zan);
	tmp.put("shit", gj.shit);
	zanObj.put(gj.hiid, tmp);
}
request.setAttribute("zanObj", zanObj);
request.setAttribute("imgList", list);
request.setAttribute("imgList", list);
String host = ConfigCache.get("domainName", "www.zhongjiebao.com");
request.setAttribute("host", host);
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
            art.dialog.open('/house/baidu_map.jsp?area='+ThiArea,{id:'seemap',width:800,height:600});
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
var imgJSON = JSON.parse("{}");
var zanObj = JSON.parse('${zanObj}');
function init(){
	//构造房源图片 json格式 
	var imgs = $('.imgListBox img');
	var data = [];
	for(var i=0;i<imgs.length;i++){
		var obj = JSON.parse("{}");
		obj.src=$(imgs[i]).attr('path');
		obj.start=i;
		obj.hiid = $(imgs[i]).attr('hiid');
		obj.zanCount = parseInt($(imgs[i]).attr('zanCount'));
		obj.shitCount = parseInt($(imgs[i]).attr('shitCount'));
		if(!zanObj[obj.hiid]){
			obj.zan=0;
			obj.shit = 0;
		}else{
			obj.zan = zanObj[obj.hiid].zan;
			obj.shit = zanObj[obj.hiid].shit;
		}
		data.push(obj);
	}
  imgJSON.data = data;
  imgJSON.zanObj = zanObj;
	imgJSON.status=1;
	imgJSON.start=0;
	
	//chuzu = getParam('chuzu');
	if($('#sourceLink').attr('link')!=''){
		$('#sourceLinkTr').css('display','');
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
        ThiBoxDiv.css({'display':'block','min-height': '28px'});
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
	            	  TelBoxStr=TelBoxStr+'<p class="onselect" onmouseover="'+mouseover+'" onmouseout="'+mouseout+'" ><span class="lxr">'+ thiLxr +'</span> <span onclick="searchTel(this)" class="tel click hoverTitle" data-gtf="'+thiTel+'">'+ thiTel +'<span class="hoverTitBox"  id="GTF'+thiTel+'"  ></span><span class="hoverJT"></span></span> '
	            	  	+'<span data="'+thiTel+'" class="telFrom" ></span> <span class="baidu iconfont" data-toggle="tooltip" style="cursor:pointer" title="百度">&#xe64a;</span>'
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
                    // ThiTelFrom.each(function(index , obj){
                    // 	getTelForm($(obj).attr('data'));
                    // });
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
            var ThiTel=$(this).parent().find('.tel').attr('data-gtf');
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
        ThiBox.on("mouseover mouseout",'.hoverTitle',function(event){
          var T=$(this),
          Tel=T.attr('data-gtf');
          if(event.type == "mouseover"){
            getTelForm(Tel);
          }else if(event.type == "mouseout"){
            //Tel
          }
        })
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
    
    ImgListBox($('.see_house_images .imgbox img.img'));

    $(document).on('click','.btnIcon',function(){
      //alert($('.see_house_genjin').offset().top)
      var Thi=$(this),
      DSname=Thi.data('sname');
      //alert(DSname)
      ScrollGoto($('.'+DSname));
    })

      var T1=$('.TableMainL'),
      T2=$('.see_house_genjin'),
      T3=$('.see_house_images');
      var T1top=(T1.offset().top-34),
      T2top=(T2.offset().top-34),
      T3top=(T3.offset().top-34);
    $('.sideMainer').scroll(function(e){
      //alert($(this).scrollTop())
      var Thi=$(this),
      ThiScrollTop=Thi.scrollTop();
      //alert(T1top+'|'+T2top+'|'+ThiScrollTop)
      if(ThiScrollTop<=T2top){
        $('.TableMainLeftTit h2').text('房源详细');
      }else if(ThiScrollTop<=T3top){
        $('.TableMainLeftTit h2').text('跟进列表');
      }else if(ThiScrollTop>=T3top){
        $('.TableMainLeftTit h2').text('房源图片');
      }
    });
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
	$(window.parent.document).find('#tel').val($(span).attr('data-gtf'));
	window.parent.doSearchAndSelectFirst();
}

function openOriginalLink(link){
	if(!window.top.gui){
		window.open(link , '_blank');
	}else{
		window.top.gui.Shell.openExternal(link);
	}
}
</script>

<script type="text/javascript">
</script>

<script type="text/javascript">
loadCss('${refPrefix}/style/css.css');
loadCss('${refPrefix}/bootstrap/css/bootstrap.css');
loadCss('${refPrefix}/style/style.css');
loadCss('${refPrefix}/style/css_ky.css');

loadJs('${refPrefix}/js/buildHtml.js');
//loadJs('${refPrefix}/js/jquery.j.tool.js');
loadJs('${refPrefix}/bootstrap/js/bootstrap.js');
loadJs('${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s');
loadJs('${refPrefix}/js/dialog/plugins/iframeTools.source.js');
loadJs('${refPrefix}/js/house/getTelFrom.js');
setTimeout(init , 300);
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
            <span class="fr">
              <a class="btnIcon" data-sname="TableMainL" title="顶部"><i class="iconfont">&#xe617;</i></a>
              <a class="btnIcon" data-sname="see_house_genjin" title="跟进"><i class="iconfont">&#xe633;</i></a>
              <a class="btnIcon" data-sname="see_house_images" title="图片"><i class="iconfont">&#xe634;</i></a>
            </span>
            <h2>房源详细</h2>
        </div>
     </div>
        
        
     <div style="display:table-row;">     
        <div style="display:table-cell; width:100%;">
        <div class="sideMainer" style="height:100%; overflow:hidden; overflow-y:auto;">
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
	                  <c:if test="${house.zjia==0 }">面议</c:if>
	                  <c:if test="${house.zjia!=0 }">
	                  		<span style=" color:#F00; font-weight:bold;"><fmt:formatNumber  value="${house.zjia}"  type="number"  pattern="###.#" /></span> 万元
	                  </c:if>
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
                <tr id="sourceLinkTr" style="display:none">
                  <td class="biaoti biaotiMax"></td>
                  <td class="neirong TextColor1"><a id="sourceLink" link="${house.href}" href="javascript:void(0)"  onclick="openOriginalLink('${house.href}')"  >原链接</a></td>
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
              <th><h2 class="h2">跟进列表</h2></th>
            </tr>
          </thead>
          <tbody>
          	<c:forEach items="${gjList }" var="gj">
            <tr class="list">
              <td style="padding-left:5px;padding-right:5px">
                <div style="margin-bottom:4px;max-width:248px;">${gj.conts}</div>
                <div style="margin-bottom:4px;max-width:248px;font-size:11px">(状态变更:${gj.ztai})</div>
                <p style="display:inline-block;width:110px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden"><span title="${gj.uname}">${gj.dname}-${gj.uname}</span></p>
                 <span style="float:right;color:#999999"><fmt:formatDate value="${gj.addtime}" pattern="yyyy-MM-dd HH:mm"/></span>
              </td>
            </tr>
            </c:forEach>
          </tbody>
        </table>
<script type="text/javascript">
function ImgAutoBox(img){
    function imgAct(){
        var ThiW=Thi.width(),
        ThiH=Thi.height(),
        isW='W',
        ThiP=Thi.parent(),
        ThiPW=ThiP.width(),
        ThiPH=ThiP.height();
        if(ThiH>ThiW){isW='H'}
        if(isW=='W'){
            Thi.css({
                'height': ThiPH,
                'width': 'auto'
                
            });
            ThiWn=(Thi.width()-ThiPW)/2;
            Thi.css({'margin-left':-ThiWn})
        }else{
            Thi.css({
                'width': ThiPW,
                'height': 'auto'
            });
            ThiHn=(Thi.height()-ThiPH)/2;
            Thi.css({'margin-top':-ThiHn})
        }
        ThiP.css('overflow', 'hidden');
    }
    var Thi=img;
    var img = new Image(); 
    img.src =Thi.attr("src");
    if(img.complete){
        imgAct();
    }else{
        img.onload = function(){
            imgAct();
        }
    }
}
function ImgListBox(a){
    if(!a){a=$('.imgbox img')}
    var Thi=a,ThiBoxWidth=0;
    Thi.each(function(index, el) {
        var This=$(this),
        ThiIMG=This,
        ThiBox=This.parents('.imgbox');
        if(ThiBoxWidth==0){ThiBoxWidth=ThiBox.width()}
        //ThiBoxWidth=''?ThiBox.width():ThiBoxWidth;
        ThiBox.height(ThiBoxWidth);
        This.find('a.jia').css({
            'height': ThiBoxWidth+'px',
            'line-height':ThiBoxWidth+'px'
        });
        ImgAutoBox(ThiIMG);
    });
}

function ScrollGoto(a){
    if(a.length>0){
      var ThiPTop=($('.TableMainL').offset().top-34)*(-1)
        var ThiTop=a.offset().top+ThiPTop;
        if(ThiTop<100){ThiTop=ThiTop-34}
        //alert(a.offset().top)
        $('.sideMainer').animate({'scrollTop': ThiTop},500, function(){});
    }
}
$(document).on('click', '.imgListBox .imgbox', function(event) {
	imgJSON.start = $(this).index();
  	window.parent.openMaxPic(imgJSON)
  	event.preventDefault();
});
</script>
<style type="text/css">
.TableMainLeftTit{ position: relative; }
.TableMainLeftTit .fr{ position: absolute; top:4px; right: 5px; height: 26px; line-height: 22px; border: 1px solid #CFCFCF; border-radius: 2px; background:#EFEFEF; box-shadow: -1px -1px 0px #FFF; }
.TableMainLeftTit .fr a{ display: block; height: inherit; line-height: inherit; float: left; padding: 0 2px; color: #999; text-shadow: -1px -1px 0px #FFF; }
.TableMainLeftTit .fr i{ font-size: 18px; }
h2{ margin: 0; padding: 0; font-size: 100%; line-height: inherit; }

h2.h2{border-bottom: 1px solid #d1d1d1;
    background: -webkit-gradient(linear, 0 0, 0 100%, from(#ffffff), to(#e9e9e9));}

</style>
	        <table class="TableMainLGj see_house_images" width="100%">
	          <thead>
	            <tr>
	              <th><h2 class="h2">房源图片 <i></i></h2></th>
	            </tr>
	          </thead>
	          <tbody>
	            <tr class="list">
	              <td>
        <c:if test="${imgList.size()>0 }">
	                <div class="imgListBox">
	                	<c:forEach items="${imgList }"  var="img">
	                		<c:if test="${ not empty img.path }">
	                			<a href="#" class="imgbox"  ><img hiid="${img.hiid }" zanCount="${img.zanCount }"  shitCount="${img.shitCount }"  path="http://${host }/zjb_house_images/${img.hid }/${img.uid}/${img.path}"  src="http://${host }/zjb_house_images/${img.hid }/${img.uid}/${img.path}.t.jpg" alt="" class="img"></a>
	                		</c:if>	
	                	</c:forEach>
	                </div>
    </c:if>
    <c:if test="${imgList.size()==0 }">
                  <div class="see_house_noImages">
                    <i class="iconfont">&#xe67c;</i> 暂无图片...
                  </div>
    </c:if>
	              </td>
	            </tr>
	          </tbody>
	        </table>

          <div class="see_house_gotoPhone">
           <span class="imgbox"><img src="../style/images/zjb_all_101_rgba.png" alt="" class=""></span>
           <span class="textbox">
             <h3>中介宝APP</h3>
             <div class="">5000+ 人在用<br><a href="#" class="btn btn_free">五星好评双重送</a></div>
           </span>
           <div class="see_house_xiangxi">
             <ul class="">
               <li>①.各大APP应用商城五星好评送手机版5天时间(每个手机号码只送1次)。</li>
               <li>②.百度搜索中介宝，五星好评送手机版5天时间。</li>
               <li>③.手机版上传房源真实照片，被点1个赞增加1个积分。</li>
               <li>④.活动期间5积分可兑换1天手机版时间。</li>
               <li>⑤.手机版已过期无法上传照片，请参考一和二。</li>
               <li>⑥.五星好评后拍照或截图发给中介宝客服。</li>
               <li>⑦.如有疑问请联系中介宝客服<br>　 QQ:9129588　0551-65314555。</li>
             </ul>
           </div>
          </div>
        </div>
        </div>
     </div>
        
<script type="text/javascript">
$(document).on('click', '.btn_free', function(event) {
  var FreeSM=$('.see_house_xiangxi'),
  isFree=FreeSM.hasClass('show');
  if(isFree){
    FreeSM.removeClass('show');
    ScrollGoto($('.see_house_gotoPhone'));
  }else{
    FreeSM.addClass('show');
    ScrollGoto($('.see_house_gotoPhone'));
  }
  event.preventDefault();
});
</script>
        
        
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
              <a href="#" data-toggle="tooltip" title="地图查看" class="btns" data-type="map" data-area="${house.area}" class="hoverTitle"><i class="iconfont">&#xe60e;</i> 地图</a>
            </li>
          </ul>
        </div>
     </div>


</div>
<style type="text/css">
.hoverTitle{ position: relative; }
.hoverTitle .hoverTitBox{ display: none; position: absolute; background: #FFF;left: 0;top: -26px;text-align: center;width: 100%; border: 1px solid #999; border-radius: 1px; }
.hoverTitle:hover .hoverTitBox{ display: block;  }
.hoverTitle .hoverJTs{ position: absolute; bottom: 0; left: 45%; display: block; border-top: 6px solid #999; border-left: 6pt solid transparent; border-bottom: 6pt solid  transparent; border-right: 6pt solid  transparent;}

.hoverTitle .hoverTitBox{ font-size: 14px; font-family: 'microsoft yahei'; color: #696969; }
</style>
</body>
</html>

