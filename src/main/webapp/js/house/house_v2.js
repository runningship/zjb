var houseData;
var houseGJbox;
var searching=false;
var formHtml=[];
var formPos=-1;
var chuzu=0;
function doSearch(callback){
  if(searching){
    return;
  }
  if(event==undefined || $(event.srcElement).attr('action')!='page'){
    $('.pageInput').val(1);
  }
  searching=true;
  $('#searchBtn').attr('disabled','disabled');
  var a=$('form[name=form1]').serialize();
  // pushQueryToHistory($('form[name=form1]').serializeArray());
  YW.ajax({
    type: 'POST',
    url: '/c/house/listAll',
    data:a,
    mysuccess: function(data){
        houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        
        if(callback){
          callback();
        }
        //滚动到最上
        $('#KY_TableMain').parent().scrollTop(-2000);
    },
    complete:function(){
      searching=false;
      $('#searchBtn').removeAttr('disabled');
    }
  });
}

function doSearchAndSelectFirst(notFresh){
  //saveQuery();
  doSearch(function(){
    if(houseData.page.data.length>0){
      $('.TableB').find('tr')[1].click();
    }
  });
  if(notFresh==false || notFresh==undefined){
    formPos = formHtml.length-1;
  }
}

function saveQuery(){
  $('form input').each(function(index,obj){
    if(obj.type=='text' || obj.type=='hidden'){
      $(obj).attr('value',$(obj).val());
    }else if(obj.type=='checkbox' || obj.type=='radio'){
      if(obj.checked){
        $(obj).attr('checked','checked');
      }else{
        $(obj).removeAttr('checked');
      }
    }
  });
  formHtml.push($('form').html());
  // console.log($('form').html());
}

function goAhread(){
  if(formPos>=formHtml.length-1){
    return;
  }
  formPos++;
  $('form').html(formHtml[formPos]);
  doSearchAndSelectFirst(true);
  
}

function goBack(){
  if(formPos<=0 || formHtml.length<1){
    return;
  }
  formPos--;
  $('form').html(formHtml[formPos]);
  doSearchAndSelectFirst(true);
}

function getShenHeText(lock){
  if(lock==1){
    return "审";
  }else{
    return "未";
  }
}

function showAction(hcid,cid , seeGX){
  if(cid==1){
    return true;
  }else if(hcid==cid){
    if(seeGX==1){
      return false;
    }else{
      return true;
    }
  }else{
    return false;
  }
}

function shenheFy(id , obj){
  // event.cancelBubble=true;
  YW.ajax({
    type: 'POST',
    url: '/c/house/toggleShenHe?id='+id,
    mysuccess: function(data){
        var json = JSON.parse(data);
        if(json.sh==1){
          $(obj).text('审');
          $(obj).css('color','green');
        }else{
          $(obj).text('未');
          $(obj).css('color','red');
        }
    }
  });
}

function houseEdit(id){
  // event.cancelBubble=true;
  art.dialog.open("/house/house_edit.jsp?id="+id,{id:'seemap'})
}

function deletehouse(id){
  event.cancelBubble=true;
  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
  YW.ajax({
    type: 'POST',
    url: '/c/house/physicalDelete?id='+id,
    mysuccess: function(data){
      $(event.srcElement).attr('action','page');
      doSearchAndSelectFirst();
      alert('删除成功');
    }
  });
  },function(){},'warning');
}

function getSider(id){
    if(id){
        $('#sideCont').attr('src','/house/houseSee_v2.jsp?id='+id+'&chuzu='+chuzu);
    }
}

function setMapSearch(lngStart , lngEnd , latStart , latEnd){
	$('#lngStart').val(lngStart);
	$('#lngEnd').val(lngEnd);
	$('#latStart').val(latStart);
	$('#latEnd').val(latEnd);
	
	var str = '经度:'+lngStart+'-'+lngEnd+',维度:'+latStart+'-'+latEnd;
	//设置查询条件提示 
	if($('#mapSearch').length==0){
		var xx = '<li id="mapSearch" name=map>地图</li>';
	    $('#query_str').find('span').append(xx);	
	}
	doSearchAndSelectFirst(true);
}

function openMapSearch(xuequ){
	var lngStart = $('#lngStart').val();
	var lngEnd = $('#lngEnd').val();
	var latStart = $('#latStart').val();
	var latEnd = $('#latEnd').val();
	art.dialog.open('/house/mapSearch.jsp?xuequ='+xuequ+'&latStart='+latStart+'&latEnd='+latEnd+'&lngStart='+lngStart+'&lngEnd='+lngEnd,{id:'mapSearch',title:'地图框选找房',width:1200,height:550});
}

function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a');
    ThiA.removeClass('hover');
    var Vals=ThiA.eq(index).addClass('hover').attr('area');
    $('#search').val(Vals);
}

