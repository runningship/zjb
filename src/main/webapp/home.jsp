<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="inc/resource.jsp"></jsp:include>
<%
//Enumeration<String> names = request.getHeaderNames();
//CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
Calendar now = Calendar.getInstance();
now.set(Calendar.HOUR_OF_DAY, 0);
now.set(Calendar.MINUTE, 0);
now.set(Calendar.SECOND, 0);
Date start= now.getTime();

now.set(Calendar.HOUR_OF_DAY, 23);
now.set(Calendar.MINUTE, 59);
now.set(Calendar.SECOND, 59);
Date end= now.getTime();

//long count = dao.countHql("select count(*) from Notice where (isPublic=? or isPublic=?) and  addtime>=? and addtime<=? " , 2,3 ,start , end);
//request.setAttribute("ggCount", count);
%>
<!DOCTYPE html>
<html >
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">

<!-- <link rel="stylesheet" type="text/css" href="/oa/style/cssOa.css" /> -->
<!-- <link rel="stylesheet" type="text/css" href="/oa/style/cocoWindow.css" /> -->
<!-- <link rel="stylesheet" type="text/css" href="/oa/style/cocoWinLayer.css" /> -->
<!-- <link rel="stylesheet" type="text/css" href="/oa/style/im.css" /> -->

<link href="${refPrefix}/style/css.css" rel="stylesheet">
<link href="${refPrefix}/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${refPrefix}/style/style.css" rel="stylesheet">
<link href="${refPrefix}/style/animate.min.css" rel="stylesheet" />
<script src="${refPrefix}/js/jquery.js" type="text/javascript"></script>
<script src="${refPrefix}/js/buildHtml.js" type="text/javascript"></script>
<script src="${refPrefix}/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<%-- <script src="${refPrefix}/js/jquery.timers.js" type="text/javascript"></script> --%>
<%-- <script src="${refPrefix}/js/jquery.input.js" type="text/javascript"></script> --%>
<script src="${refPrefix}/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<%-- <script src="${refPrefix}/js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script> --%>
<script type="text/javascript">
function setSideMenuCurr(){
    var menuerBox=$('.menuSide'),
    menuerBoxOn=menuerBox.find('a').parents('.curr'),
    menuerBoxOnH=menuerBoxOn.innerHeight(),
    menuerBoxTop=menuerBoxOn.position().top,
    menuerBoxOnI=menuerBoxOn.index(),
    menuerBoxI=menuerBox.children('#menu_animate_block');
    menuerBoxI.stop().animate({top: menuerBoxTop+(menuerBoxOnH/2-10)},300);
}
var selectedFrame;
var hasShowAds=false;
var web_socket_on=false;
function setSideMenu(){
    var menuerBox=$('.menuSide'),
    menuerIndex=0,
    menuerBoxH=menuerBox.innerHeight(),
    menuerBoxTop=menuerBox.position().top,
    menuerBoxLH=menuerBox.children('li').height();
    menuerBox.append('<div id="menu_animate_block"></div>');
    var menuerBoxI=menuerBox.children('#menu_animate_block');
    menuerBox.css({'position':'relative'});
    menuerBoxI.css({
        'position':'absolute',
        'right': 0,
        'border-color':'transparent #FFF transparent transparent',
        'border-style':'solid',
        'border-width':'10px',
        'font-size':'0',
        'height':'0px',
        'line-height':'0',
        'height':'20px',
        'width':'20px'
    });

    menuerBox.on('click', 'a', function(event) {
        var Thi=$(this),
        ThiPLi=Thi.parents('li'),
        ThiIndex=ThiPLi.index(),
        ThiPLiH=ThiPLi.height(),
        ThiTop=ThiPLi.position().top,
        ThiTopAct=0;
        if(ThiIndex<=menuerIndex){
            ThiTopAct= ThiTop+(ThiPLiH/2)-10
        }else{
            ThiTopAct= ThiTop+(ThiPLiH/2)-10
        }
        menuerBoxI.stop().animate({top:ThiTopAct},300);
        menuerIndex=ThiIndex;
    })

    var menuerBoxOn=menuerBox.find('a').parents('.curr'),
    menuerBoxOnH=menuerBoxOn.innerHeight(),
    menuerBoxTop=menuerBoxOn.position().top,
    menuerBoxOnI=menuerBoxOn.index();

    menuerBoxI.stop().animate({top: menuerBoxTop+(menuerBoxOnH/2-10)+(menuerBoxOnH*menuerBoxOnI)},300);
}
/*设置iframebox与z-index*/
var iframeIndex=100;
function setiframe(id,src){
    iframeIndex+=1;
var iframeBox=$('#iframeBox'),
    iframeDom='<iframe src="'+src+'" id="iframe_'+id+'" name="iframe_'+id+'" class="iframe" width="100%" height="100%" marginheight="0" frameborder="0" style="overflow:auto;"></iframe>';
    //设置绝对定位元素的宽对等于父元素的宽度
    
    iframeBox.find('.iframe').hide();
    if(iframeBox.find('#iframe_'+id).length>0){
        if(iframeBox.find('#iframe_'+id).contents().find('body').length<1){
            iframeBox.find('#iframe_'+id).attr('src',src);
        }
        iframeBox.find('#iframe_'+id).css({'z-index': iframeIndex,'opacity':0}).show().animate({'opacity':1});
    }else{
        iframeBox.append(iframeDom).css({'z-index': iframeIndex});
    }
    selectedFrame =  $('#iframe_'+id);
    // $(iframeBox).width($(iframeBox).parent().width());
}

