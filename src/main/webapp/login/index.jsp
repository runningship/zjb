<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../inc/resource.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝 5.0</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<c:if test="${useLocalResource!=1}">
<link href="/style/css.css" rel="stylesheet">
<link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/jquery.timers.js" type="text/javascript"></script>
<script src="/js/jquery.input.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.js" type="text/javascript"></script>
<script src="/js/jquery.SuperSlide.2.1.1.source.js" type="text/javascript"></script>
<link href="/js/Ladda/ladda-themeless.min.css" rel="stylesheet">
<script src="/js/Ladda/spin.min.js" type="text/javascript" async="async"></script>
<script src="/js/Ladda/ladda.js" type="text/javascript" async="async"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
</c:if>
<c:if test="${useLocalResource==1}">
<link href="file:///resources/style/css.css" rel="stylesheet">
<link href="file:///resources/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="file:///resources/style/style.css" rel="stylesheet">
<script src="file:///resources/js/jquery.js" type="text/javascript"></script>
<script src="file:///resources/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="file:///resources/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="file:///resources/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.cookie.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.timers.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.input.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.j.tool.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.SuperSlide.2.1.1.source.js" type="text/javascript"></script>
<link href="file:///resources/js/Ladda/ladda-themeless.min.css" rel="stylesheet">
<script src="file:///resources/js/Ladda/spin.min.js" type="text/javascript" async="async"></script>
<script src="file:///resources/js/Ladda/ladda.js" type="text/javascript" async="async"></script>
<script type="text/javascript" src="file:///resources/js/buildHtml.js"></script>
</c:if>
<script type="text/javascript" src="/js/sysinfo.js"></script>
<script type="text/javascript">
try{
    win.restore();
}catch(e){
    console.log(e);
}
try{
    // win.resizeTo(692,660);
    // win.setPosition();
    gui.App.setCrashDumpDir(".");
    // gui.App.clearCache();
    // for(module in global.require.cache){
    //     if(global.require.cache.hasOwnProperty(module)){
    //         delete global.require.cache[module];
    //     }
    // }
}catch(e){
    console.log(e);
}
try{
    win.setMinimumSize(692,660);
    win.setMaximumSize(692,660);
}catch(e){
    console.log(e);
}
var debug=getParam('debug');
var debugCity = getParam('city');
var json = loadConfigAsJSON();
var cityNames=json.cityName;
var domains = json.domain;
var gotos=getParam('goto');
var pcinfo = getParam('pcinfo');
try{
    if(debug){
        pcinfo = JSON.parse('{}');
        pcinfo.debug = debug;
    }else{
        if(gui.App.manifest['--use_uuid_lic']){
            pcinfo = JSON.parse("{}");
        }else{
            if(pcinfo==undefined || pcinfo==null || pcinfo==""){
                loadHardwareInfo(function(data){
                    pcinfo = data;
                });
            }else{
                pcinfo = decodeURIComponent(pcinfo.replace(/\+/g,  " "));
                pcinfo = JSON.parse(pcinfo);
            }    
        }
        
    }
}catch(e){

}

function dialogClose(){
    var list = art.dialog.list;
    for (var i in list) {
        list[i].close();
    };
}
function onClickNav(index){
    if(!index){index=0}
    $('.act_menu').find('li').eq(index).find('a').click();
}

function isFocus(){
    if(!$('#idName').val()){
        $('#idName').focus();
        return false;
    }
    $('#idPassword').focus();
    return false;
}