function updateHouse(id,data){
  getSider(id);
  var json = JSON.parse(data);
  var tr = $('tr[data-hid='+json['house']['id']+']');
  var a = tr.children()[2]
  var b = tr.children()[3]
  var c = tr.children()[4]
  var d = tr.children()[5]
  var e = tr.children()[6]
  var f = tr.children()[7]
  var g = tr.children()[8]
  var h = tr.children()[9]
  var i = tr.children()[10]
  var j = tr.children()[11]
  var k = tr.children()[12]
  $(a).html(json['house']['ztai']);
  $(b).html(json['house']['quyu']);
  $(c).html(json['house']['area']+' '+json['house']['dhao']+'-'+json['house']['fhao']);
  $(d).html(json['house']['lxing']);
  $(e).html(json['house']['hxf']+'-'+json['house']['hxt']+'-'+json['house']['hxw']);
  $(f).html(json['house']['mji']);
  $(g).html(json['house']['zjia']);
  $(h).html(json['house']['djia']);
  $(i).html(json['house']['lceng']+'/'+json['house']['zceng']);
  $(j).html(json['house']['zxiu']);
  $(k).html(json['house']['dateadd']);
}

    $(document).on('click', '.adboxs', function(event) {
        var Thi=$(this),
        ThiImg=Thi.find('img');
            ThiImg.removeClass('fadeInDown').addClass('fadeOutUp');
        var tms=setTimeout(function(){
            Thi.hide();
        },700);
        event.preventDefault();
    });

$(document).ready(function(){
	chuzu = getParam('chuzu');
	initTopMenu();
	autoComplete($('#search'));
	
  ChangeW();
  
    Page.Init();
//     $.get('/c/config/getQueryOptions', function(data) {
//       queryOptions=JSON.parse(data);
//       buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
//       buildHtmlWithJsonArray("id_fxing",queryOptions['fxing'],true);
//       buildHtmlWithJsonArray("id_zhuangxiu",queryOptions['zhuangxiu'],true);
//       buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
//       buildHtmlWithJsonArray("id_zhuangtai",queryOptions['ztai_sell'],true, true);
//       doSearchAndSelectFirst();
//     });
    buildQueryOptions();
    doSearchAndSelectFirst();
    
    $("button.selectMethod").parent().hover(function(){
      $(this).siblings().find("div.ulErMenu").hide();
      $(this).find("div.ulErMenu").show();
    },function(){
         $(this).find("div.ulErMenu").hide(); 
    });
    $('.id_House_list').on('click', 'a', function(event) {
        var Thi=$(this),
        rel=Thi.data('rel'),
        this_hid=Thi.data('hid');
    });
    $('.TableB').on('click', 'tr', function(event) {
        var Thi=$(this),
        ThiHid=Thi.data('hid');
        if(houseGJbox)houseGJbox.close();
        getSider(ThiHid);
        // Thi.toggleClass('selected');
        return false;
    });
    // tableHover();
    if(!window.top.hasShowAds){
      //if($${seeAds}){
//         if(window.screen.height<768){
//           art.dialog.open("/ad/leshi.html",{id:'ads',width:406,height:406,title:'',lock:true,padding:0,top:50});   
//         }else{
//           art.dialog.open("/ad/leshi.html",{id:'ads',width:406,height:406,title:'',lock:true,padding:0}); 
//         }
        //$('#ad_container').show();
        window.top.hasShowAds=true;
      //}
    }
	 
	$(document).bind("contextmenu",function(e){
			  return false;
	});
	
	 
	// $(document).bind("mousedown",function(e){
	// 	if(e.which == 3){
 //  		$("#goAhread").css('display','');
 //  		$("#goBack").css('display','');
 //      if(formPos<=0){
 //        $("#goBack").css('display','none');
 //      }
 //      if(formPos<0 || formPos>=formHtml.length-1){
 //        $("#goAhread").css('display','none');
 //      }
 //      $("#GoAndBack").css({"display":"block","top":e.pageY+5,"left":e.pageX+5});
	// 	}
		
	// });
	
 //  $(document).bind("mouseup",function(e){
 //    if(e.which == 1){
 //      $("#GoAndBack").hide();
 //    }
    
 //  });
		 
});

function showAds(img){
	if(img){
		$('#ad_container img').attr('src',img);
	}
	$('#ad_container img').removeClass('fadeOutUp').addClass('fadeInDown');
	$('#ad_container').show();
}

function openGongGao(){
	art.dialog.open("/ad/gonggao.html",{id:'gonggao',width:960,height:615,title:'',lock:true,padding:0}); 
}			 

function setPageOrder(col){
	var order = $('#'+col +'Order').attr('next');
	$('#orderBy').val(col);
	$('#order').val(order);
	$('.order').text('');
	if(order=='desc'){
		//↑↓
		$('#'+col +'Order').text('↓');
		$('#'+col +'Order').attr('next' , 'asc');
	}else{
		$('#'+col +'Order').text('↑');
		$('#'+col +'Order').attr('next' , 'desc');
	}
	$('#searchBtn').click();
}

function onSetDateStart(){
	var timeend=$dp.$('idTime');
	var wp =WdatePicker({lang:'zh-cn',
		startDate:'%y-%M-%d 00:00:00',
		dateFmt:'yyyy-MM-dd',
		onpicked:function(){
			idTime.focus();
			},
		maxDate:'#F{$dp.$D(\'idTime\')}'
		});
}

function onSetDataEnd(){
	var wp2 = WdatePicker({lang:'zh-cn',startDate:'%y-%M-%d 23:59:59',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'idTimes\')}'});
}