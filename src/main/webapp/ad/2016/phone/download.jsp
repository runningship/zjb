<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
body{ }
.adboxs.ad_phone { position: absolute; top: 0%; right: 0; bottom: 0; left: 0; text-align: center; background:rgba(0,0,0,0.5);}
.adboxs.ad_phone .adboxitem{ margin:0 auto; width: 400px; height: 550px; padding-top: 100px; position: relative;}

.ad_phone .lineBox{ position: relative; display: inline-block;text-decoration: none;
font-size: 15px;color: #33ab6a;font-weight: bold;text-align: center;
width: 180px;height: 180px;line-height: 180px;
border: 2px solid rgba(255,255,255,.0); 
-webkit-box-sizing: border-box;
-moz-box-sizing: border-box;
box-sizing: border-box;
-webkit-transition: 0.4s;-o-transition: 0.4s;transition: 0.4s;
}
.ad_phone .lineBox img{ display: block; margin: 13px 0 0 13px; }
.ad_phone .lineBox .line{ position: absolute; display: inline-block; background-color: #FFF;
-webkit-transition: 1.5s ease;-o-transition: 1.5s ease;transition: 1.5s ease;}
.ad_phone .lineBox .line_top{ height: 2px;width: 0;left: -50%;top: -2px;}
.ad_phone .lineBox .line_right{ height: 0;width: 2px;top: -50%;right: -2px;}
.ad_phone .lineBox .line_bottom{ width: 2px;height: 0;bottom: -50%;left: -2px;}
.ad_phone .lineBox .line_left{ height: 2px;width: 0;right: -50%;bottom: -2px;}
.ad_phone .lineBox:hover .line_top{width:100%;left:-0px;}
.ad_phone .lineBox:hover .line_right{height:100%;top:-0px;}
.ad_phone .lineBox:hover .line_bottom{height:100%;bottom:-0px;}
.ad_phone .lineBox:hover .line_left{width:100%;right:-0px;}


