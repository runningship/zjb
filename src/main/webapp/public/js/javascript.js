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
/* 登陆与注册功能 */
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
        // $.ajax({
        //   type: 'POST',
        //   url: '?tel='+u+'&p='+p,
        //   success: function(data){
            layer.msg('ok');
        //   }
        // });
    }

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
           url: '/c/weixin/houseOwner/doLogin?tel='+u+'&pwd='+pwd+'&cityPy='+c,
           //url: '?tel='+u+'&pwd='+p+'&code='+c,
           mysuccess: function(data){
            layer.msg('ok');
           }
         });
    }
}

function regCodeFun(){
var form=$('.forms_reg'),
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
        // $.ajax({
        //   type: 'POST',
        //   url: '?tel='+tel,
        //   success: function(data){
            layer.msg('已发');
            c.focus();
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
/* 所有按钮功能 */
$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='login'){
        layer.closeAll();
        layer.open({
            type: 1,
            title:'登陆',
            area: ['320px', '400px'],
            content: $('.loginbox'),
            success: function(layero,index){mh();},
            cancel: function(index){mhs();} 
        });
        $('.forms_login').find('.u').focus();
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
    }else if(ThiType=='submit_login'){
        loginAction();
    }else if(ThiType=='submit_reg'){
        regAction();
    }else if(ThiType=='regCode'){
        regCodeFun();
    }else if(ThiType=='getPwds'){
        layer.msg('找回密码功能');
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
    }else if(ThiType=='SC'){
        if(Thi.hasClass('no')){
            // $.ajax({
            //   type: 'POST',
            //   url: '?tel='+tel,
            //   success: function(data){
                Thi.removeClass('no');
                layer.msg('测试：未收藏→收藏');
            //   }
            // });
        }else{
            // $.ajax({
            //   type: 'POST',
            //   url: '?tel='+tel,
            //   success: function(data){
                Thi.addClass('no');
                layer.msg('测试：收藏→未收藏');
            //   }
            // });
        }
    }
    event.preventDefault();
    /* Act on the event */
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
    autoComplete($('#nope'));
});
/* house */
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
    $(document).find('body').prepend('<div id="autoCompleteBox" class="autocomplete"></div>');
    var Thi=id,
    oldVal,ThiMaxLen=0,ThiCurrIndex=-1,
    ThiWidth=Thi.innerWidth(),
    ThiHeight=Thi.innerHeight()+1,
    ThiOptTop=Thi.offset().top+ThiHeight,
    ThiOptLeft=Thi.offset().left,
    autocomplete=$('#autoCompleteBox');
    // autocomplete.width(ThiWidth).css({'top':ThiOptTop+ThiHeight,'left':ThiOptLeft});
    autocomplete.css({'top':ThiOptTop+ThiHeight,'left':ThiOptLeft});
    autocomplete.on('click','a',function(event) {
       var This=$(this),
       ThisIndex=This.index(),
       ThisArea=This.attr('area'),
       ThisAddress=This.data('address'),
       ThisQuyu=This.data('quyu');
       setSearchValue(ThisIndex);
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