var frameCheckTimer;
function beforeIframeChange(frameId , targetUrl){
	$('#'+frameId)[0].contentWindow.location = targetUrl;
	frameCheckTimer = setTimeout(function(){
		clearTimeout(frameCheckTimer);
		beforeIframeChange(frameId , targetUrl);
	} , 5000);
}

function iframeChanged(frameId){
	clearTimeout(frameCheckTimer);
}

$(function() {

try{
    // $('#iframeBox').height($('#iframeBox').height()+40);
   
    try{
        win.setMinimumSize(1040,660);
        win.setMaximumSize(9020,9960);
        win.on('resize', function (stream) {
            if(screen.width-win.width<5){
                WinMaxOrRev(0);
            }else{
                WinMaxOrRev(1);  
            }
        });
        win.window.onblur=function(){
            win.isFocus=false;
        };
        win.window.onfocus=function(){
            win.title="中介宝";
            win.isFocus=true;
        };
    }catch(e){
        console.error(e);
    }
    setTimeout(notify,3000);
    setSideMenu();
    // 添加winBox边框
    WinBoxBorder();
    gui.Window.get().on('close', function() {
        quit();
    });
    WinMax();
    
}catch(e){
    console.error(e);
};

// window.onresize = function(){
//   $('#iframeBox').height($('#iframeBox').height()+40);
// }

$(document).on('click', '.ibtn', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type'),
    ThiPbox=Thi.parents('.menuSide'),
    ThiPli=Thi.parents('li');
    ThiPbox.find('li').removeClass('curr');
    ThiPli.addClass('curr');
    if(ThiType=='url'){
        var ThiIdf=Thi.data('id'),
            ThiHref=Thi.attr('href');
        if(ThiHref){
            setiframe(ThiIdf,ThiHref);
        }
    }
    return false;
});

//点击第一个菜单
$('.menuSide').find('.curr a').click();
    my_uid=${me.id};
    my_avatar=${me.avatar};
    my_name = '${me.uname}';
    ws_url = 'ws://${domainName}:9099?uid=${me.id}&city=${me.cityPinyin}';
    //start IM	
	
});


$(window).resize(function() {
    setSideMenuCurr();
});

function resetPwd(){
  art.dialog.open('/v/settings/Pwdchange.html');
}

function fankui(){
  art.dialog.open('/v/plugin/fankui.html');
}

function pay(){
  art.dialog.open('/v/pay/order.html');
}

function openBackList(){
	art.dialog.open('/pay/bank_list.html');
}

