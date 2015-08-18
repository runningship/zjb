/**
 * 
 * @authors Your Name (you@example.org)
 * @date    2015-08-11 09:30:30
 * @version $Id$
 */

function mh(){
  $('.bodyer').addClass('mh');
}
function mhs(){
  $('.bodyer').removeClass('mh');
}
function resizes(){
    var b=$('.bodyer'),
    h=$('.header'),
    m=$('.mainer'),
    f=$('.footer'),
    bh=b.innerHeight(),
    hh=h.innerHeight(),
    fh=f.innerHeight(),
    mhs=bh-hh-fh;
    m.height(mhs).css({'top':hh+'px'});
}
$(window).resizeEnd({
    delay : 100
}, function() {
    resizes();
})
/* 判断手机号 */
function isPhone(s){
//var patrn=/^(13[0-9]|15[0|3|5|6|7|8|9]|18[8|9])\d{8}$/;
var patrn=/^1\d{10}$/;
if (!patrn.exec(s)) return false;
return true;
}
/* 登录与注册功能 */
function loginAction(){
var form=$('.forms_login'),
    u=form.find('.u'),
    p=form.find('.p'),
    uv=u.val(),
    pv=p.val();
    if(!uv||!isPhone(uv)){
        u.focus();
        layer.msg('请输入正确的手机号');
        return false;
    }else if(!pv){
        p.focus();
        layer.msg('请输入密码');
        return false;
    }else{
    	YW.ajax({
            type: 'POST',
            url: '/c/weixin/houseOwner/doLogin?tel='+uv+'&pwd='+pv+'&cityPy='+$('#currentCity').attr('py'),
            mysuccess: function(data){
                var exp = new Date();
                exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
                document.cookie = "tel=" + uv + ";expires=" + exp.toGMTString()+ "; path=/";
                window.location.reload();
            }
        });
    	
    }

}
function logoutAction(){
	YW.ajax({
        type: 'POST',
        url: '/c/weixin/houseOwner/logoutWZJB',
        mysuccess: function(data){
            window.location.reload();
        }
    });
}
function openLoginWindow(){
    layer.closeAll();
    layer.open({
        type: 1,
        title:'登录',
        area: ['320px', '300px'],
        content: $('.loginbox'),
        success: function(layero,index){mh();},
        cancel: function(index){mhs();} 
    });
    $('.forms_login').find('.u').focus();
}

function regAction(){
var form=$('.forms_reg'),
    u=form.find('.u'),
    c=form.find('.c'),
    p=form.find('.p'),
    p2=form.find('.p2'),
    uv=u.val(),
    cv=c.val(),
    pv=p.val(),
    p2v=p2.val();
    if(!isPhone(uv)){
        u.focus();
        layer.msg('请输入正确的手机号');
        return false;
    }else if(!cv){
        c.focus();
        layer.msg('请输入手机收到的验证码');
        return false;
    }else if(pv.length<5){
        p.focus();
        layer.msg('请输入不小于5位密码');
        return false;
    }else if(p2v!=pv){
        p2.focus();
        layer.msg('请重复输入密码');
        return false;
    }else{
    	 YW.ajax({
             type: 'POST',
             url: '/c/weixin/houseOwner/verifyCode?tel='+uv+'&pwd='+pv+'&cityPy='+$('#currentCity').attr('py')+'&code='+cv,
             mysuccess: function(data){
                 var exp = new Date();
                 exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
                 document.cookie = "tel=" + uv + ";expires=" + exp.toGMTString()+ "; path=/";
                 layer.open({
                     content:'注册成功',
                     btn: ['OK']
                 	//回调函数刷新页面
                 });
             }
           });
    }
}

