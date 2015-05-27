<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>忘记密码</title>
<meta name="description" content="">
<meta name="keywords" content="">
<meta name="viewport" content="width=device-width,user-scalable=no" />   
<link href="css/style.css" rel="stylesheet">
<!-- <script src="http://libs.baidu.com/jquery/1.11.1/jquery.min.js"></script> -->
<script type="text/javascript" src="js/jquery.js"></script>
<script src="js/layer.m/layer.m.js"></script>

<script type="text/javascript">
function ouTimes(){
    var btn=$('.getcode');
    t=T_s--;
    if(t<=0){
        clearInterval(t);
        btn.removeClass('out').html('获取验证码');
    }else{
        btn.html(t+'s');
    }
}
var T_s=59;
function getcode(tel){
    var btn=$('.getcode'),
    cla='out';
    btn.addClass(cla);
    $.ajax({
      type: 'POST',
      url: '/c/weixin/houseOwner/sendVerifyCode?tel='+tel,
      success: function(data){
        $('#code').focus();
        layer.open({
            content:'验证码已经发送到手机'
        });
      }
    });
    var t;
    t=setInterval("ouTimes()",1000);
}
$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    
    if(ThiType=='getcode'){
        if(Thi.hasClass('out')){ return false;}
        var dom_tel=$('#tel'),
        dom_tel_v=dom_tel.val();
        if(dom_tel_v.length==11){
            getcode(dom_tel_v);
        }else{
            layer.open({
                content:'请输入正确的手机号码',
                btn: ['OK']
            });
        }
    }else if(ThiType=='submit'){
        var dom_tel_v = $('#tel').val();
        var dom_pwd_v = $('#pwd').val();
        var dom_code_v = $('#code').val();
        if(dom_tel_v.length==11&&dom_pwd_v.length>=1&&Thi.hasClass('blue')){
            $.ajax({
                type: 'POST',
                url: '/c/weixin/houseOwner/verifyCode?tel='+dom_tel_v+'&pwd='+dom_pwd_v+"&code="+dom_code_v,
                success: function(data){
                    var exp = new Date();
                    exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
                    document.cookie = "tel=" + dom_tel_v + ";expires=" + exp.toGMTString();
                    window.location = 'houses.jsp';
                },
                error:function(data){
                    var json = JSON.parse(data.responseText);
                    alert(json.msg);
                    layer.open({
                      content:json.msg,
                      btn: ['OK']
                    });
                }
            });
        }else if(dom_tel_v.length==11){
            layer.open({
                content:'请输入正确的手机号码',
                btn: ['OK']
            });
        }else if(dom_pwd_v.length<6){
            layer.open({
                content:'请输入至少6位密码',
                btn: ['OK']
            });
        }
    }
    event.preventDefault();
    /* Act on the event */
}).on('input', '.text', function(event) {
    var tels=$('#tel'),
    pwds=$('#pwd');
    if(tels.val()&&pwds.val()){
        $('#submit').removeClass('gray').addClass('blue');
    }else{
        $('#submit').removeClass('blue').addClass('gray');
    }
    event.preventDefault();
    /* Act on the event */
});
</script>
</head>
<body>
<div class="body wx addtel">
    <div class="wrap PT30">
        <div class="item write">
            <input type="tel" class="text" name="tel" id="tel" value="" placeholder="手机号码">
        </div>
        <div class="item write">
            <input type="password" class="text" name="pwd" id="pwd" value="" placeholder="输入新密码">
        </div>
        <div class="item write">
            <input type="number" class="text w60b" name="code" id="code" value="" placeholder="验证码">
            <a href="#" class="btn blue w40b btn_act getcode " data-type="getcode">获取验证码</a>
        </div>
        <div class="item tipbox hide">
            <div class="cRed"><b>错误提示等</b></div>
        </div>
        <div class="item">
            <a href="#" class="btn gray btn_act MB10" id="submit" data-type="submit">确定</a>
            <a href="login.jsp" class="btn link">取消</a>
        </div>
    </div>
</div>
    
</body>
</html>