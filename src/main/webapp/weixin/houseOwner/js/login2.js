/**
 * reg
 * @authors shaishai
 * @date    2015-05-15 16:28:25
 */


$(document).ready(function() {
    
});
function setTips(str){
    $('.tipbox').html(str).removeClass('hide');
}
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
function getcode(){
    var btn=$('.getcode'),
    cla='out';
    btn.addClass(cla);
    layer.open({
        content:'验证码已经发送到手机',
        btn: ['OK']
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
            /*$.post('path/i.html', param1:dom_tel_v, function(data, textStatus, xhr) {
                ...
            });*/
            getcode();
        }else{
            layer.open({
                content:'请输入正确的手机号码',
                btn: ['OK']
            });
        }
    }else if(ThiType=='submit'){
        var dom_tel=$('#tel'),
        dom_code=$('#code'),
        dom_tel_v=dom_tel.val(),
        dom_code_v=dom_code.val();
        if(dom_tel_v.length==11&&dom_code_v.length==4&&Thi.hasClass('blue')){
            layer.open({
                content:'登录确认',
                btn: ['OK']
            });
        }else{
            layer.open({
                content:'请输入正确的手机号码',
                btn: ['OK']
            });
        }
    }
    event.preventDefault();
    /* Act on the event */
}).on('click', '.text', function(event) {
    $('.tipbox').addClass('hide');
    event.preventDefault();
    /* Act on the event */
}).on('input', '.text', function(event) {
    var tels=$('#tel'),
    pwds=$('#pwd'),
    code=$('#code');
    if(tels.val()&&pwds.val()&&code.val()){
        $('#submit').removeClass('gray').addClass('blue');
    }else{
        $('#submit').removeClass('blue').addClass('gray');
    }
    event.preventDefault();
    /* Act on the event */
});