function setDomSize(){
    var domW=$(window).width(),
    domW3=parseInt(domW*3),
    bodyer=$('body'),
    header=bodyer.find('.header'),
    menuer=bodyer.find('.menuer'),
    footer=bodyer.find('.footer'),
    bodyerH=bodyer.innerHeight(),
    headerH=header.innerHeight(),
    menuerH=menuer.innerHeight(),
    footerH=footer.innerHeight(),
    mainerH=bodyerH-headerH-menuerH-footerH;
    $(document).find('.tempWrap').width(domW+'px').find('.bd').width(domW3+'px').height(mainerH+'px').find('ul').width(domW+'px').height(mainerH+'px');
    slideW=domW;
//    art.dialog.tips('<br>dom:bodyerH:'+bodyerH+'<br>menuerH:'+menuerH+'<br>mainerH:'+mainerH+'<br>tempWrap:width:'+domW+'<br>bd:width:'+domW3+'|height:'+mainerH+'<br>ul:width:'+domW+'|height:'+mainerH,30);
    var menuOnIndex=menuer.find('.on').index(),
    mainUlLeft=0;
    if(menuOnIndex>0){
        mainUlLeft=domW*(menuOnIndex);
        $(document).find('.bd').css({'left':-mainUlLeft});
    }
/**/
    var menuOn=menuer.find('.on')
    menuOnPLi=menuer.children('li'),
    menuOnPLiW=menuOnPLi.innerWidth(),
    menuOnLeft=menuOn.position().left;
    menuerBoxCurr(menuer.children('#menu_animate_block'),menuOnLeft+(menuOnPLiW/2-10));
}
function ActLnane(){
    var actBox=$('#idNameBox'),
    actInput=actBox.find('.form-control'),
    actList=actBox.find('.form_menu_list'),
    getLnameJSON=loadConfigAsJSON(),
    getLnames=getLnameJSON["account"],
    actListIndex=-1;
    if (getLnames!=undefined&&getLnames.length>0) {
    /* 登陆帐号设置和帐号下拉设置 */
        var Lindex=getLnames.length-1;
        actInput.val(getLnames[Lindex]);isFocus();
        if (getLnames.length>0){
            var htmls='';
            $.each(getLnames, function(index, val) {
                actList.prepend('<a href="#">'+val+'</a>')
                //htmls='<a href="#">'+val+'<a>'+htmls;
            });
            //var actListOl=actList.html(htmls);
        }
    };
    if (getLnames!=undefined&&getLnames.length>1) {
    /* 登陆帐号操作 */
        actInput.on('focusin', actInput, function(event) {
            actList.show(300);
        }).on('focusout', actInput, function(event) {
            var thiTimes=setTimeout(function(){
                actList.hide(200);
                actListIndex=-1;
                actList.find('a').removeClass('curr');
                clearTimeout(thiTimes);
            },300)
        }).on('keydown', function(event) {
            var Thi=$(this),
            ThiVal=Thi.val(),
            ThiLi=actList.find('a');
            if(event.keyCode==38 || event.keyCode==40){
                if(event.keyCode==38){
                    actListIndex--;
                }else if(event.keyCode==40){
                    actListIndex++;
                }else{return false;}
                if(actListIndex>=ThiLi.length){actListIndex--;}
                else if(actListIndex<0){actListIndex=0;}
                Thi.val(ThiLi.eq(actListIndex).text())
                ThiLi.removeClass('curr').eq(actListIndex).addClass('curr');
            }
            if(event.keyCode==13){return false};
            if(event.keyCode==9){return true};
        });
    };
    actList.on('click', 'a', function(event) {
        var a=$(this),
        aTxt=a.text();
        if(aTxt){
            actInput.val(aTxt);
            //actList.hide(200);
        }
        //blockAlert(a.html())
        return false;
    });
}
function hetKeyAct(){
    /* 按键处理 */
    $('#idName').keydown(function(event) {
        if(event.keyCode==13){  
            //if(!$('#idPassword').val()){
                $('#idPassword').focus();
            //}else{
            //    $('.btn_submit').click();
            //}
            return false; //屏蔽回车键    
        } 
        if(event.keyCode==8){  
            return true; //屏蔽退格删除键    
        } 
        if(event.keyCode==9){  
            return true; //屏蔽退tab键    
        } 
    }).focus();
    $('#idPassword').keydown(function(event) {
        if(event.keyCode==13){  
            $('.btn_submit').click();
            return false; //屏蔽退格删除键    
        }
        if(event.keyCode==8){  
            return true; //屏蔽退格删除键    
        } 
        if(event.keyCode==9){  
            return true; //屏蔽退tab键    
        } 
    })
    $('.btn_submit').keydown(function(event) {
        if(event.keyCode==9){  
            return false; //屏蔽F5刷新键   
        }
        if ((event.keyCode==9)&&(event.keyCode==16)){   
           event.returnValue=true;   
           return true;  
       }   
    }) 
}
$(document).ready(function() {
// 设置登陆帐号
    ActLnane();
// 回车等快捷键功能
    hetKeyAct();
// 添加winBox边框
    WinBoxBorder();
// 按钮功能区
    $(document).on('click', '.menuBtn', function(event) {
        var Thi=$(this);
        var ThiType=Thi.data('type');
        if(ThiType=='url'&&ThiType!='#'){
            var ThiUrl=Thi.attr('href');
            if(ThiUrl){shell.openExternal(ThiUrl)}
        }
    }).on('click', '.btn', function(event) {
        var Thi=$(this);
        var ThiType=Thi.data('type');
        if(ThiType=='url'&&ThiType!='#'){
            var ThiUrl=Thi.attr('href');
            if(ThiUrl){shell.openExternal(ThiUrl)}
        }else if(ThiType=='wangjimima'){
            art.dialog({
                id:'tips',
                title:'提示',
                content:'忘记密码，请致电贵公司管理部门，或者联系中介宝客服！',
                ok:true
            });
        }else if(ThiType=='wangjizhanghao'){
            art.dialog({
                id:'tips',
                title:'提示',
                content:'忘记帐号，请致电贵公司管理部门，或者联系中介宝客服！',
                ok:true
            });
        }else if(ThiType=='submit'){
            var l;
            try{
            	Ladda.create(this);
            }catch(e){
            		
            }
            if(l){
            	l.start();	
            }
            
            if(!$('#idName').val()){
            	if(l){
                	l.stop();
            	}
                alert('帐号不能为空');
                $('#idName').focus();
                return false;
            }
            if(!$('#idPassword').val()){
            	if(l){
                	l.stop();
            	}
                alert('密码不能为空');
                $('#idPassword').focus();
                return false;
            }
            pcinfo.lname=$('#idName').val();
            pcinfo.pwd=$('#idPassword').val();
            if(!debug && gui.App.manifest['--use_uuid_lic']){
                var xx = readLic();
                if(xx==undefined){
                    alert('请先授权');
                    if(l){
                    	l.stop();
                	}
                    return;
                }else{
                    pcinfo.uselic=1;
                    pcinfo.lic = xx.lic;
                    pcinfo.licCreateTime = xx.licCreateTime;
                }
                
            }
            json = loadConfigAsJSON();
            pcinfo.cityPinyin=json.city_py;
            pcinfo.cityCoordinate=json.coodinate;
            if(!pcinfo.cityPinyin){
            	//alert('请先设置城市');
                pcinfo.cityPinyin = "hefei";
            }
            if(!pcinfo.cityCoordinate){
                pcinfo.cityCoordinate = "117.23355, 31.827258";
            }
            if(debug){
            	pcinfo.cityPinyin=debugCity;
            }
            YW.ajax({
                type: 'POST',
                url: '/c/user/login',
                data:pcinfo,
                mysuccess: function(data){
                    if(!debug){
                        try{
                            putConfig('domain', domains);
                            putConfig('cityName', cityNames);
                            appendConfig('account', pcinfo.lname);    
                        }catch(e){
                            alert('保存用户信息失败,请检查中介宝安装目录下的写权限设置.或者联系中介宝客服为您解决.');
                        }
                    }
                    window.location="/home.jsp";
                },complete:function(){
                	if(l){
                    	l.stop();
                	}
                },beforeSend:function(){}
            });
            
            
        }
        return false;
    }).on('click', '.menuBtn', function(event) {return false;});
// 添加翻页项
    $(".bodyer").slide({ effect:"left",trigger:"click" });
// 设置页面尺寸
    setDomSize();
// 设置页面参数
    $('#idCity').val(cityNames);
    
// 设置默认显示页面
    // var inTime=setTimeout(function(){
    //     //alert(gotos)
    //     if(gotos==1){
    //         onClickNav(1);
    //     }else{
    //         isFocus();
    //     }
    //     clearTimeout(inTime);
    // },1300);
// 设置默认显示页面
    var inTimes=setTimeout(function(){
        win.width=660;
        clearTimeout(inTimes);
    },10);
// 按钮加载中状态
    try{
        Ladda.bind( 'button', { timeout: 2000 } );
    }catch(e){
        console.log(e);
    }
    isFocus();
});
$(document).on('click', '.act_menu a', function(event) {
    var Thi=$(this),
    ThiP=Thi.parents('.act_menu'),
    ThiPli=ThiP.find('li'),
    ThiPliIndex=Thi.parents('li').index();
    Thi.parent().addClass('curr');
    ThiPli.removeClass('curr');
    if(ThiPliIndex==1 && $('#iframe').attr('src')==''){
        art.dialog({id:'winArtDialog',title:false,
            content:'注册页面加载中...',
            top:'50%',
            time:100
        })
    }
    var inTime=setTimeout(function(){
        if(ThiPliIndex==0){
            isFocus();
        }else if(ThiPliIndex==1){
            if($('#iframe').attr('src')==''){
                $('#iframe').attr('src','iframe_reg.jsp?2&cityPy='+json.city_py+'&cityName='+cityNames+'&domain='+domains);
            }
            $("#iframe").contents().find('#authCode').focus();
        }
        clearTimeout(inTime);
    },600);
});
$(window).resize(function(e) {
    // e.bubbles=false;
    // e.preventDefault();
    // e.stopPropagation();
    //hex.sizeTo(692,660);
    // setDomSize();
// 设置页面尺寸
    setDomSize();
});
function menuerBoxCurr(menuerBoxI,leftNum){
    menuerBoxI.stop().animate({left:leftNum},300);
}
function changeCity(){
    window.location='/welcome.html?pcinfo='+JSON.stringify(pcinfo);
}
$(function(){
var menuerBox=$('.menuer'),
menuerBoxW=menuerBox.innerWidth(),
menuerBoxLeft=menuerBox.position().left,
menuerBoxLW=menuerBox.children('li').width();
menuerBox.append('<div id="menu_animate_block"></div>');
var menuerBoxI=menuerBox.children('#menu_animate_block');
menuerBox.css({'position':'relative'});
menuerBoxI.css({'position':'absolute','left': 0});

menuerBox.on('click', 'a', function(event) {
    var Thi=$(this),
    ThiIndex=Thi.index(),
    ThiPLi=Thi.parents('li'),
    ThiPLiW=ThiPLi.innerWidth(),
    ThiLeft=Thi.position().left;
    menuerBoxI.stop().animate({left: ThiLeft+(ThiPLiW/2-10)},300);
    //menuerBoxCurr(menuerBoxI,ThiLeft+(ThiPLiW/2-10));
})

var menuerBoxOn=menuerBox.find('a').parents('.on'),
menuerBoxOnW=menuerBoxOn.width(),
menuerBoxOnLeft=menuerBoxOn.position().left,
menuerBoxOnI=menuerBoxOn.index();

menuerBoxI.stop().animate({left: menuerBoxOnLeft+(menuerBoxOnW/2-10)+(menuerBoxOnW*menuerBoxOnI)},300);

// addTitlebar("top-titlebar", "", "");
}); 

