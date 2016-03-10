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
    $(document).on('click', '.adboxs', function(event) {
        var Thi=$(this),
        ThiImg=Thi.find('.adboxitem_zf');
        ThiImg.removeClass('fadeInDown').addClass('fadeOutUp');
        Thi.addClass('fadeOut');
        var tms=setTimeout(function(){
           Thi.remove();
        },700);
        event.preventDefault();
    });
</script>
<style type="text/css">
.adboxs { position: absolute; top: 0%; right: 0; bottom: 0%; left: 0; z-index: 999999; text-align: center; background: rgba(0,0,0,0.7); padding-top: 10%; }
.adboxs .adboxitem_zf{ position: relative; margin:0 auto; height: 400px; padding: 30px; background: url(/ad/2016/zufang/ad_year2016bg.jpg); }
.adboxs .adposa{ position: absolute; }
.adboxs .h1{ height: 111px; background: url(/ad/2016/zufang/ad_qiuzu.png) no-repeat top center; margin: 10px auto 20px; ;}
.adboxs .adboxcont{ max-width: 600px; margin: 0 auto; padding-top: 0px; color: #FFE5B8; font-size: 30px; font-family:"microsoft yahei"}
.adboxs .adboxcont p{ padding: 0; margin: 0; }
.adboxs .adboxcont p.PT20{ padding-top: 16px; }
.adboxs .adboxcont p.PT10{ padding-top: 8px; }
.adboxs .adboxcont p.tipss{ padding-top: 10px; font-size: 14px;}
.adboxs .adboxcont p.FS30{ font-size: 30px;}
.adboxs .adboxcont p.FS40{ font-size: 40px;}
.adboxs .adboxcont p span.FS20{ font-size: 20px;}
.adboxs .btn .l {
width: 100px;
height: 55px;
background: red;
position: relative;
}  
.adboxs .btn .l:before {  
content: "";
position: absolute;
top: -25px;
left: 0;
width: 0;
height: 0;
border-left: 50px solid transparent;
border-right: 50px solid transparent;
border-bottom: 25px solid red;
}
.adboxs .btn .l:after {
content: "";
position: absolute;
bottom: -25px;
left: 0;
width: 0;
height: 0;
border-left: 50px solid transparent;
border-right: 50px solid transparent;
border-top: 25px solid red;
}
</style>
</head>
<body>
<div class="adboxs animated">
    <div class="adboxitem_zf animated fadeInDown">
        <div class="adboxcont">
            <h1 class="h1"></h1>
            <p>诚邀广大中介朋友推荐</p>
            <p>“适合开火锅店、400-600平米、</p>
            <p>商业街区沿路或沿巷、非转让”房屋！</p>
            <p class="PT10 FS30">成交立付佣金10000元！</p>
            <p class="PT20">电话：刘先生 15385185555 <span class="FS20">（24小时恭候推荐）</span></p>
        </div>
    </div>
</div>
</body>
</html>