.adboxs.ad_phone { font-family: "microsoft yahei","黑体";}
.ad_phone h3{ font-size: 14px; color: #FFF; font-weight: normal; margin-bottom: 20px; }
.ad_phone h2{ font-size: 16px; color: #FFF; font-weight: normal;opacity:0; margin-top: 30px; margin-bottom: 20px; }
.ad_phone .fontBox{ width: 70%; margin: 0 auto; text-align: left; padding:0 20px; opacity: 0; }
.ad_phone .br{border-top: 1px solid #FFF; }

.ad_phone .posa{ position: absolute; }
.ad_phone .posaFK{ bottom: 10px; right: 10px; text-decoration: none; color:rgba(255,255,255,0.7); opacity: 0; }

/*
<animation-name>
<animation-duration>
<animation-timing-function>
<animation-delay>
<animation-iteration-count>
<animation-direction>

-webkit-animation-name: 'round'; //动画名称
-webkit-animation-duration: 60s; //播放一次所持续时间
-webkit-animation-timing-function: ease;
//动画播放频率
ease：（逐渐变慢）默认值
linear：（匀速）
ease-in：(加速)
ease-out：（减速）
ease-in-out：（加速然后减速）
-webkit-animation-iteration-count: infinite;
//数字为次数，动画播放次涒为无限次
animation-delay:0; //动画何时执行
animation-direction:alternate;//反向循环

232, 84, 84,1
 */
.ad_phone .adbox_ani1{
background: rgb(65, 235, 219);box-shadow: 2px 10px 20px rgba(0,0,0,0.5);
-webkit-animation-name: 'adbox_ani1';
-webkit-animation-duration: 3.5s;
-webkit-animation-timing-function: ease;
-webkit-animation-iteration-count: 1;
animation-delay:0s;
}

.ad_phone .lineBox .line_top{
-webkit-animation-name: 'line_top';
-webkit-animation-duration: 3.5s;
-webkit-animation-timing-function: ease;
-webkit-animation-iteration-count: 1;
animation-delay:0;
}
.ad_phone .lineBox .line_right{
-webkit-animation-name: 'line_right';
-webkit-animation-duration: 3.5s;
-webkit-animation-timing-function: ease;
-webkit-animation-iteration-count: 1;
animation-delay:0;
}
.ad_phone .lineBox .line_bottom{
-webkit-animation-name: 'line_bottom';
-webkit-animation-duration: 3.5s;
-webkit-animation-timing-function: ease;
-webkit-animation-iteration-count: 1;
animation-delay:0;
}
.ad_phone .lineBox .line_left{
-webkit-animation-name: 'line_left';
-webkit-animation-duration: 3.5s;
-webkit-animation-timing-function: ease;
-webkit-animation-iteration-count: 1;
animation-delay:0;
}

.ad_phone .lineBox{
-webkit-animation-name: 'lineBox1';
-webkit-animation-duration: 1.5s;
-webkit-animation-timing-function: ease;
-webkit-animation-iteration-count: 1;
animation-delay:1.5s;

}
.ad_phone .lineBox img{
-webkit-animation-name: 'img1';
-webkit-animation-duration: 1s;
-webkit-animation-timing-function: ease;
-webkit-animation-iteration-count: 1;
animation-delay:2.0s;
}
.ad_phone .lineBox.ani2{
-webkit-animation-name: 'lineBox2';
-webkit-animation-duration: 1.5s;
-webkit-animation-timing-function: linear;
-webkit-animation-iteration-count: infinite;
animation-direction:alternate;
}

@-webkit-keyframes 'adbox_ani1' {
    0%{background: rgba(65, 235, 219,0);box-shadow: 2px 10px 20px rgba(0,0,0,0.0)}
    50% {background: rgba(65, 235, 219,1);box-shadow: 2px 10px 20px rgba(0,0,0,0.0)}
    100% {background: rgba(65, 235, 219,1);box-shadow: 2px 10px 20px rgba(0,0,0,0.5)}
}

@-webkit-keyframes 'line_top' {
    0%{left: -50%;top: -2px;}
    50% {width:100%;left:-0px;}
    100% {width:100%;left:-0px; opacity: 0}
}
@-webkit-keyframes 'line_right' {
    0%{top: -50%;right: -2px;}
    50% {height:100%;top:-0px;}
    100% {height:100%;top:-0px; opacity: 0}
}
@-webkit-keyframes 'line_bottom' {
    0%{bottom: -50%;left: -2px;}
    50% {height:100%;bottom:-0px;}
    100% {height:100%;bottom:-0px; opacity: 0}
}
@-webkit-keyframes 'line_left' {
    0%{right: -50%;bottom: -2px;}
    50% {width:100%;right:-0px;}
    90% {width:100%;right:-0px;}
    100% {width:100%;right:-0px; opacity: 0}
}

@-webkit-keyframes 'lineBox2' {
    0%{ }
    100% { border-color: #FFF; }
}

@-webkit-keyframes 'lineBox1' {
    0%{background:rgba(255,255,255,0);}
    50% {background:rgba(255,255,255,1); border-color: #FFF;}
    99% {background:rgba(255,255,255,0);}
    100% {background:#3F3F3F;}
}
@-webkit-keyframes 'img1' {
    0% { opacity: 0;}
    100% { opacity: 1;}
}
</style>


<script type="text/javascript">
$(document).on('click', '.adboxs.ad_phone', function(event) {
    var Thi=$(this),
    ThiImg=Thi.find('img');
        ThiImg.removeClass('fadeInDown').addClass('fadeOutUp');
    var tms=setTimeout(function(){
        Thi.hide();
    },700);
    event.preventDefault();
});
$(document).on('click','.ad_phone .posaFK', function(event) {
	fankui();
    event.preventDefault();
});

$(document).ready(function() {
var time_adboxitem=setTimeout(function(){
    $('.adboxitem').addClass('adbox_ani1');
    clearTimeout(time_adboxitem);
},1000);
var time_saoma=setTimeout(function(){
    $('.saoma').addClass('zoomIn');
    clearTimeout(time_saoma);
},0);
var time_saoma2=setTimeout(function(){
    $('.saoma').removeClass('zoomIn').addClass('swing');
    clearTimeout(time_saoma2);
},1000);
var time_shenbian=setTimeout(function(){
    $('.shenbian').css({'opacity':1}).addClass('fadeInUp');
    clearTimeout(time_shenbian);
},3000);
var time_fontBox=setTimeout(function(){
    $('.fontBox').animate({'opacity':1}, 1500,function(){$(this).animate({'opacity':0},1000)});
    $('.posaFK').animate({'opacity':1},500)
    clearTimeout(time_fontBox);
},2000);
var time_ani2=setTimeout(function(){
    $('.lineBox').addClass('ani2');
    clearTimeout(time_ani2);
},2900);
});
</script>

<div class="adboxs ad_phone"  id="ad_phone" style="display:none; z-index: 9999999;">
    <div class="adboxitem">
        <h3 class="saoma animated">扫码下载APP客户端</h3>
        <div class="lineBox">
            <img src="/ad/img/zjb/zjb_all_150_b.png" alt="" class=" animated fadeInDown">
            <span class="line line_top"></span>
            <span class="line line_right"></span>
            <span class="line line_bottom"></span>
            <span class="line line_left"></span>
        </div>
        <h2 class="shenbian animated">春天了，动起来——中介宝</h2>
        <div class="fontBox">
            <p class="br"></p>
        </div>
            <a href="#" class="btn white posa posaFK">意见问题反馈</a>
    </div>
</div>