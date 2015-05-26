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
<script>
$(document).ready(function() {
    
});
$(document).on('click', '.ul_list li', function(event) {
    var Thi=$(this),
    ThiId=Thi.data('id');
    if(ThiId=='add'){
        window.location='add.jsp';
    }else{
        window.location='edit.jsp?id='+ThiId;
    }
	
    event.preventDefault();
});

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
</script>
</head>
<body>
<div class="body wx list">
    <div class="wrap">
        <ul class="item ul_list">
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
                    <span class=" w25b">${house.hxf }室${house.hxt }厅${house.hxw }卫</span>
                </div>
            </li>
        </c:forEach>
            
        </ul>
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
        <li><a href="citys.jsp">${city }</a></li>
        <li><a href="houses.jsp">刷新</a></li>
        <li><a href="/c/weixin/houseOwner/logout">退出</a></li>
    </ul>
</div>
    
</body>
</html>