function regCodeFun(formClass){
var form=$('.'+formClass),
    u=form.find('.u'),
    c=form.find('.c'),
    cBtn=form.find('.code'),
    cla='out';
    if(cBtn.hasClass(cla)){
        c.focus();
        return false;
    }else if(!isPhone(u.val())){
        u.focus();
        layer.msg('请输入正确的手机号');
        return false;
    }else{
        cBtn.addClass(cla);
        YW.ajax({
            type: 'POST',
            url: '/c/weixin/houseOwner/sendVerifyCode?tel='+u.val(),
            mysuccess: function(data){
            	layer.msg('已发');
                c.focus();
            }
          });
        
        var T_s=59,t;
            t=setInterval(function(){
                var ts=T_s--;
                if(ts<=0){
                    clearInterval(t);
                    cBtn.removeClass('out').html('获取验证码');
                }else{
                    cBtn.html(' '+ts+' 秒后重新获取');
                }
            },1000);
        //   }
        // });
    }
}

/* getPwd*/
function resetPwd(){
    var form=$('.forms_pwds'),
    u=form.find('.u'),
    c=form.find('.c'),
    p=form.find('.p'),
    uv=u.val(),
    cv=c.val(),
    pv=p.val();
     if(!isPhone(uv)){
        u.focus();
        layer.msg('请输入正确的手机号');
        return false;
    }else if(!cv){
        c.focus();
        layer.msg('请输入手机收到的验证码');
        return false;
    }else if(pv.length<5){
        p.focus();
        layer.msg('请输入不小于5位密码');
        return false;
    }
    YW.ajax({
        type: 'POST',
        url: '/c/weixin/houseOwner/verifyCode?tel='+uv+'&pwd='+pv+'&cityPy='+$('#currentCity').attr('py')+'&code='+cv,
        mysuccess: function(data){
            var exp = new Date();
            exp.setTime(exp.getTime() + 1000*3600*24*365);//过期时间一年 
            document.cookie = "tel=" + uv + ";expires=" + exp.toGMTString()+ "; path=/";
            layer.open({
                content:'密码重置成功',
                btn: ['OK']
            });
        }
      });
}
/* 所有按钮功能 */
$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='login'){
    	openLoginWindow();
    }else if(ThiType=='reg'){
        layer.closeAll();
        layer.open({
            type: 1,
            title:'注册',
            area: ['320px', '400px'],
            content: $('.regbox'),
            success: function(layero,index){mh();},
            cancel: function(index){mhs();} 
        });
        $('.forms_reg').find('.u').focus();
    }else if(ThiType=='getPwds'){
        layer.closeAll();
        layer.open({
            type: 1,
            title:'找回密码',
            area: ['320px', '400px'],
            content: $('.getPwdbox'),
            success: function(layero,index){mh();},
            cancel: function(index){mhs();} 
        });
        $('.forms_pwds').find('.u').focus();
    }else if(ThiType=='submit_login'){
        loginAction();
    }else if(ThiType=='submit_reg'){
        regAction();
    }else if(ThiType=='regCode'){
        regCodeFun('forms_reg');
    }else if(ThiType=='reset_pwd_code'){
    	regCodeFun('forms_pwds');
    }else if(ThiType=='logout'){
    	logoutAction();
    }else if(ThiType=='empty'){
    	var action = $('#action').val();
    	$('input[type=text]').val('');
    	$('input[type=checkbox]').attr("checked" , false);
    	$('#action').val(action);
    	$('.submit').click();
    }else if(ThiType=='reset_pwd'){
    	resetPwd();
    }else if(ThiType=='SwitchCity'){
        layer.closeAll();
        layer.open({
            type: 1,
            title:'选择城市',
            area: ['400px', '360px'],
            content: $('.citybox'),
            success: function(layero,index){mh();},
            cancel: function(index){mhs();} 
        });
    }else if(ThiType=='seeMyHouse'){
        layer.msg('查看我的房源')

    }else if(ThiType=='addHouse'){
        layer.open({
            type: 2,
            title:'添加房源',
            area: ['610px', '480px'],
            fix: false, //不固定
            maxmin: false,
            content: 'chushou_add.jsp'
        });
    }else if(ThiType=='editHouse'){
        var hid=$(this).parents('tr').data('hid');
        layer.open({
            type: 2,
            title:'修改房源',
            area: ['610px', '480px'],
            fix: false, //不固定
            maxmin: false,
            content: 'chushou_edit.jsp?hid='+hid
        });
    }else if(ThiType=='delHouse'){
        var tr=$(this).parents('tr'),hid=tr.data('hid');
        layer.confirm('确定删除房源？', {icon: 3,
            btn: ['删除','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	HouseDelete(Thi);
            layer.msg('已删除', {icon: 1});
        }, function(){});
    }else if(ThiType=='addRent'){
        layer.open({
            type: 2,
            title:'添加租房',
            area: ['610px', '480px'],
            fix: false, //不固定
            maxmin: false,
            content: 'chuzu_add.jsp'
        });
    }else if(ThiType=='editRent'){
        var hid=$(this).parents('tr').data('hid');
        layer.open({
            type: 2,
            title:'修改租房',
            area: ['610px', '480px'],
            fix: false, //不固定
            maxmin: false,
            content: 'chuzu_edit.jsp?hid='+hid
        });
    }else if(ThiType=='delRent'){
        layer.confirm('确定删除租房？', {icon: 3,
            btn: ['删除','取消'], //按钮
            shade: false //不显示遮罩
        }, function(){
        	RentDelete(Thi);
            layer.msg('已删除', {icon: 1});
        }, function(){});
    }else if(ThiType=='submit_add'){
        if($('.addsubmit').length>0){
            $('.addsubmit').click();
        }
    }else if(ThiType=='SC'){
    	//判断用户是否在线
    	if(!Thi.attr('uid')){
    		//弹出登录窗口
    		openLoginWindow();
    		return;
    	}
    	
        if(Thi.hasClass('no')){
        	YW.ajax({
                type: 'POST',
                url: '/c/weixin/houseOwner/toggleFav?hid='+Thi.attr('hid')+'&cuzu='+Thi.attr('cuzu'),
                mysuccess: function(data){
                	Thi.removeClass('no');
                    layer.msg('已收藏');
                }
            });
        }else{
        	YW.ajax({
                type: 'POST',
                url: '/c/weixin/houseOwner/toggleFav?hid='+Thi.attr('hid')+'&cuzu='+Thi.attr('cuzu'),
                mysuccess: function(data){
                	Thi.addClass('no');
                    layer.msg('取消收藏');
                }
            });
        }
    }else if(ThiType=='submit'){
        if($('.submit').length>0){
            $('.submit').click();
        }
    }
    event.preventDefault();
    /* Act on the event */
});
/* 屏蔽按键 */

