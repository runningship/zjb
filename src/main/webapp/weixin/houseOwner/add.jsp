<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>发布房源</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />   
<link href="css/style.css" rel="stylesheet">
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<!-- <script src="../../js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script> -->
<!-- <script src="../../js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script> -->
<script type="text/javascript" src="js/buildHtml.js"></script>
<script src="js/check.js?12" type="text/javascript"></script>
<script src="js/layer.m/layer.m.js"></script>
<script src="js/textareaAutoHeight.js"></script>
<script src="js/regForm.js"></script>
<script>
function save(){
    exists(function(){
        var a=$('form[name=form1]').serialize();
        YW.ajax({
        type: 'POST',
        url: '/c/weixin/houseOwner/addHouse',
        data:a,
        mysuccess: function(data){
            alert('发布成功');
            setTimeout(function(){window.location='houses.jsp';},2000)
            }
        });
    });
}

$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='submit'){
        save();
    }
    event.preventDefault();
    /* Act on the event */
});

$(document).ready(function() {
    $('textarea').tah({
        moreSpace:15,
        maxHeight:600,
        animateDur:200
    });
});
</script>
</head>
<body>
<div class="body wx content">
    <div class="wrap">
        <div class="cGray item">
            <a href="houses.jsp" class="btn link w20b fr">取消</a>
            <span class="h2">添加新的房源 <span class="hTip"></span></span>
        </div>
        <form name="form1" role="form" onsubmit="save();">
        <ul class="item ul_list">
            <li class="dblock">
                <span class="title">楼盘：</span>
                <span class="inputbox"><input type="text" class="text" id="area" name="area"  desc="楼盘名称" placeholder="楼盘名称" data-rule="noNull" data-tip="请输入楼盘名称" data-tipErr="请输入楼盘名称"></span>
            </li>
            
            <li class="dblock">
                <span class="title">地址：</span>
                <span class="inputbox"><input type="text" class="text isFormItem" value="" name="address" desc="楼盘地址" placeholder="楼盘地址"></span>
            </li>
            <li>
                <span class="title">栋号：</span>
                <span class="inputbox"><input class="text isFormItem" id="dhao" name="dhao" value="" desc="栋号" placeholder="栋号" data-rule="noNull" data-tip="请输入栋号" data-tipErr="请输入栋号"></span>
            </li>
            <li>
                <span class="title">房号：</span>
                <span class="inputbox"><input class="text isFormItem" id="fhao" name="fhao" value="" desc="房号" placeholder="房号" data-rule="noNull" data-tip="请输入房号" data-tipErr="请输入房号"></span>
            </li>
            <li>
                <span class="title">楼层：</span>
                <span class="inputbox"><input type="number" class="text isFormItem" value="" name="lceng" desc="楼层" placeholder="楼层" data-rule="noNull" data-tip="请输入楼层" data-tipErr="请输入楼层"></span>
            </li>
            <li>
                <span class="title"> </span>
                <span class="inputbox">
                	<input type="number" class="text " name="zceng" desc="总层" placeholder="总层" />
                </span>
                
            </li>
            <li>
                <span class="title">面积：</span>
                <span class="inputbox"><input type="number" class="text isFormItem" value="" name="mji" desc="面积" placeholder="" data-rule="noNull" data-tip="请输入面积" data-tipErr="请输入面积"><b>㎡</b></span>
            </li>
            <li>
                <span class="title">总价：</span>
                <span class="inputbox"><input type="number" class="text isFormItem" value="" name="zjia" desc="总价" placeholder="" data-rule="noNull" data-tip="请输入总价" data-tipErr="请输入总价"><b>万元</b></span>
            </li>
            <li>
                <span class="title">年代：</span>
                <span class="inputbox"><input type="number"  desc="建筑年代" min="1900" max="2015" class="text isFormItem" value="" name="dateyear" placeholder="" data-rule="noNull" data-tip="请输入年代" data-tipErr="请输入年代"><b>年</b></span>
            </li>
            <li>
            </li>
            <li>
                <span class="title">装潢：</span>
                <span class="inputbox">
                    <select class="select" name="zxiu" id="zxiu" type="tabs" tabindex="10">
                    <c:forEach items="${zxius}" var="zxiu">
                        <option value="${zxiu.name}">${zxiu.name}</option>
                        </c:forEach>
                    </select>
                </span>
            </li>
            <li>
                <span class="title">楼型：</span>
                <span class="inputbox">
                    <select class="select" name="lxing" id="lxing" type="tabs" tabindex="10">
                    <c:forEach items="${lxings}" var="lxing">
                        <option value="${lxing.name}">${lxing.name}</option>
                        </c:forEach>
                    </select></span>
            </li>
            <li>
                <span class="title">户型：</span>
                <span class="inputbox">
                    <select class="select" name="hxing" id="hxing" type="tabs" tabindex="10">
                    <c:forEach items="${hxings}" var="hxing">
                        <option value="${hxing.name}">${hxing.name}</option>
                        </c:forEach>
                    </select>
                </span>
            </li>
            <li>
                <span class="title">区域：</span>
                <span class="inputbox">
                    <select class="select" name="quyu" id="quyu" type="tabs" tabindex="10">
                    <c:forEach items="${quyus}" var="quyu">
                        <option value="${quyu.name}">${quyu.name}</option>
                        </c:forEach>
                    </select>
                </span>
            </li>
            
            <li class="dblock">
                <span class="title">备注：</span>
                <span class="inputbox">
                    <textarea name="" class="text isFormItem" name="beizhu" placeholder="">&nbsp;</textarea>
                </span>
            </li>
        </ul>
    </form>
    </div>
    <div class="wrap footer">
        <a href="#" class="btn blue btn_act " data-type="submit"><span>发布</span></a>
    </div>
</div>
    
</body>
</html>