function openFeedBack(fbId){
    art.dialog.open('/v/settings/feedback_huifu.html?id='+fbId,{
      id:'fbReply',
      title:'反馈'
    });
    //设置已读
    YW.ajax({
        type: 'get',
        datatype: 'json',
        url: '/c/reply/setRead?fbId='+fbId
      });
}

function notify(){
   YW.ajax({
    type: 'get',
    datatype: 'json',
    url: '/c/reply/getUnReadReply',
    mysuccess: function(data){
        var json = JSON.parse(data);
      if(json.unReadFbId==-1){
        return;
      }
      art.dialog.notice({
        title: '小助手',
        width: 140,
        content: '<a style="font-size:13px;" href="#" onclick="openFeedBack('+json.unReadFbId+')">您有一条新的消息</a>',
        padding:'0px 15px 20px 15px',
        time:5000
      }); 
    }
  });

  
}

function editProfile(){
	art.dialog.open('/settings/user_edit.jsp');
}

function openReg(){
	art.dialog.open('/settings/uc_index_u_reg.jsp');
}
</script>
<script>
/*function popup(childFrame,callback){
    childFrame[callback].call(childFrame,returnValue);
}*/
//$("body").find("iframe#iframe_oa")[0].contentWindow.LayerShow();
</script>
<script type="text/javascript">
var icoOA;
var icoGC;
function showAds(){
    var xx = $('#iframe_house')[0];
    //xx.contentWindow.showAds('/ad/img/zjb/baidu.png');
    $('.adboxs').show();
}
$(document).ready(function() {
	var times;
    $('.showAds').hover(function(){
        times=setTimeout(showAds,500);
    },function(){
        clearTimeout(times);
    }).click(function(event) {
        showAds();
        clearTimeout(times);
    });
    //显示小广告
    //showAds();
});

function icoDD(c,n){
    var Thi=$(c),
    ThiI=Thi.find('i.iconfont');
    var T1,T2,T3,T4,Ta,Tb;
    if(n!=false||n==true){n=true}else{n=false}
    function a2(){
        T1=setTimeout(function(){
            ThiI.css({'color': '#FFF'});
            //clearTimeout(T2);
        },200);
    }
    function a1(){
        T1=setTimeout(function(){
            ThiI.css({'color': '#F00'});
            a2();
            //clearTimeout(T1);
        },200);
    }
    function b1(){
        var i=0;
        Ta=setInterval(function() {
            if(i<=1){
                a1();
                ++i;
            }else{
                clearInterval(Ta);
            }
        },400)
    }
    return setInterval(function() {
        b1();
    },1800);
}
$(document).ready(function() {
// 	getUnReadStatistic();
// 	setInterval(function() {
// 		getUnReadStatistic();
//     },60*1000);
//clearInterval(icoOA);
});

function getUnReadStatistic(){
	YW.ajax({
	    type: 'get',
	    datatype: 'json',
	    url: '/c/oa/getUnReadStatistic',
	    mysuccess: function(data){
	        var json = JSON.parse(data);
	        if(json.oaCount>0){
	        	if(!icoOA){
	        		icoOA=icoDD('.oaClass');	
	        	}
	        }else{
	        	clearInterval(icoOA);
	        	icoOA=null;
	        }
	        if(json.piazzaCount>0){
	        	if(!icoGC){
	        		icoGC=icoDD('.guangchang');	
	        	}
	        	
	        }else{
	        	clearInterval(icoGC);
	        	icoGC = null;
	        }
	    }
	  });
}
</script>

</head>
<body>
<div id="allMainBoxer" style="width:100%; height:100%; overflow:hidden; position:relative;">

<c:if test="${use_im==1}">
    <div>
<%--         <jsp:include page="oa/coco.jsp"></jsp:include> --%>
    </div>
</c:if>

<div class="html default table MainLeft">




    <div class="tr">
        <div class="lefter td title">
            <div class="table">
                <div class="tr thead">
                    <span title="$${version} 更新概要&#13; 1.系统管理&#13; 2权限优化" class="logo title nobar"></span>
                </div>
                <div class="tr tbody">
                    <ul class="menuSide">
                        <li class="curr"><a href="/house/house_v2.jsp" class="ibtn" data-type="url" data-id="house" data-toggle="tooltip" data-placement="right" title="房源"><i class="iconfont">&#xe636;</i><span>房源</span></a></li>
