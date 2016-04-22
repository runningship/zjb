<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<link href="/ad/2016/animate.min.css" rel="stylesheet">
<script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script>
<script type="text/javascript">
    $(document).on('click', '.regPhone .btn_act', function(event) {
        var Thi=$(this),
        Type=Thi.attr('data-type');
        if(Type=='b1'){
            $('.regPhone .nav1').show();
            $('.regPhone .nav2').hide();
            return false;
        }else if(Type=='b2'){
            $('.regPhone .nav2').show();
            $('.regPhone .nav1').hide();
            return false;
        }else if(Type=='nav1'){
            openReg();
        }else if(Type=='nav2'){
            editProfile();
        }else if(Type=='b3'){
        }else{}
        event.preventDefault();
    }).on('click', '.adboxs.regPhone', function(event) {
        var Thi=$(this),
        ThiImg=Thi.find('img');
        ThiImg.removeClass('fadeInDown').addClass('flipOutX');
        Thi.addClass('fadeOut')
        var tms=setTimeout(function(){
            Thi.remove();
        },750);
        event.preventDefault();
    });
</script>
<style type="text/css">
.adboxs.regPhone { position: absolute; top: 0; right: 0; bottom: 0; left: 0; text-align: center; background: rgba(0,0,0,0.75);z-index: 999999;}
.adboxs.regPhone .adboxitem{ margin:10% auto 0; position: relative; width: 980px;}
.adboxs.regPhone .ab{ position: absolute; bottom: 29px; display: block; height: 46px; width: 150px; background: rgba(0,0,0,0.0); }
.adboxs.regPhone .ab1{ right: 390px; width: 240px; }
.adboxs.regPhone .ab2{ right: 221px; width: 168px; }
.adboxs.regPhone .ab3{ right: 90px; width: 130px; }
.adboxs.regPhone .dn{ display: none;}
.adboxs.regPhone .nav1{ top: -2px; right: 90px; height: auto; width: auto; bottom: auto; display: none; z-index: 1000;}
.adboxs.regPhone .nav2{ top: -2px; right: 90px; height: auto; width: auto; bottom: auto; display: none; z-index: 1000;}
</style>

<div class="adboxs regPhone animated">
    <div class="adboxitem">
        <img src="/ad/2016/reg/ad_regPhone.png" alt="" class=" animated fadeIn">
        <a href="" class="ab ab1 btn_act" data-type="b1"></a>
        <a href="" class="ab ab2 btn_act" data-type="b2"></a>
        <a href="" class="ab ab3 btn_act" data-type="b3"></a>
    </div>
    <a href="" class="ab nav1 btn_act" data-type="nav1" ><img src="/ad/2016/reg/ad_regPhone_n1.png" alt="" class=""></a>
    <a href="" class="ab nav2 btn_act" data-type="nav2" ><img src="/ad/2016/reg/ad_regPhone_n2.png" alt="" class=""></a>
</div>