$(document).keydown(function(event){  
//alert('a'+event.keyCode)
    if ((event.altKey)&&   
        ((event.keyCode==37)||   //屏蔽 Alt+ 方向键 ←   →
        (event.keyCode==39))){   
        event.returnValue=false;   
        return false;  
    }   
    if((event.ctrlKey)&&(event.keyCode==13)){   //
      if($('.submit').length>0){
        $('.submit').click();
      }
    }  
}); 
$(document).on('keyup','[data-keySubmit="true"]', function(event) {
    if(event.keyCode==13){
        var Thi=$(this),
        ThiTo=Thi.data('keysubmitto');
        if($(ThiTo).length>0){
            $(ThiTo).click();
        }
    }
});

/* 鼠标经过 */
function mouseHover(){
var st,Thi;

$('.HA .HB').hover(function(){
    clearTimeout(st);
    var Thi=$(this);
    //alert(Thi.find('.HC').length)
    Thi.siblings().find('.HC').hide();
    Thi.siblings().removeClass('active');
    Thi.addClass('active');
    Thi.find('.HC').show();
},function(){
    var Thi=$(this);
    st=setTimeout(function(){
        Thi.find('.HC').hide();
        Thi.removeClass('active');
    },500);
});
$('.HAs').hover(function(){
    clearTimeout(st);
    var Thi=$(this);
    //alert(Thi.find('.HC').length)
    ///Thi.siblings().find('.HC').hide();
    Thi.siblings().removeClass('active');
    Thi.addClass('active');
    //Thi.find('.HC').show();
},function(){
    var Thi=$(this);
    st=setTimeout(function(){
        //Thi.find('.HC').hide();
        Thi.removeClass('active');
    },500);
});
}