<%--                         <c:if test="${auths.indexOf('fy_fz_on')>-1}"> --%>
<!--                         <li ><a href="/houseOwner/OwnerList.jsp" class="ibtn" data-type="url" data-id="client" data-toggle="tooltip" data-placement="right" title="房主"><i class="iconfont">&#xe601;</i><span>房主</span></a></li> -->
<%--                         </c:if> --%>
                        <c:if test="${authNames.contains('ky_on')}">
                        <li ><a href="/client/index.jsp" class="ibtn" data-type="url" data-id="client" data-toggle="tooltip" data-placement="right" title="客源"><i class="iconfont">&#xe601;</i><span>客源</span></a></li>
                        </c:if>
                        <c:if test="${authNames.contains('yw_on')}">
                        <li ><a href="/v/yewu/outHouse.html" class="ibtn" data-type="url" data-id="yewu" data-toggle="tooltip" data-placement="right" title="业务"><i class="iconfont">&#xe608;</i><span>业务</span></a></li>
                        </c:if>
                        <c:if test="${authNames.contains('ht_on')}">
                        <li ><a href="/v/plugin/wait/wait.html" class="ibtn" data-type="url" data-id="hetong" data-toggle="tooltip" data-placement="right" title="合同"><i class="iconfont">&#xe614;</i><span>合同</span></a></li>
                        </c:if>
                        <c:if test="${authNames.contains('cw_on')}">
                            <li><a href="/v/plugin/wait/wait.html" class="ibtn" data-type="url" data-id="caiwu" data-toggle="tooltip" data-placement="right" title="财务"><i class="iconfont">&#xe613;</i><span>财务</span></a></li>    
                        </c:if>
                        <c:if test="${authNames.contains('oa_on')}">
                            <li ><a href="/oa/index.jsp" class="ibtn oaClass" data-type="url" data-id="oa" data-toggle="tooltip" data-placement="right" title="OA"><i class="iconfont">&#xe633;</i><span>OA</span></a></li>
                        </c:if>
                        <li >
                        	<a href="/piazza/index.jsp" class="ibtn guangchang" data-type="url" data-id="piazza" data-toggle="tooltip" data-placement="right" title="广场"><i class="iconfont">&#xe604;</i><span>广场</span></a>
                        </li>
                        <c:if test="${authNames.contains('map_on')}">
                         <li >
                        	<a href="/map/xuequ.jsp" class="ibtn guangchang" data-type="url" data-id="xuequ" data-toggle="tooltip" data-placement="right" title="地图"><i class="iconfont">&#xe67d;</i><span>地图</span></a>
                        </li>
                        </c:if>
                        <c:if test="${authNames.contains('sz_on')}">
                            <li class=" positionBottom"><a href="/settings/uc_index.jsp" data-id="sz" class="ibtn" data-type="url" data-toggle="tooltip" data-placement="right" title="设置"><i class="iconfont">&#xe62c;</i></a></li>
                        </c:if>
                            
                    </ul>
                </div>
<!--                 <div class="tr tfoot">
    <ul class="menuSideTool">
        <li><a href="/settings/uc_index.html" class="ibtn" data-type="url" data-toggle="tooltip" data-placement="right" title="设置"><i class="iconfont">&#xe62c;</i></a></li>
    </ul>
</div> -->
            </div>
        </div>
        <div class="righter td title">
            <div class="table" >
                <div class="tr thead title">
                    <div id="dragbar" class="maxHW title titlebar" style="float:right;width:400px;height:40px;"></div>
                    <div class="wintool title nobar">
                        <ul class="wintools" style="padding-left:50px;">
