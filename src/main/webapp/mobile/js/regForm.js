





/*
表单验证
 */




/* 错误提示 */
function isFormTip(This,type,text){
    /*
        type:error,
     */
    var ThisTip=This.data('tip'),
    ThisTipErr=This.data('tip-err');
    if(!text){text=ThisTip}
    if(type=='hide'){
        This.removeClass('error');
    }else{
        layer.open({
            content:text,
            style: 'background-color:rgba(0,0,0,0.5); color:#fff; border:none;',
                time: 2,
                shade: false
        });
        This.addClass('error');
    }
}

/* 验证代码
写在表单页面里*/

function isFormRule(This,rule){
    var isTrue=false;
    switch(rule){
    case "username":
        if(This.val()==''){
           isFormTip(This,'error');
        }else if(!This.val().match(/^1[3|4|5|7|8][0-9]\d{8}$/)  && !This.val().match(/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/) ){
            isFormTip(This,'error');
        }else{
            isFormTip(This,'hide');
            isTrue=true;
        }
    break;
    case "password":
        if(This.val()==''){
            isFormTip(This,'error');
        }else{
            isFormTip(This,'hide');
            isTrue=true;
        }
    break;
    case "noNull":
        if(This.val()==''){
            isFormTip(This,'error');
        }else{
            isFormTip(This,'hide');
            isTrue=true;
        }
    break;
    case "checkbox":
        if(!This.prop('checked')){
            isFormTip(This,'error');
        }else{
            isFormTip(This,'hide');
            isTrue=true;
        }
    break;
    default:
        if(!This.val().match(rule)){
            isFormTip(This,'error');
        }else{
            isFormTip(This,'hide');
            isTrue=true;
        }
    }
return isTrue;
}

/* 循环表单 */
function isFormEach(Thi,submit){
    var isTrues=true;
    Thi.find('.isFormItem').each(function(index, el) {
        /*
            data-type:text,checkbox,radio…
            data-from:classname(data-type:from)
        */
        var This=$(this),
        ThisVal=This.val(),
        ThisType=This.attr('type'),
        ThisDrule=This.data('rule'),
        ThisDisadled=This.prop('disabled');
        if(!ThisDisadled){
            if(submit=='submit'){
                var getIsTrue=isFormRule(This,ThisDrule);
                if(!getIsTrue){isTrues=false}
            }
            if(ThisType=='text' || ThisType=='password' ){
                This.on("focus",function(){
                    isFormTip(This,'hide')
                }).on("blur",function(){
                    isFormRule(This,ThisDrule);
                })
            }else if(ThisType=='checkbox'){
                This.on("change",function(){
                    isFormRule(This,ThisDrule);
                })
            }
        }
        //alert(ThisType +'|'+ This.val())
    });
    return isTrues;
}

/* 设置表单 */
/**
 * form 添加.isForm
 * input 循环 isFormItem
 * @return {Boolean} [description]
 */
function isForm(){
    if($('.isForm').length<=0){ return false;}
    var Thi=$('.isForm'),
    ThiDaction=Thi.data('action');
    isFormEach(Thi);
    Thi.on('submit', function(event) {
        var getIsTrues=isFormEach(Thi,'submit');
        return getIsTrues;
        event.preventDefault();
        /* Act on the event */
    });
}


$(document).ready(function() {
    isForm();
});