// 列表标题与内容宽度保持一致
function tableFix(TableH,TableB){
    TableB.width('100%')
    for(var i=0;i<=TableB.find('td:last').index();i++){
        //console.log('td:'+TableB.find('tr').eq(1).find('td').eq(i).text()+'tdw:'+TableB.find('tr').eq(1).find('td').eq(i).width()+'')
        TableH.find('th').eq(i).width(TableB.find('tr').eq(1).find('td').eq(i).width());
    }
}
function setTableFix(){
    var TableH=$('.TableH'),
    TableB=$('.TableB')
    if(TableH&&TableB){
        tableFix(TableH,TableB);
    }
}
// 列表经过
function tableHover(){
var st;
$(document).find('.table-hover').find('tr').hover(function(){
    clearTimeout(st);
    var Thi=$(this);
        Thi.addClass('hover').siblings().removeClass('hover');
    st=setTimeout(function(){
        Thi.find('.HC').show();
        clearTimeout(st);
    },100);
},function(){
    var Thi=$(this);
    clearTimeout(st);
    st=setTimeout(function(){
        Thi.find('.HC').hide();
        Thi.removeClass('hover');
        clearTimeout(st);
    },500);
});
}
// 
function tableClick(){
    $(document).find('.table-hover').on('click', 'tr', function(event) {
        $(this).parents('table').find('tr').removeClass('cursor');
        $(this).addClass('cursor').addClass('curr');
    });
}
/* search fun*/
function searchFun(){
var st,Thi;
    $('.searchItem').hover(function(){
        clearTimeout(st);
        Thi=$(this);
        Thi.siblings().removeClass('hover');
        Thi.addClass('hover');
    },function(){
        Thi=$(this);
        st=setTimeout(function(){
            Thi.removeClass('hover');
        },500);
    });
    $('.searchItem label').hover(function(){
        Thi=$(this);
        Thi.siblings().removeClass('hover');
        Thi.addClass('hover');
    },function(){
        Thi.removeClass('hover');
    });
}
$(document).ready(function() {
    resizes();
    setTableFix();
    mouseHover();
    tableClick();
    tableHover();
    searchFun();
    // layer.open({
    //     type: 1,
    //     title:'注册',
    //     area: ['320px', '400px'], 
    //     content: $('.regbox')
    // });
    //autoComplete($('#nope'));
});


/* form-active  form mi style */
$(document).ready(function() {
    $(document).find('.form-active').find('.input').focusin(function(){
        $(this).parent().addClass('active').addClass('focus');
    }).focusout(function(){
        if($(this).is('select')){
            if($(this).is(":selected")){
                $(this).parent().removeClass('active');
            }
        }else{
            if(!$(this).val()){
                $(this).parent().removeClass('active');
            }
        }
        $(this).parent().removeClass('focus').removeClass('curr');
    }).hover(function() {
        $(this).parent().addClass('hover');
    }, function() {
        $(this).parent().removeClass('hover');
    }).each(function(index, el) {
        if($(this).is('select')){
            if($(this).val()){
                $(this).parent().addClass('active');
            }else{
                $(this).parent().addClass('curr');
            }
        }else if($(this).is('textarea')){
            if($(this).val()){
                $(this).parent().addClass('active');
            }
        }else if($(this).is('input')){
            if($(this).val()){
                $(this).parent().addClass('active');
            }
        }
    });
});


/* house */
  function setSearchValue(index){
      var ThiA=$('#autoCompleteBox').find('a');
      ThiA.removeClass('hover');
      var Vals=ThiA.eq(index).addClass('hover').attr('title');
      $('#nope').val(Vals);
  }

/**
 * 添加 autoComplete 功能
 * autoComplete($('#input的class或id'))
 * 需在页面script中添加以下段落
  function setSearchValue(index){
      var ThiA=$('#autoCompleteBox').find('a');
      ThiA.removeClass('hover');
      var Vals=ThiA.eq(index).addClass('hover').attr('title');
      $('#search').val(Vals);
  }
 * setSearchValue(当前选中的行)
 */
