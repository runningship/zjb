/**
 * reg
 * @authors shaishai
 * @date    2015-05-15 16:28:25
 */

function setTips(str){
    $('.tipbox').html(str).removeClass('hide');
}
$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='submit'){
        var dom_tel=$('#tel'),
        dom_pwd=$('#pwd'),
        dom_tel_v=dom_tel.val(),
        dom_pwd_v=dom_pwd.val();
        if(dom_tel_v.length==11&&dom_pwd_v.length>=1&&Thi.hasClass('blue')){
            $.ajax({
              type: 'POST',
              url: '/c/weixin/houseOwner/doLogin?tel='+dom_tel_v+'&pwd='+dom_pwd_v,
              success: function(data){
                  var exp = new Date();
                  exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
                  document.cookie = "tel=" + dom_tel_v + ";expires=" + exp.toGMTString();
                  window.location = 'houses.jsp';
              },
              error:function(data){
                  var json = JSON.parse(data.responseText);
                  //alert(json.msg);
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
        }else if(dom_pwd_v.length>=1){
            layer.open({
                content:'请输入密码',
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
    pwds=$('#pwd');
    if(tels.val()&&pwds.val()){
        $('#submit').removeClass('gray').addClass('blue');
    }else{
        $('#submit').removeClass('blue').addClass('gray');
    }
    event.preventDefault();
    /* Act on the event */
});