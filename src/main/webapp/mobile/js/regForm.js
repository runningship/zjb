





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

function ImgAutoBox(img){
    function imgAct(){
        var ThiW=Thi.width(),
        ThiH=Thi.height(),
        isW='W',
        ThiP=Thi.parent(),
        ThiPW=ThiP.width(),
        ThiPH=ThiP.height();
        if(ThiH>ThiW){isW='H'}
        if(isW=='W'){
            Thi.css({
                'height': ThiPH,
                'width': 'auto'
                
            });
            ThiWn=(Thi.width()-ThiPW)/2;
            Thi.css({'margin-left':-ThiWn})
        }else{
            Thi.css({
                'width': ThiPW,
                'height': 'auto'
            });
            ThiHn=(Thi.height()-ThiPH)/2;
            Thi.css({'margin-top':-ThiHn})
        }
        ThiP.css('overflow', 'hidden');
    }
    var Thi=img;
    var img = new Image(); 
    img.src =Thi.attr("src");
    if(img.complete){
        imgAct();
    }else{
        img.onload = function(){
            imgAct();
        }
    }
}
function ImgListBox(a){
    if(!a){a=$('.ImgListBox')}
    var Thi=a,
    ThiLi=Thi.children('li');
    ThiLi.each(function(index, el) {
        var This=$(this),
        ThiIMG=This.find('img');
        ThiW=This.width();
        This.height(ThiW);
        This.find('a.jia').css({
            'height': ThiW+'px',
            'line-height':ThiW+'px'
        });
        ImgAutoBox(ThiIMG);
    });

}

function addPics(){
	api.actionSheet({
        buttons:['拍照' , '相册']
    },function(ret,err){
    	//
       if(ret.buttonIndex==1){
    	   api.getPicture({
    		    sourceType: 'camera',
    		    encodingType: 'jpg',
    		    mediaValue: 'pic',
    		    destinationType: 'url',
    		    allowEdit: false,
    		    quality: 100,
    		    targetWidth:900,
    		    targetHeight:900,
    		    saveToPhotoAlbum: true
    		}, function(ret, err){ 
    		    //blockAlert(JSON.stringify(ret));
    		    if(ret.data!=""){
    		    	imgArr+=ret.data+";"
        	    	thumbArr+=ret.data+";"
        	    	//添加到页面显示
        	    	var ImgListBox = $('.ImgListBox');
        	    	ImgListBox.prepend('<li class="houseImage" path="'+ret.data+'" thumb="'+ret.data+'" onclick="showBigImage()"><a href="#" class=""><img style="" src="'+ret.data+'" alt=""></a></li>');
    		    }
    		    
    		});
       }else if(ret.buttonIndex==2){
    	   var obj = api.require('UIMediaScanner');
    		obj.open({
    		    type:'picture',
    		    column: 4,
    		    classify: true,
    		    max: 4,
    		    sort: {
    		        key: 'time',
    		        order: 'desc'
    		    },
    		    texts: {
    		        stateText: '已选择*项',
    		        cancelText: '取消',
    		        finishText: '完成'
    		    },
    		    styles: {
    		        bg: '#fff',
    		        mark: {
    		            icon: '',
    		            position: 'bottom_left',
    		            size: 20
    		        },
    		        nav: {
    		            bg: '#eee',
    		            stateColor: '#000',
    		            stateSize: 18,
    		            cancelBg: 'rgba(0,0,0,0)',
    		            cancelColor: '#000',
    		            cancelSize: 18,
    		            finishBg: 'rgba(0,0,0,0)',
    		            finishColor: '#000',
    		            finishSize: 18
    		        }
    		    }
    		}, function(ret){
    		    if(ret){
    		    	var ImgListBox = $('.ImgListBox');
    		        for(var i=ret.list.length-1;i>=0;i--){
    		        	imgArr+=ret.list[i].path+";"
    		        	thumbArr+=ret.list[i].thumbPath+";"
    		        	//添加到页面显示
    		        	ImgListBox.prepend('<li class="houseImage" path="'+ret.list[i].path+'" thumb="'+ret.list[i].thumbPath+'" onclick="showBigImage()"><a href="#" class=""><img style="" src="'+ret.list[i].thumbPath+'" alt=""></a></li>');
    		        }
    		    }
    		});
       }
    });
	
}

function showBigImage(){
	//sm 
    var obj = api.require('imageBrowser');
    var tmp = imgArr.split(';');
    var arr = [];
    for(var i=0;i<tmp.length;i++){
    	if(tmp[i]==""){
    		continue;
    	}
    	arr.push(tmp[i]);
    }
    obj.openImages({
        imageUrls: arr,
        showList:false
    });
}