function autoComplete(id){
    if(id.length<1){return false;}
    $(document).find('body').prepend('<div id="autoCompleteBox" class="autocomplete"></div>');
    var Thi=id,
    oldVal,ThiMaxLen=0,ThiCurrIndex=-1,
    ThiWidth=Thi.innerWidth(),
    ThiHeight=Thi.innerHeight()+1,
    ThiOptTop=Thi.offset().top+ThiHeight,
    ThiOptLeft=Thi.offset().left,
    autocomplete=$('#autoCompleteBox');
    // autocomplete.width(0);
    // autocomplete.width(ThiWidth).css({'top':ThiOptTop+ThiHeight,'left':ThiOptLeft});
    autocomplete.css({'top':ThiOptTop+ThiHeight,'left':ThiOptLeft});
    autocomplete.on('click','a',function(event) {
       var This=$(this),
       ThisIndex=This.index(),
       ThisArea=This.attr('area'),
       ThisAddress=This.data('address'),
       ThisQuyu=This.data('quyu');
       setSearchValue(ThisIndex,true);
       return false;
    });
    Thi.on('keydown',function(event) {
    }).on('keyup',function(event) {
        var This=$(this),
        ThisVal=This.val(),
        param={search:ThisVal};
        ThiOptTop=Thi.offset().top+ThiHeight+3,
        ThiOptLeft=Thi.offset().left,
        autocomplete.css({'top':ThiOptTop,'left':ThiOptLeft});
        //alert(event.keyCode)
        if(event.keyCode=='13'){
            autocomplete.hide();
            return false;
        }else if(event.keyCode=='27'){
            autocomplete.hide();
            return false;
        }else if(event.keyCode=='38'){
            if(ThiCurrIndex<=0){
                ThiCurrIndex=0;
            }else{
                ThiCurrIndex--;
            }
            if(ThiCurrIndex<$('#autoCompleteBox').find('a').length-13){
              $('#autoCompleteBox').scrollTop($('#autoCompleteBox').scrollTop()-26);
            }
            setSearchValue(ThiCurrIndex);
            //alert(ThiCurrIndex+'|'+ThiMaxLen+'|'+autocomplete.find('a').eq(ThiCurrIndex).attr('title'))
            return false;
        }else if(event.keyCode=='40'){
            if(ThiCurrIndex>=ThiMaxLen-1){
                ThiCurrIndex=ThiMaxLen-1;
            }else{
                ThiCurrIndex++;
            }
            if(ThiCurrIndex>12){
              $('#autoCompleteBox').scrollTop($('#autoCompleteBox').scrollTop()+26);
            }
            setSearchValue(ThiCurrIndex);
            //alert(ThiCurrIndex+'|'+ThiMaxLen+'|'+autocomplete.find('a').eq(ThiCurrIndex).attr('title'))
            return false;
        }
        if(oldVal!=ThisVal){
            oldVal=ThisVal;
            YW.ajax({
                type: 'POST',
                url: '/c/areas/prompt',
                data:param,
                success: function(data){
                    var d=JSON.parse(data);
                    autocomplete.html('');
                    $.each(d['houses'], function(index, val) {
                        autocomplete.prepend('<a href="#" area="'+val.area+'" title="'+val.address+'" data-address="'+val.address+'" data-quyu="'+val.quyu+'" ><i>'+val.quyu+'</i><b>'+val.area+'</b>'+val.address+'</a>');
                    });
                    ThiMaxLen=d['houses'].length;
                    ThiCurrIndex=-1;
                    if(ThiMaxLen>0){
                        autocomplete.show()
                    }else{
                        autocomplete.hide()
                    }
                },complete:function(){
                    
                },beforeSend:function(){}
            });
        }
    }).on('focusin',function(event) {
        if(autocomplete.html()){autocomplete.show();}
    }).on('focusout',function(event) {
        ThiCurrIndex=0;
        var autocompleteTime=setTimeout(function(){
            autocomplete.hide();
            clearTimeout(autocompleteTime);
        },200);
    });
}