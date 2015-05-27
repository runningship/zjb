<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>修改房源</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />   
<link href="css/style.css" rel="stylesheet">
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript" src="js/buildHtml.js"></script>
<script src="js/check.js" type="text/javascript"></script>
<script src="js/layer.m/layer.m.js"></script>
<script>
function update(){
    exists(function(){
        var a=$('form[name=form1]').serialize();
        YW.ajax({
        type: 'POST',
        url: '/c/weixin/houseOwner/updateHouse',
        data:a,
        mysuccess: function(data){
            alert('发布成功');
            window.location='houses.jsp';
            }
        })
    });
}

function deleteHouse(){
    YW.ajax({
    type: 'POST',
    url: '/c/weixin/houseOwner/deleteHouse?id=${house.id}',
    mysuccess: function(data){
        window.location='houses.jsp';
        }
    })
}

$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='submit'){
    	update();
    }else if (ThiType=='del') {
        deleteHouse();
    }
    event.preventDefault();
    /* Act on the event */
});
</script>
</head>
<body>
<div class="body wx content">
    <div class="wrap">
        <form name="form1" role="form" >
        <input type="hidden" name="id" value="${house.id }"/>
        <ul class="item ul_list">
            <li class="dblock">
                <span class="title">楼盘：</span>
                <span class="inputbox"><input type="text" class="text" value="${house.area }" desc="楼盘名称" name="area" placeholder="楼盘名称"></span>
            </li>
            
            <li class="dblock">
                <span class="title">地址：</span>
                <span class="inputbox"><input type="text" class="text"  name="address" value="${house.address }" desc="楼盘地址" placeholder="楼盘地址"></span>
            </li>
            <li>
                <span class="title">栋号：</span>
                <span class="inputbox"><input type="text isFormItem" class="text"  name="dhao" value="${house.dhao }" desc="栋号" placeholder="栋号"></span>
            </li>
            <li>
                <span class="title">房号：</span>
                <span class="inputbox"><input class="text" name="fhao" value="${house.fhao }" desc="房号" placeholder="房号"></span>
            </li>
            <li>
                <span class="title">楼层：</span>
                <span class="inputbox"><input type="number" class="text" name="lceng" value="${house.lceng }" desc="楼层" placeholder="楼层"></span>
            </li>
            <li>
                <span class="title">总层：</span>
                <span class="inputbox"><input type="number" class="text" name="zceng" value="${house.zceng }" desc="总层" placeholder="总层"></span>
            </li>
            <li>
                <span class="title">面积：</span>
                <span class="inputbox"><input type="number" class="text"  name="mji" value="<fmt:formatNumber type="number" value="${house.mji }" maxFractionDigits="2"></fmt:formatNumber>"  desc="面积"/><b>㎡</b></span>
            </li>
            <li>
                <span class="title">总价：</span>
                <span class="inputbox"><input type="number" class="text" name="zjia" value="<fmt:formatNumber type="number" value="${house.zjia }" maxFractionDigits="2"></fmt:formatNumber>"  desc="总价"/><b>万元</b></span>
            </li>
            <li>
                <span class="title">年代：</span>
                <span class="inputbox"><input type="number" min="1900" max="2015" desc="建筑年代" class="text" value="${house.dateyear }"  name="dateyear" placeholder=""><b>年</b></span>
            </li>
            <li>
            </li>
            <li>
                <span class="title">装潢：</span>
                <span class="inputbox">
                    <select class="select" name="zxiu" id="zxiu" type="tabs" tabindex="10">
                    <c:forEach items="${zxius}" var="zxiu">
                        <option value="${zxiu.name}"<c:if test="${house.zxiu eq zxiu.name}"> selected="selected"</c:if>>${zxiu.name}</option>
                        </c:forEach>
                    </select>
                </span>
            </li>
            <li>
                <span class="title">楼型：</span>
                <span class="inputbox">
                    <select class="select" name="lxing" id="lxing" type="tabs" tabindex="10">
                    <c:forEach items="${lxings}" var="lxing">
                        <option value="${lxing.name}"<c:if test="${house.lxing eq lxing.name}">selected="selected"</c:if>>${lxing.name}</option>
                        </c:forEach>
                    </select></span>
            </li>
            <li>
                <span class="title">户型：</span>
                <span class="inputbox">
                    <select class="select" name="hxing" id="hxing" type="tabs" tabindex="10">
                    <c:forEach items="${hxings}" var="hxing">
                        <option value="${hxing.name}"<c:if test="${fxing eq hxing.name}">selected="selected"</c:if>>${hxing.name}</option>
                        </c:forEach>
                    </select>
                    </span>
            </li>
            <li>
                <span class="title">区域：</span>
                <span class="inputbox">
                    <select class="select" name="quyu" id="quyu" type="tabs" tabindex="10">
                    <c:forEach items="${quyus}" var="quyu">
                        <option value="${quyu.name}"<c:if test="${house.quyu eq quyu.name}"> selected="selected"</c:if>>${quyu.name}</option>
                        </c:forEach>
                    </select>
                </span>
            </li>
            
            <li class="dblock">
                <span class="title">备注：</span>
                <span class="inputbox"><textarea rows="4" name="beizhu" style="width:100%;">${house.beizhu }</textarea></span>
            </li>
        </ul>
        </form>
    </div>
    <div class="wrap footer">
        <a href="#" class="btn blue btn_act getcode " data-type="submit" >更新</a>
    </div>
    <div class="wrap footer">
        <a href="#" class="btn red btn_act getcode " data-type="del" >删除</a>
    </div>
</div>
    
</body>
</html>