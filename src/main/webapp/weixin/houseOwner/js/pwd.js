$(document).ready(function() {
	var cityPy = getCookie("cityPy");
	var city = getCookie("city");
	$('#city').val(decodeURI(city));
	$('#cityPy').val(cityPy);
});

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
                url: '/c/weixin/houseOwner/verifyCode?tel='+dom_tel_v+'&pwd='+dom_pwd_v+"&code="+dom_code_v+"&cityPy="+$('#cityPy').val(),
                success: function(data){
                    var exp = new Date();
                    exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
                    document.cookie = "tel=" + dom_tel_v + ";expires=" + exp.toGMTString();
                    document.cookie = "city=" + encodeURI($('#city').val()) + ";expires=" + exp.toGMTString()+ "; path=/";
                    document.cookie = "cityPy=" + $('#cityPy').val() + ";expires=" + exp.toGMTString()+ "; path=/";
                    layer.open({
                        content:'请输入正确的手机号码',
                        btn: ['OK'],
                        yes: function(index){
                        	window.location = 'login.jsp';
                        }
                    });
                    
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
    }else if(ThiType=="city"){
    	window.location="citys.jsp";
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

function getCookie(name) { 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
    if(arr=document.cookie.match(reg))
    	return arr[2];
        //return unescape(arr[2]);
    else 
        return ""; 
}