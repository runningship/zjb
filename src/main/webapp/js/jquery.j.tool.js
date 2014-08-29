/**
 * All Javascript
 * @authors YiMiXia.com (7687849@qq.com)
 * @date    2014-08-06 11:02:12
 * @version zjb 5.0
 */
// JavaScript Document
//$(document).bind("contextmenu", function () { return false; });
//$(document).bind("selectstart", function () { return false; });
/*
*/
//屏蔽右键菜单
// document.oncontextmenu = function (event){
// 	if(window.event){
// 		event = window.event;
// 	}try{
// 		var the = event.srcElement;
// 		if (!((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || the.tagName == "TEXTAREA")){
// 			return false;
// 		}
// 		return true;
// 	}catch (e){
// 		return false; 
// 	} 
// }
//屏蔽全选
document.onselectstart = function (event){
	if(window.event){
		event = window.event;
	}try{
		var the = event.srcElement;
		if (!((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || the.tagName == "TEXTAREA" || the.className == "neirong" || the.className == "telNum" || the.className == "onselect")){
			//alert(the.getAttribute("selectstart"))
			return false;
		}
		return true;
	}catch (e){
		return false; 
	} 
}
$(document).keydown(function(event){  
//alert('a'+event.keyCode)
    if ((event.altKey)&&   
      ((event.keyCode==37)||   //屏蔽 Alt+ 方向键 ←   
       (event.keyCode==39)))   //屏蔽 Alt+ 方向键 →   
   {   
       event.returnValue=false;   
       return false;  
   }   
    if(event.keyCode==8){   //
      if(window.event){
        event = window.event;
      }try{
        var the = event.srcElement;
        if (!((the.tagName == "INPUT" && (the.type.toLowerCase() == "text" || the.type.toLowerCase() == "password")) || the.tagName == "TEXTAREA" || the.className == "neirong" || the.className == "telNum" || the.className == "onselect")){
          //alert(the.getAttribute("selectstart"))
          return false;
        }
        return true;
      }catch (e){
        return false; 
      }   
    }  
    if(event.keyCode==9){  
      if(window.event){
        event = window.event;
      }try{
        var the = event.srcElement;
        if (the.tagName.toLowerCase() == "select" || the.tagName.toLowerCase() == "textarea" || the.tagName.toLowerCase() == "input"){
          return true;
        }
        return false;
      }catch (e){
        return false; 
      }  
    }  
    if(event.keyCode=="116"){  
        return false; //屏蔽F5刷新键   
    }  
    if((event.ctrlKey) && (event.keyCode==82)){  
        return false; //屏蔽alt+R   
    }  
}); 

//窗口调用的公共函数
try{
var gui = require('nw.gui');
var win = gui.Window.get();
var shell = gui.Shell;
var winMaxHeight,winMaxWidth;
}catch (e){}
function WinMaxOrRev(a){
  if(a=='0'){
    winMaxNone='none';
    winRevertNone='inline-block';
  }else{
    winMaxNone='inline-block';
    winRevertNone='none';
  }
  if($('.winBtnMax').length>0){
    $('.winBtnMax').css('display',winMaxNone);
  }
  if($('.winBtnRevert').length>0){
    $('.winBtnRevert').css('display',winRevertNone);
  }
}
function WinMin(){/*最小化*/
	win.minimize();
}
function WinMax(){/*最大化*/
  if(process.platform === 'win32' && parseFloat(require('os').release(), 10) > 6.1) {
    win.setMaximumSize(screen.availWidth + 15, screen.availHeight + 15);
  }
	win.maximize();
  winMaxHeight=win.height;
  winMaxWidth=win.width;
	WinMaxOrRev(0);
}
function WinRevert(){/*恢复*/
	win.restore();
  if(win.width<692){
    win.resizeTo(692,win.height);
  }
  WinMaxOrRev(1);
//    alert(hex.getSize().height)
//hex.sizeTo(1022,660);
//window.resizeTo(window.screen.width, window.screen.height);
/*var gui = require('nw.gui');
  var win = gui.Window.get();
  win.maximize();*/
}
function WinMaxRev(){/*最大化*/
//alert(hex.formState)
	if(hex.formState==0){
		WinMax();
	}else if(hex.formState==2){
		WinRevert();
	}
}
function WinClose(force){/*退出*/
//    hex.close();
  win.close(force);	
 /* YW.ajax({
    type: 'POST',
    url: '/zb/c/user/logout',
    data:'',
    success: function(data){
      alert('正在关闭...');
      hex.close();
    },
    error:function(data){
      hex.close();
    }
  });*/
}

//根据titile可拖动窗口
// document.addEventListener('mousemove', function (e) {
//     if (e.target.classList.contains('title')) {
//         try{
//             hex.setAsTitleBarAreas(e.clientX, e.clientY);
//             // hex.setAsSystemMenuIconAreas(e.clientX, e.clientY);
//         }catch(e){}
//     } else {
//         try{
//             hex.setAsTitleBarAreas(-1, -1);
//             hex.setAsNonBorderAreas(-1, -1);
//         }catch(e){}
//     }
// }, false);
// try{
// }catch(e){}

//添加边框线条
function WinBoxBorder(){
	var winBorder='<div class="winBoxBorder winBoxBorderT"></div>'+
'<div class="winBoxBorder winBoxBorderR"></div>'+
'<div class="winBoxBorder winBoxBorderB"></div>'+
'<div class="winBoxBorder winBoxBorderL"></div>'
	$('body').append(winBorder)

}
//计算内页侧边标题高度等
function getSideH(){
  var htmls=$('body>.html'),
  sides=htmls.find('.sideCont');
  return sides.css('top');
}
function getHeadH(){
  var htmls=$('body>.html'),
  sides=htmls.find('.header').innerHeight();
  return sides;
}
// 列表标题与内容宽度保持一致
function tableFix(TableH,TableB){
  for(var i=0;i<=TableB.find('td:last').index();i++){
    TableH.find('th').eq(i).width(TableB.find('tr').eq(2).find('td').eq(i).width());
  }
}
function setTableFix(){
	var TableH=$('.TableH'),
	TableB=$('.TableB')
	if(TableH&&TableB){
		tableFix(TableH,TableB);
	}
}
// 
function tableHover(){
    $(document).find('.table-hover').on('click', 'tr', function(event) {
        $(this).parents('table').find('tr').removeClass('cursor');
        $(this).addClass('cursor').addClass('curr');
    });
}

  //改善btn-group的操作感受
    var btn_group_time=null;
    $(document).on('hide.bs.dropdown','.btn-group', function () {
      return false;
    }).on('show.bs.dropdown','.btn-group', function () {
      $(".btn-group:not(this)").removeClass('open');
    }).on('mousemove','.btn-group', function(event) {
      clearTimeout(btn_group_time);
    }).on('mouseout','.btn-group', function () {
      btn_group_time=setTimeout(function(){
        $(".btn-group").removeClass('open');
      },500);
    });

//页面加载完之后执行的脚本
$(document).ready(function() {
// 增加class为table-hover的表格点击行换色
tableHover();
// 所有A标签添加禁止拖拽
$('a').attr('draggable','false');
  //页面侧边动作
  //aBtnNavFun('.aNavBtn');
  //设置表格标题宽度
  setTableFix();
  //winBtn
    $(document).on('click', '.winBtn', function(event) {
        var Thi=$(this);
        var ThiQ=Thi.data('q');
        if(ThiQ=='menu'){

        }else if(ThiQ=='min'){
            WinMin();
        }else if(ThiQ=='max'){
            WinMax();
        }else if(ThiQ=='rev'){
            WinRevert();
        }else if(ThiQ=='close'){
        	if(Thi.attr('close')=='force'){
        		WinClose(true);
        	}else{
        		quit();
        	}
            
        }
        return false;
    });
}).resize(function(event) {
  /* Act on the event */
  WinMaxOrRev(0);
  alert(0)
});;


function getEnumTexts(category,code){
  var arr = category;
  for(var i=0;i<arr.length;i++){
    if(arr[i]['code']==code){
      return arr[i]['name'];
    }
  }
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
    $(document).find('body').prepend('<div id="autoCompleteBox" class="autocomplete"></div>');
    var Thi=id,
    oldVal,ThiMaxLen=0,ThiCurrIndex=-1,
    ThiWidth=Thi.innerWidth(),
    ThiHeight=Thi.innerHeight()+1,
    ThiOptTop=Thi.offset().top+ThiHeight,
    ThiOptLeft=Thi.offset().left,
    autocomplete=$('#autoCompleteBox');
    autocomplete.width(ThiWidth).css({'top':ThiOptTop+ThiHeight,'left':ThiOptLeft});
    autocomplete.on('click','a',function(event) {
       var This=$(this),
       ThisIndex=This.index(),
       ThisArea=This.attr('title'),
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
        ThiOptTop=Thi.offset().top+ThiHeight,
        ThiOptLeft=Thi.offset().left,
        autocomplete.css({'top':ThiOptTop,'left':ThiOptLeft});
        //alert(event.keyCode)
        if(event.keyCode=='27'){
            autocomplete.hide();
            return false;
        }else if(event.keyCode=='38'){
            if(ThiCurrIndex<=0){
                ThiCurrIndex=0;
            }else{
                ThiCurrIndex--;
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
                        autocomplete.prepend('<a href="#" title="'+val.area+'" data-address="'+val.address+'" data-quyu="'+val.quyu+'" ><i>'+val.quyu+'</i><b>'+val.area+'</b>'+val.address+'</a>');
                    });
                    ThiMaxLen=d['houses'].length
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









//获取url里需要的值
function getParam(name){
var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i"),
decode;
decode=(reg.test(location.search))? encodeURIComponent(decodeURIComponent(RegExp.$2.replace(/\+/g, " "))) : '';
decode=decodeURIComponent(decode);
return decode;
}

//相同class点击填充背景，导航选中状态
function aBtnNavFun(a){
  if($(document).on!=undefined){
    $(document).on('click', a, function(event) {
        $(a).removeClass('acurr');
        $(this).addClass('acurr');
    });
  }
}

function menuSetCurr(This){
    This.parent().find('li').removeClass('curr');
    This.addClass('curr')
}
function menuTopFun(){
    var acts=getParam('act'),
    menuBox=$('.menuLi'),
    menuBLi=menuBox.children('li');
    if(acts){
        menuBLi.removeClass('curr');
        menuBox.children('.act_'+acts).addClass('curr')
    }else{
        menuBLi.eq(0).addClass('curr')
    }
    menuBLi.addClass('nobar');
}

// 获取归属地信息
function getTelFrom(a,t,callback){
$.ajax({
dataType:'script',
scriptCharset:'gb2312',////////
url:'http://php.tech.sina.com.cn/iframe/download/download_srv.php?action=bst_ms&code='+a,
success:function(e){
if(I_SUCCESS==1){
    //extAlert(a,ms_data[1]+ms_data[2]);
    callback(ms_data[1]+ms_data[2])
}else {
    callback('查询失败');
}
}
})
}
	



function copyToClipBoard(text,callback){ 
var clipBoardContent=text; 
window.clipboardData.setData("Text",clipBoardContent); 
callback('复制成功');
} 








function btnGenjin(a,b){
	art.dialog.open("/house/house_genjin.asp?hid="+a+"&chuzu="+b,{
		title:'跟进 [编号'+a+']',
		height:160,
		width:330,
		button:[{
			name:'跟进规则',
			callback:function(){
				art.dialog({
					content: '<div class="genjinguize" style="font-size:16px; line-height:1.3em;">规则：<br />'+
'1、显示在售房源的，五家公司操作修改成已售或停售，则此房源状态自动改变；<br />'+
'2、显示已售或停售，两家公司再次提交已售或停售，则此房源自动删除；<br />'+
'3、显示已售或停售，两家公司操作修改成其他状态，则此房源状态自动改变；<br />'+
'4、以上所说“公司”非同一公司的分公司、门店；<br />'+
'5、请合理跟进。中介宝将自动屏蔽不良用语，人工删除或修改不良跟进；<br />'+
'6、请勿随跟进填写暗语或自我判定的代码，否则也将人工删除；<br />'+
'7、好房源，来自日积月累，来自点点滴滴，来自我们共同的努力。<div>',
					title:'中国好房源，来自好经纪！——中介宝'
				});
			return false;
			}
		}],okVal:'提交',
		ok: function () {
			var iframe = this.iframe.contentWindow,
			iframe_btn = iframe.document.getElementById('submits');
			$(iframe_btn).click();
			return false;
		},cancel: true
	})
}




/*-=-=-=-=-=-=-=-=-=[ 功能 ]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
//判断字符串长度
function lens(s) {
var l = 0;
var a = s.split("");
for (var i=0;i<a.length;i++) {
if (a[i].charCodeAt(0)<299) {
l++;
} else {
l+=2;
}
}
return l;
}
//拆分字符串判断另个字符串是否存在
function splitIsStr(a,b,c){
var str=a;
if(b==''){b=','};
strs=str.split(b); //字符分割      
for (i=0;i<strs.length ;i++ ){    
if(strs[i]==c){
return 1;
break;
}else{
return 0;
}
} 
}

/*-=-=-=-=-=-=-=-=-=[ 全选的多选框 开始 ]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
function selectAll(){
	if(this.checked==true){ 
		checkAll('id'); 
	} else { 
		clearAll('id'); 
	}
}
function checkAll(name){
	var el = document.getElementsByTagName('input');
	var len = el.length;
	for(var i=0; i<len; i++){
		if((el[i].type=="checkbox") && (el[i].name==name)){
			el[i].checked = true;
		}
	}
}
function clearAll(name){
	var el = document.getElementsByTagName('input');
	var len = el.length;
	for(var i=0; i<len; i++){
		if((el[i].type=="checkbox") && (el[i].name==name)){
			el[i].checked = false;
		}
	}
}
//全选的多选框 结束


/*-=-=-=-=-=-=-=-=-=[ cookies ]=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/
//写入cookies
function SetCookie(name,value){
	var Days = 30;
	var exp = new Date(); 
	exp.setTime(exp.getTime() + Days*24*60*60*1000);
	document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();
}
//读取cookies
function GetCookie(name){
	var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
	if(arr=document.cookie.match(reg)) return unescape(arr[2]);
	else return null;
}
//删除cookies
function DelCookie(name){
	var exp = new Date();
	exp.setTime(exp.getTime() - 1);
	var cval=GetCookie(name);
	if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
//使用示例
//SetCookie("a","yimixia.com");
//alert(GetCookie("a"));
// $(function(){
// //屏蔽JS报错
// function killErrors() {return true;}
// window.onerror = killErrors;	
// })







/* 封装 */
function jajax(url,param,asyncs){
    YW.ajax({
        type: 'POST',
        async:false,
        url: '/c/dept/listDept',
        data:param,
        success: function(data){
            return JSON.parse(data);
        }
    });
}


function quit(){
  art.dialog.confirm('退出系统？', function () {
      $.ajax({
        type: 'get',
        url: '/c/user/logout',
        success: function(data){
          WinClose(true);
        }
      });
  },function(){},'warning');
}