function clearCache(){
	try{
		gui.App.clearCache();
		window.location.reload();
	}catch(e){
		
	}
}
</script>
</head>
<body style="overflow: hidden">
<div class="html login">
    <div class="header titlebar">
        <div class="wintool nobar">
            <ul class="wintools">
                <li class="dropdown">
                    <a href="" class="winBtn winBtnText" data-toggle="dropdown">在线客服</a>
                    <ul class="dropdown-menu dropdown-menu-right nobar" role="menu">
                        <li><a href="#">客服电话：0551-65314555</a></li>
                        <li class="divider"></li>
                        <li><a href="http://wpa.qq.com/msgrd?v=3&uin=9129588&site=qq&menu=yes" class="btn" data-type="url"><i class="iconfont">&#xe60d;</i> 客服QQ：9129588</a></li>
                        <li><a href="http://wpa.qq.com/msgrd?v=3&uin=912958811&site=qq&menu=yes" class="btn" data-type="url"><i class="iconfont">&#xe60d;</i> 客服QQ：912958811</a></li>
                        <li><a href="http://wpa.qq.com/msgrd?v=3&uin=912958822&site=qq&menu=yes" class="btn" data-type="url"><i class="iconfont">&#xe60d;</i> 客服QQ：912958822</a></li>
                        <li><a href="http://wpa.qq.com/msgrd?v=3&uin=912958833&site=qq&menu=yes" class="btn" data-type="url"><i class="iconfont">&#xe60d;</i> 客服QQ：912958833</a></li>
                        <li class="divider"></li>
                        <!-- <li><a href="#"><i class="iconfont iconfontbold">&#xe633;</i> 在线反馈</a></li> -->
                        <li><a href="http://www.zhongjiebao.com/" class="btn" data-type="url"><i class="iconfont">&#xe60f;</i> 登录官网</a></li>
                    </ul>
                </li>
                <li><a href="javascript:void(0)" close="force" class="winBtn winBtnClose" data-q="close"><i></i></a></li>
            </ul>    
        </div>
    </div>
    <div class="bodyer">
        <ul class="menuer hd clearfix animation act_menu">
            <li><a href="" class="menuBtn">登录</a></li>
            <li><a href="" class="menuBtn">授权</a></li>
            <li><a href="" class="menuBtn">手机</a></li>
            <div id="menu_animate_block"></div>
        </ul>
        <div class="mainer bd animation">
            <ul>
                <li>
                    <div class="row loginBox" >
                        <div class="col-xs-2"></div>
                        <div class="col-xs-8">
                            <form class="form-horizontal" role="form">
                                <div class="form-group">
                                    <label for="idCity" class="col-xs-2 control-label" >城市:</label>
                                    <div class="col-xs-8">
                                        <input type="text" class="form-control" value="" id="idCity" placeholder="" readonly>
                                    </div>
                                    <a class="col-xs-2 btn btn-link" onclick="changeCity()">切换城市</a>
                                </div>
                                <div class="form-group">
                                    <label for="idName" class="col-xs-2 control-label">账号:</label>
                                    <div class="col-xs-8 form_menu_box" id="idNameBox">
                                        <input type="text" class="form-control" id="idName" tabindex="10" placeholder="">
                                        <div class="form_menu_list"></div>
                                    </div>
                                    <a class="col-xs-2 btn btn-link" data-type="wangjizhanghao">忘记帐号？</a>
                                </div>
                                <div class="form-group">
                                    <label for="idPassword" class="col-xs-2 control-label">密码:</label>
                                    <div class="col-xs-8">
                                        <input type="password" class="form-control" id="idPassword" tabindex="11" placeholder="">
                                    </div>
                                    <a class="col-xs-2 btn btn-link" data-type="wangjimima">忘记密码？</a>
                                </div>
                                <div class="form-group"></div>
                                <div class="form-group">
                                    <label class="col-xs-2 control-label"></label>
                                    <div class="col-xs-8">
                                        <button type="button" class="btn btn-primary btn-block ladda-button btn_submit" data-style="expand-left" data-type="submit" tabindex="12"><span class="ladda-label">登　录</span></button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="col-xs-2"></div>
                    </div>
                </li>
            </ul>
            <ul>
                <li>
                    <iframe src="" id="iframe" name="iframe" width="100%" height="100%" scrolling="no" marginheight="0" frameborder="0"></iframe>
                </li>
            </ul>
            <ul>
                <li>
                <center>
                    <img src="/style/images/ad_phone.jpg" usemap="ad_phone" alt="" />
                </center>
                </li>
            </ul>
        </div>
    </div>
    <div class="footer">
        <div style="float:left;cursor:pointer" onclick="clearCache();"> 清理缓存</div><div class="fr">当前版本：5.0</div>
        <div class="fl tipbox"></div>
    </div>
</div>
</body>
</html>