<!--                         	<li><a href="#" onclick="return false;"><img src="style/images/phone.png" style="width:17px;height:17px;margin-right:13px;" onmouseover="$('#ewm').attr('style','position: absolute;top:35px;left:-50px;border:none;width:200px;');" onmouseout="$('#ewm').attr('style','display:none;');"><img src="style/images/zjb-android.png" id="ewm" style="display:none"/></a></li> -->
                            <li><a href="#"  onclick="window.top.gui.Shell.openExternal('http://.a.zhongjiebao.com');" title="中介宝外网" class="winBtns "><i class="iconfont">&#xe666;</i></a></li>
                            <li><a href="#" onclick="return false;" title="中介宝手机版" class="winBtns showAds"><i class="iconfont">&#xe678;</i></a></li>
                            <li><a href="#" onclick="fankui();" title="请给予我们您的宝贵意见" class="winBtns "><i class="iconfont">&#xe633;</i></a></li>
                            <li class="dropdown btn-group">
                                <a href="" class="winBtn black winBtnMenu" data-toggle="dropdown"><i></i></a>
                                <ul class="dropdown-menu dropdown-menu-right" role="menu">
                                    <li><a href="javascript:void(0)"><i class="iconfont">&#xe68b;</i> 姓名: <b class="me_uname">${me.uname}</b></a></li>
                                    <li><a href="javascript:void(0)"><i class="iconfont">&#xe651;</i> 账号: <b class="me_lname">${me.lname}</b></a></li>
                                    <li><a href="javascript:void(0)"><i class="iconfont">&#xe61b;</i> <b class="me_cname">${cname}-${dname}</b></a></li>
                                    <li><a href="javascript:void(0)"><i class="iconfont">&#xe657;</i> 职位: <b class="me_title">${role.title}</b></a></li>
                                    <li><a href="javascript:void(0)"><i class="iconfont">&#xe662;</i> 电话: <b class="me_tel">${me.tel}</b></a></li>
                                    <li role="presentation" class="divider"></li>
                                    <li onclick="openReg();"><a href="javascript:void(0)"><i class="iconfont">&#xe69b;</i> 注册账号</a></li>
                                    <li onclick="editProfile();"><a href="javascript:void(0)"><i class="iconfont">&#xe69d;</i> 修改资料</a></li>
                                    <li role="presentation" class="divider"></li>
                                    <li><a href="javascript:void(0)" onclick="pay();"><i class="iconfont">&#xe623;</i> 在线支付</a></li>
                                    <li><a href="javascript:void(0)" onclick="openBackList();"><i class="iconfont">&#xe623;</i> 银行账户</a></li>
                                    <li class="hidden"><a href="javascript:void(0)"><i class="iconfont">&#xe623;</i> 到期: 30 天  <span class="badge">支付</span></a></li>
                                    <li role="presentation" class="divider"></li>
                                    <!-- <li><a href="javascript:void(0)" onclick="resetPwd();">修改密码</a></li> -->
                                    <!-- <li><a href="javascript:void(0)" onclick="fankui();">建议反馈</a></li> -->
                                    <li><a href="javascript:void(0)" onclick="relogin();">重新登录/切换账号</a></li>
                                    <!-- <li><a href="javascript:void(0)" onclick="pay();">在线付费</a></li> -->
                                </ul>
                            </li>
                            <li><a href="" class="winBtn black winBtnMin" data-q="min"><i></i></a></li>
                            <li><a href="" class="winBtn black winBtnMax" data-q="max"><i></i></a></li>
                            <li><a href="" class="winBtn black winBtnRevert" data-q="rev"><i></i></a></li>
                            <li><a href="" class="winBtn black winBtnClose" data-q="close"><i></i></a></li>
                        </ul>    
                    </div>
                </div>
                <div class="tr tbody">
                    <div id="iframeBox" class="maxHW" style="margin-top:-40px;height:105%"></div>
                </div>
            </div>
        </div>
    </div>
</div>

</div>
<script type="text/javascript">
function EditToolUser(json){
    var S=json;//eval(json);
    if (!S) return false;
    $('.me_tel').text(S.tel);
    $('.me_uname').text(S.uname);
}
</script>
<jsp:include page="ad/2016/phone/download.jsp"></jsp:include>
<jsp:include page="ad/2016/reg/ad_regPhone.jsp"></jsp:include>
</body>
</html>