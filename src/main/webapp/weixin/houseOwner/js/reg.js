/**
 * reg
 * @authors shaishai
 * @date    2015-05-15 16:28:25
 */


$(document).ready(function() {
	var cityPy = getCookie("cityPy");
	var city = getCookie("city");
	$('#city').val(decodeURI(city));
	$('#cityPy').val(cityPy);
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
function getcode(tel){
    var btn=$('.getcode'),
    cla='out';
    btn.addClass(cla);
    YW.ajax({
      type: 'POST',
      url: '/c/weixin/houseOwner/sendVerifyCode?tel='+tel,
      mysuccess: function(data){
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
        var dom_tel=$('#tel'),
        dom_pwd=$('#pwd'),
        dom_code=$('#code'),
        dom_tel_v=dom_tel.val(),
        dom_pwd_v=dom_pwd.val(),
        dom_code_v=dom_code.val();
        if(dom_pwd_v.length<6){
        	layer.open({
                content:'密码至少六位数字字母或字符',
                btn: ['OK']
            });
        	event.preventDefault();
        	return;
        }
        if(dom_code_v.length<=0){
        	layer.open({
                content:'请输验证码',
                btn: ['OK']
            });
        	event.preventDefault();
        	return;
        }
        if(!$('#cityPy').val()){
        	layer.open({
                content:'请先选择城市',
                btn: ['OK']
            });
        	event.preventDefault();
        	return;
        }
        if(dom_tel_v.length==11&&Thi.hasClass('blue')){
            YW.ajax({
              type: 'POST',
              url: '/c/weixin/houseOwner/verifyCode?tel='+dom_tel_v+'&pwd='+dom_pwd_v+'&code='+dom_code_v+"&cityPy="+$('#cityPy').val(),
              mysuccess: function(data){
                  var exp = new Date();
                  exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
                  document.cookie = "tel=" + dom_tel_v + ";expires=" + exp.toGMTString()+ "; path=/";
                  document.cookie = "city=" + encodeURI($('#city').val()) + ";expires=" + exp.toGMTString()+ "; path=/";
                  document.cookie = "cityPy=" + $('#cityPy').val() + ";expires=" + exp.toGMTString()+ "; path=/";
                  layer.open({
                      content:'注册成功',
                      btn: ['OK']
                  });
              },
              error:function(data){
                 
              }
            });
        }else{
            layer.open({
                content:'请输入正确的手机号码',
                btn: ['OK']
            });
        }
    }else if(ThiType=="city"){
    	window.location="citys.jsp";
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

function getCookie(name) { 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
        return arr[2]; 
    else 
        return ""; 
}