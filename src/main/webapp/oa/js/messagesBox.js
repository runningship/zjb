      
	  var _move=false;//移动标记  
	  var _x,_y;//鼠标离控件左上角的相对位置  
	  
	  
	  function layerShowBox(id){
		  
		 // var $mainId = $(window.top.document).find("#"+id);
		var $mainId =$("#iframe_oa",window.top.document).contents().find("#"+id);
		 // var $mainId =  $("#"+id);
		  var w = $mainId.width();
		  var h = $mainId.height();
		  var winW = $("#iframe_oa",window.top.document).width();
		  var winH = $("#iframe_oa",window.top.document).height();
		  var L = (winW - w)/2;
		  var T = (winH - h)/2;
		  
/*		  if(L<=0) L=0;
		  if(T<=0) T=0;*/
		  
		  T = T<=50?50:T;
		  T = T>=winH-200?winH-200:T;
					
					L = L<=10?10:L;
					L = L>=winW-300?$winW-300:L;
		  
		  $mainId.css("left",L);
		  $mainId.css("top",T);
		  
		  
	  }
	  
	  function showBox(){
		 
		 $("#layerBoxDj").css("display","block");
		 
		 
		    $("#cocoWintit").mousedown(function(e){  
				_move=true;  
				
				$("#layerBoxDj").css({"z-index":artDialog.defaults.zIndex++});
				
				$(".mask").show();
				
				_x=e.pageX-parseInt($("#layerBoxDj").css("left"));  
				_y=e.pageY-parseInt($("#layerBoxDj").css("top"));  
				$("#layerBoxDj").fadeTo(0, 0.75);//点击后开始拖动并透明显示  
				
			});  
			
			$(document).mousemove(function(e){  
			//	$(document).bind("selectstart",function(){return false;});  
				if(_move){  
					var x=e.pageX-_x;//移动时根据鼠标位置计算控件左上角的绝对位置  
					var y=e.pageY-_y;
					
					
					x = x<=10?10:x;
					x = x>=$(window).width()-300?$(window).width()-300:x;
					
					y = y<=50?50:y;
					y = y>=$(window).height()-100?$(window).height()-100:y;
					
					
					$("#layerBoxDj").css({top:y,left:x});//控件新位置
				}  
			});
			
			$("#cocoWintit").mouseup(function(){  
				_move=false;  
				$("#layerBoxDj").fadeTo(0, 1);//松开鼠标后停止移动并恢复成不透明
				$(".mask").hide();  
			})
			
/*			 $("#cocoWintit").mouseup(function(){  
				_move=false;  
				$("#layerBoxDj").fadeTo(10, 1);//松开鼠标后停止移动并恢复成不透明  
			});  */
		 
		   
	  }
	  
	  function closeBox(callback){
		 
		 $("#layerBoxDj").css("display","none");
		 $("#layerBoxBgShadow").css("display","none");
		 if(callback){
		 	callback();
		 }
	  }
	  
	  function LayerShow(id){
			//var $mainId = $(window.top.document).find("#"+id);
			//$("#iframe_oa",window.top.document).contents()
			var $mainId =$("#iframe_oa",window.top.document).contents().find("#"+id);
			//var $mainId =$("#"+id);
		    var $id = $mainId.children(".cocoLayerTit");
			
		    $id.mousedown(function(e){ 
				_move=true;  
				//$mainId.css({"z-index":artDialog.defaults.zIndex++});
				$id.parent().siblings().css({"z-index":"1990"});
				
				$(this).parent().css({"z-index":"2000"});
				
				$(".maskLayer").show();
				
				_x=e.pageX-parseInt($mainId.css("left"));  
				_y=e.pageY-parseInt($mainId.css("top"));  
				$mainId.fadeTo(0, 0.75);//点击后开始拖动并透明显示  
			});
	
		  //  alert("22");
			$("#iframe_oa",window.top.document).contents().find("#"+id).mousemove(function(e){  
				if(_move){  
					var x=e.pageX-_x;//移动时根据鼠标位置计算控件左上角的绝对位置  
					var y=e.pageY-_y;  
					
					x = x<=10?10:x;
					x = x>=$("#iframe_oa",window.top.document).width()-300?$("#iframe_oa",window.top.document).width()-300:x;
					
					y = y<=50?50:y;
					y = y>=$("#iframe_oa",window.top.document).height()-100?$("#iframe_oa",window.top.document).height()-100:y;
					
					$mainId.css({top:y,left:x});//控件新位置  
				}  
			});
			
			$id.mouseup(function(){  
				//$(document).bind("selectstart",function(){return true;});  
				_move=false;  
				$(".maskLayer").hide();  
				$mainId.fadeTo(0, 1);//松开鼠标后停止移动并恢复成不透明
			});
		 
	  }

	  function LayerCloseBox(id){
		 
		 $("#"+id).css("display","none");
		   
	  }
	  window.onresize = function(){
		  layerShowBox("cocoLayerWebLine");
	  }
	  
	  
	/*联系人排序*/  
	  
	  function lxrzaixian(id){
		  
			  var $lxrNum = $("ul#"+ id +" li").size(); 
			  var $online = $("ul#"+ id +" li"); 
			  
			  var $firstUnlineLi = $("ul#"+ id +" li").eq(0);
			  
			  
			  
			  
			  for( var i=1;i<$lxrNum;i++){//在线排序
				 
				 
				 var x = $online.eq(i).find(".cocoOnline").size();
				 var y = $online.eq(i).find(".cocoWinNewsNum").size();
				 var z = $online.eq(i).find(".cocoLeave").size();
				 
				 if(x>0){//在线的上去
					 $online.eq(i).insertBefore($firstUnlineLi);
				 }
				 if(z>0){//不在线的下去去
					 $online.eq(i).insertAfter($firstUnlineLi);
				 }
				 
				 
			  } 
			  
			  for( var i=1;i<$lxrNum;i++){
				  
				 var y = $online.eq(i).find(".cocoWinNewsNum").size();
				 if(y>0){
					 $online.eq(i).insertBefore($firstUnlineLi);
				 }
			  }
			  
			  for( var i=0;i<$lxrNum;i++){//在线有消息排序排序
				  
				 var x = $online.eq(i).find(".cocoOnline").size();
				 var y = $online.eq(i).find(".cocoWinNewsNum").size();
				 
				 if(x>0 && y>0){
					 $("ul#"+ id +" li").eq(i).insertBefore($firstUnlineLi);
				 }
				 
			  }

			  
	  }
	  
	 /*联系人排序*/ 
	 
	 
	 
	 function LayerRemoveBox(id){
		 
		 $("#"+id).remove();
		 $(".maskLayer").remove();
		   
	  }
	  
	  
	  function openNewWin(id,w,h,tit,s){
		  
		//window.showModalDialog("www.baidu.com"); 
		if ($("#iframe_oa",window.top.document).contents().find('#'+id).length>0) {
			return;
		};
	    	 var htmlText = "<iframe class='maskLayer'></iframe><div class='maskLayer'></div><div class='cocoLayer' id=" + id + " style='width:" + w + "px; height:"+ h +"px; display:block; z-index:2001;'><div class='cocoLayerTit'><span>" + tit + "</span><i class='closeBg close' onclick='LayerRemoveBox(\""+id+"\")' title='关闭'></i></div><iframe src='"+s+"' style='width:100%;border:0px;height:"+(h-32)+"px;-webkit-user-select: text;'></iframe></div>";
			 
			
			
			// window.top.document.children("body").find("#oaMainPage").append(htmlText);
			//$(window.parent.document)
			//$("#iframe_oa", window.top.document);
			     $("#iframe_oa",window.top.document).contents().find("#oaMainPage").append(htmlText);
				 layerShowBox(id);
		     	 LayerShow(id);
		     
		  
	  }
	 /*function openNewWin(id,w,h,tit,s){
		if ($('#'+id).length>0) {
			return;
		};
	    	 var htmlText = "<iframe class='mask'></iframe><div class='mask'></div><div class='cocoLayer' id=" + id + " style='width:" + w + "px; height:"+ h +"px; display:block; z-index:1990;'><div class='cocoLayerTit'><span>" + tit + "</span><i class='closeBg close' onclick='LayerRemoveBox(\""+id+"\")' title='关闭'></i></div><iframe src='"+s+"' style='width:100%;border:0px;height:"+(h-32)+"px;-webkit-user-select: text;'></iframe></div>";
			 
			
			
			// $(window.top.document).find("body").eq(0).append(htmlText);
			 $("body").append(htmlText);
				 layerShowBox(id);
		     	 LayerShow(id);
		     
		  
	  }*/
	  /*cocoListLayerTit*/
	  
	  function openListWin(id,w,h,tit,s){// 调用方式：onclick="openNewWin('addGg','980','650','全部公告','gg.html')"
		 
			if ($('#'+id).length>0) {
				return;
			};
			
	    	 var htmlText = "<iframe class='mask'></iframe><div class='mask'></div><div class='cocoLayer' id=" + id + " style='width:" + w + "px; height:" + h + "px; display:block; z-index:1990;'><div class='cocoLayerTit'><span>" + tit + "</span><i class='closeBg close' onclick='LayerRemoveBox(\""+id+"\")' title='关闭'></i></div><iframe src='"+s+"' style='width:100%;border:0px;height:"+(h-32)+"px;-webkit-user-select: text;'></iframe></div>";
			 
			 $("body").find("#oaMainPage").append(htmlText);
			// $(window.top.document).find("body").eq(0).find("div#allMainBoxer").append(htmlText);
			 
			 layerShowBox(id);
			 
			 LayerShow(id);
	  
/*			  $(window).resize(function() {
				  layerShowBox(id);
			  });	*/	  
	  }

	  
	  
	  
$(function(){
	
	  lxrzaixian("cocoList");
	  lxrzaixian("cocoQunList");
	  
	
});  

