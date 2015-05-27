<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title >我的房源</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />

<meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />

<link href="css/iconfont/iconfont.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script src="js/layer.m/layer.m.js"></script>
<script src="js/iscroll-probe.js"></script>
<style>
.scroller {
    position: absolute;
    z-index: 1;
    -webkit-tap-highlight-color: rgba(0,0,0,0);
    width: 100%;
    -webkit-transform: translateZ(0);
    -moz-transform: translateZ(0);
    -ms-transform: translateZ(0);
    -o-transform: translateZ(0);
    transform: translateZ(0);
    -webkit-touch-callout: none;
    -webkit-user-select: none;
    -moz-user-select: none;
    -ms-user-select: none;
    user-select: none;
    -webkit-text-size-adjust: none;
    -moz-text-size-adjust: none;
    -ms-text-size-adjust: none;
    -o-text-size-adjust: none;
    text-size-adjust: none;
}
#pullDown{ color: #FFF;}
</style>
<script type="text/javascript">
var myScroll;  
var pullDownEl, pullDownL;  
var pullUpEl, pullUpL;  
var Downcount = 0 ,Upcount = 0;  
var loadingStep = 0;
function pullDownAction() {
    location.reload(true);
}

function loaded() {  
    pullDownEl = $('#pullDown');  
    pullDownL = pullDownEl.find('.pullDownLabel');  
    pullDownEl['class'] = pullDownEl.attr('class');  
    pullDownEl.attr('class','').hide();   
      
    myScroll = new IScroll('#wrapper', {  
        probeType: 2,
        scrollbars: true,
        mouseWheel: true,
        fadeScrollbars: true,
        bounce:true, 
        interactiveScrollbars:true,
        shrinkScrollbars:'scale',
        click: true ,
        keyBindings:true,
        momentum:true
    });
    myScroll.on('scroll', function(){  
        if(loadingStep == 0 && !pullDownEl.attr('class').match('flip|loading')){  
        if (this.y > 5) {
            pullDownEl.attr('class','item')  
            pullDownEl.show();  
            myScroll.refresh();  
            pullDownEl.addClass('flip');  
            pullDownL.html('准备刷新...');  
            loadingStep = 1;  
        } 
        }  
    });
    myScroll.on('scrollEnd',function(){  
        if(loadingStep == 1){  
            if(pullDownEl.attr('class').match('flip|loading')){  
                pullDownEl.removeClass('flip').addClass('loading');  
                pullDownL.html('Loading...');  
                loadingStep = 2;  
                pullDownAction();  
            }
        }  
    });  
}  


document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);
$(document).ready(function() {
    loaded();
});

</script>
<script>
$(document).ready(function() {
    
});
$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='loginput'){
        clearCookie();
        window.location="/c/weixin/houseOwner/logout";
    }
}).on('click', '.ul_list li', function(event) {
    var Thi=$(this),
    ThiId=Thi.data('id');
    if(ThiId=='add'){
        window.location='add.jsp';
    }else{
        window.location='edit.jsp?id='+ThiId;
    }
	
    event.preventDefault();
})

$(document).on('click', '.fMenu', function(event) {
    event.stopPropagation();  
    var fMenu=$(this),
    fMenuT=fMenu.offset().top,
    fMenuL=fMenu.offset().left,
    fMenuW=fMenu.width(),
    fMenuId=fMenu.data('menuid'),
    fMenuBox=$('#'+fMenuId),
    fMenuBoxH=fMenuBox.height(),
    fMenuBoxW=fMenuBox.width();
    //alert(fMenuT +'|'+ fMenuL+'|'+ fMenuW +'|'+ )
    if(fMenuBox.length>0){
        var ThiTop=fMenuT-fMenuBoxH-10,ThiLeft;
        if((fMenuL+fMenuBoxW)>$('.body').width()){
            ThiLeft=$('.body').width()-fMenuBoxW-10;
        }else{
            ThiLeft=fMenuL+(fMenuW/2)-(fMenuBoxW/2);
        }
        fMenuBox.show().css({'top':ThiTop,'left':ThiLeft})
    }
    event.preventDefault();
    /* Act on the event */
});
$('body,html').on('click', function(event){
    if($('.fMenuBox').css('display')!='none'){
        $('.fMenuBox').hide()
    }
});

function getCookie(name) 
{ 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
 
    if(arr=document.cookie.match(reg))
        return unescape(arr[2]); 
    else 
        return null; 
} 

function delCookie(name) 
{ 
    var exp = new Date(); 
    exp.setTime(exp.getTime() - 1); 
    var cval=getCookie(name); 
    if(cval!=null){
    	document.cookie= name + "="+cval+";expires="+exp.toGMTString()+";path=/";
    	document.cookie= name + "="+cval+";expires="+exp.toGMTString();
    }
}
function clearCookie(){
	delCookie("city");
	delCookie("tel");
	//delCookie("house_owner_city");
}

function quit(){
	clearCookie();
	window.location="/c/weixin/houseOwner/logout";
}
</script>
</head>
<body>
<div class="body wx list">
    <div class="wrap mainer" id="wrapper">
        <div class="scroller">
        <div class="item" id="pullDown">
            <div class="pullDownIcon"></div>  
            <div class="pullDownLabel">下拉刷新</div> 
        </div>
        <ul class="item ul_list "id="thelist">
        <c:if test="${houses.size()<3 }">
            <li data-id="add" class="green">
                <div class="ibox">
                    <i class="icon iconfont">&#xe610;</i> 添加新房源
                </div>
            </li>
        </c:if>
        <c:forEach items="${houses}" var="house">
        	<li data-id="${house.id}" >
                <div class="ibox">
                    <span class="hname">${house.area } ${house.dhao }#${house.fhao }</span>
                    <span class=" w25b">${house.quyu }</span>
                </div>
                <div class="ibox">
                    <span class=" w25b"><fmt:formatNumber type="number" value="${house.zjia }" maxFractionDigits="2"></fmt:formatNumber>万</span>
                    <span class="mji w25b"><fmt:formatNumber type="number" value="${house.mji }" maxFractionDigits="2"></fmt:formatNumber> ㎡</sup></span>
                    <span class="lceng w25b">${house.lceng }/${house.zceng }层</span>
<%--                     <span class=" w25b">${house.hxf }室${house.hxt }厅${house.hxw }卫</span> --%>
                </div>
            </li>
        </c:forEach>
            
        </ul>
        <c:if test="${houses.size()>=3 }">
        <div class="item cGray">
            　<i class="iconfont">&#xe614;</i> 您只能添加3条数据。
         </div>
        </c:if>
        </div>
    </div>
    <div class="wrap footer table">
        <div class="tr">
            <a href="" class="btn border tal td "></a>
            <a href="#" class="btn border td fMenu icoMenu" data-menuid="caidan"><i class="icon iconfont">&#xe605;</i></a>
        </div>
    </div>
</div>
<div class="fMenuBox" id="caidan">
    <ul>
        <li><a href="citys.jsp">${city }[切换]</a></li>
        <li><a href="#" class="btn_act" data-type="loginput">退出</a></li>
    </ul>
</div>
    
</body>
</html>