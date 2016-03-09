<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Examples</title>
<meta name="description" content="">
<meta name="keywords" content="">
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript">
    $(document).on('click', '.btn_act', function(event) {
        var Thi=$(this),
        Type=Thi.attr('data-type');
        if(Type=='b1'){
            editProfile();
            return false;
        }else if(Type=='b2'){
            openReg();
            return false;
        }else if(Type=='b3'){
        }else{}
        event.preventDefault();
    }).on('click', '.adboxs', function(event) {
        var Thi=$(this),
        ThiImg=Thi.find('img');
        ThiImg.removeClass('fadeInDown').addClass('fadeOutUp');
        Thi.addClass('fadeOut')
        var tms=setTimeout(function(){
            Thi.remove();
        },750);
        event.preventDefault();
    });
</script>
<style type="text/css">
.adboxs { position: absolute; top: 0; right: 0; bottom: 0; left: 0; text-align: center; background: rgba(0,0,0,0.75);z-index: 999999;}
.adboxs .adboxitem_reg{ margin:10% auto 0; position: relative; width: 980px;}
.ab{ position: absolute; bottom: 29px; display: block; height: 46px; width: 150px; }
.ab1{ right: 430px; width: 200px; }
.ab2{ right: 221px; width: 208px; }
.ab3{ right: 90px; width: 130px; }
</style>
</head>
<body>
<div class="adboxs animated">
    <div class="adboxitem_reg">
        <img src="/ad/2016/reg/ad_regPhone.png" alt="" class=" animated fadeIn">
        <a href="" class="ab ab1 btn_act" data-type="b1"></a>
        <a href="" class="ab ab2 btn_act" data-type="b2"></a>
        <a href="" class="ab ab3 btn_act" data-type="b3"></a>
    </div>
</div>
</body>
</html>