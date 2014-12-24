      
	  var _move=false;//移动标记  
	  var _x,_y;//鼠标离控件左上角的相对位置  
	  
	  
	  function layerShowBox(id){
		  
		  var w = $("#"+id).width();
		  var h = $("#"+id).height();
		  var winW = $(window).width();
		  var winH = $(window).height();
		  var L = (winW - w)/2;
		  var T = (winH - h)/2;
		  
		  if(L<=0) L=0;
		  if(T<=0) T=0;
		  
		  $("#"+id).css("left",L);
		  $("#"+id).css("top",T);
		  
		  
		  
	  }
	  
	  function showBox(){
		 
		 $("#layerBoxDj").css("display","block");
		 layerShowBox("layerBoxDj");
		 
		 
		 
		    $("#cocoWintit").mousedown(function(e){  
				_move=true;  
				_x=e.pageX-parseInt($("#layerBoxDj").css("left"));  
				_y=e.pageY-parseInt($("#layerBoxDj").css("top"));  
				$("#layerBoxDj").fadeTo(20, 0.5);//点击后开始拖动并透明显示  
			});  
			$(document).mousemove(function(e){  
				if(_move){  
					var x=e.pageX-_x;//移动时根据鼠标位置计算控件左上角的绝对位置  
					var y=e.pageY-_y;  
					$("#layerBoxDj").css({top:y,left:x});//控件新位置  
				}  
			});
			
			 $("#cocoWintit").mouseup(function(){  
				_move=false;  
				$("#layerBoxDj").fadeTo("fast", 1);//松开鼠标后停止移动并恢复成不透明  
			});  
		 
		   
	  }
	  
	  function closeBox(callback){
		 
		 $("#layerBoxDj").css("display","none");
		 $("#layerBoxBgShadow").css("display","none");
		 if(callback){
		 	callback();
		 }
	  }
	  
	  function LayerShowBox(id){
		 
		 $("#"+id).css("display","block");
		 layerShowBox(id);
		 
		    $("#"+id).children(".cocoLayerTit").mousedown(function(e){  
				_move=true;  
				_x=e.pageX-parseInt($("#"+id).css("left"));  
				_y=e.pageY-parseInt($("#"+id).css("top"));  
				$("#"+id).fadeTo(20, 0.5);//点击后开始拖动并透明显示  
			});  
			$(document).mousemove(function(e){  
				if(_move){  
					var x=e.pageX-_x;//移动时根据鼠标位置计算控件左上角的绝对位置  
					var y=e.pageY-_y;  
					$("#"+id).css({top:y,left:x});//控件新位置  
				}  
			});
			
			 $("#"+id).children(".cocoLayerTit").mouseup(function(){  
				_move=false;  
				$("#"+id).fadeTo("fast", 1);//松开鼠标后停止移动并恢复成不透明  
			});  
		 
	  }
	  
	  function LayerCloseBox(id){
		 
		 $("#"+id).css("display","none");
		   
	  }
	  window.onresize = function(){
		  layerShowBox("cocoLayerWebLine");
	  }
	  
	  
	/*联系人排序*/  
	  
	  function lxrzaixian(){
		  
			  var $lxrNum = $("ul#cocoList li").size(); 
			  var $online = $("ul#cocoList li"); 
			  
			  var $firstUnlineLi = $("ul#cocoList li").eq(0);
			  
			  for( var i=0;i<$lxrNum;i++){
				 
				 var x = $("ul#cocoList li").eq(i).find(".cocoOnline").size();
				 if(x>0){
					 $("ul#cocoList li").eq(i).insertBefore($firstUnlineLi);
				 }
			  }  
			  
	  }
	  
	 /*联系人排序*/ 
	  
	  
	  
$(function(){
	
	  lxrzaixian();
	  
	  //$("#msgContainer_36").ScrollintoView();
	
});  

