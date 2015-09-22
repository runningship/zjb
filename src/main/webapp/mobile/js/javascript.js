/**
 * 
 * @authors yimixia.com
 * @date    2015-08-28 10:22:14
 * @version $Id$
 */

/* old */
function msgs(a){
     layer.msg(a);
     return false;
}

/* form mi style */
$(document).ready(function() {
    $(document).find('.formItem .input').focusin(function(){
        $(this).parents('.formItem').addClass('active').addClass('focus');
    }).focusout(function(){
        if($(this).is('select')){
            if($(this).is(":selected")){
                $(this).parents('.formItem').removeClass('active');
            }
        }else{
            if(!$(this).val()){
                $(this).parents('.formItem').removeClass('active');
            }
        }
        $(this).parents('.formItem').removeClass('focus').removeClass('curr');
    }).hover(function() {
        $(this).parents('.formItem').addClass('hover');
    }, function() {
        $(this).parents('.formItem').removeClass('hover');
    }).each(function(index, el) {
        if($(this).is('select')){
            $(this).parents('.formItem').addClass('curr');
        }
    });
});

/* form checkbox style*/
$(document).ready(function() {
    $(document).on('change','.formItem .checkbox',function(){
        var Thi=$(this),
        ThiP=Thi.parents('.formItem'),
        hasCheck=Thi.prop('checked'),
        ThiType=Thi.attr('type');
        if(hasCheck){
            ThiP.addClass('active');
            if(ThiType=='radio'){
                ThiP.parent().siblings().find('.formItem').removeClass('active').find('input.checkbox').prop("checked", false);
            }
        }else{
            ThiP.removeClass('active');
        }
    }).find('.formItem.check').each(function(index, el) {
        var Thi=$(this),
        checkbox=Thi.find('input.checkbox'),
        hasCheck=checkbox.attr('checked'),
        ThiType=checkbox.attr('type');
        if(hasCheck==="checked"){
            Thi.addClass('active');
            if(ThiType=='radio'){
                Thi.parent().siblings().find($(this).attr('class')).removeClass('active');
            }
        }else{
            Thi.removeClass('active');
        }
    });
});

/* onclick list */
$(document).ready(function() {
    if($('.onclick').length){
        var This=$('.onclick'),
        ThiON=This.data('onclick');
        $(document).on('click', ThiON, function(event) {
            var Thi=$(this),
            ThiId=Thi.data('id');
            layer.msg(ThiId);
            window.location.href="houseSee.html?id="+ThiId;
            event.preventDefault();
            /* Act on the event */
        });
    }
});


/* nav hover */
    
$(document).ready(function() {
$('.Ha .Hb').hover(function(){
    var Thi=$(this),
    ThiP=Thi.parents('.Ha');
    Thi.addClass('hover').siblings().removeClass('hover');
},function(){
    var Thi=$(this),
    ThiP=Thi.parents('.Ha');
    Thi.removeClass('hover');
})
});



function fixedbakcground(){
    var scrollTop=$(window).scrollTop();
    var activebg=$('.activebg');
    var activebgArr=[];

    $(activebg).each(function(i, item) {
        if (i === 0) {
            activebgArr[i] = $(item).offset().top - Math.floor($(item).height() / 2) - 500
        } else {
            activebgArr[i] = $(item).offset().top - Math.floor($(item).height() / 2)
        }
    });
    $(window).scroll(function(e){
        scrollTop = $(window).scrollTop();
        $(activebgArr).each(function(i, item) {
            
        });
    });
}

function ScrollGoto(a){
    if(a.length>0){
        var ThiTop=a.offset().top;
        $('.mainer').animate({'scrollTop': ThiTop},500, function(){});
    }
}














