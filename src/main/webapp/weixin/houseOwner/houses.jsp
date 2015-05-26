<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
//     layer.open({
//         content:'编号：'+ThiId,
//         btn: ['OK']
//     });
	window.location='edit.jsp?id='+ThiId;
    event.preventDefault();
});

</script>
</head>
<body>
<div class="body wx list">
    <div class="wrap">
        <ul class="item ul_list">
        <c:forEach items="${houses}" var="house">
        	<li data-id="${house.id}" >
                <div class="ibox">
                    <span class="hname">${house.area } ${house.dhao }#${house.fhao }</span>
                    <span class=" w25b">${house.quyu }</span>
                </div>
                <div class="ibox">
                    <span class=" w25b">${house.zjia }万</span>
                    <span class="mji w25b">${house.mji } m<sup>2</sup></span>
                    <span class="lceng w25b">${house.lceng }/${house.zceng }层</span>
                    <span class=" w25b">${house.hxf }室${house.hxt }厅${house.hxw }卫</span>
                </div>
            </li>
        </c:forEach>
            
        </ul>
    </div>
    <div class="wrap footer">
		<c:if test="${houses.size()<3 }">
        <a href="add.jsp" class="btn border tal w40b btn_act getcode " data-type="getcode"><i class="iconfont">&#xe600;</i> 添加新房源</a>
        </c:if>
        <a href="houses.jsp" class="" >刷新</a>
        <a href="/c/weixin/houseOwner/logout" class="btn border tal w20b btn_act " >退出</a>
        <a href="citys.jsp" class="btn border tal w20b btn_act " >${city }</a>
    </div>
</div>
    
</body>
</html>