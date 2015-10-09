<%@page import="org.bc.sdak.utils.JSONHelper"%>
<%@page import="java.util.List"%>
<%@page import="com.youwei.zjb.house.entity.SchoolDistrict"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("initCordinate", ThreadSessionHelper.getCityCordinate());
CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
List<SchoolDistrict> list = dao.listByParams(SchoolDistrict.class, "from SchoolDistrict where 1=1 ");
if(!list.isEmpty()){
	request.setAttribute("list", JSONHelper.toJSONArray(list));	
}
request.setAttribute("defaultOffsetX", -20);
request.setAttribute("defaultOffsetY", -10);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>学区设置</title>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<link rel="stylesheet" type="text/css" href="/style/font/iconfont.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
	body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
	#allmap {width: 100%; height:500px; overflow: hidden;}
	#result {width:100%;font-size:12px;}
	dl,dt,dd,ul,li{
		margin:0;
		padding:0;
		list-style:none;
	}
	p{font-size:12px;}
	dt{
		font-size:14px;
		font-family:"微软雅黑";
		font-weight:bold;
		border-bottom:1px dotted #000;
		padding:5px 0 5px 5px;
		margin:5px 0;
	}
	dd{
		padding:5px 0 0 5px;
	}
	li{
		line-height:28px;
	}
	</style>
	
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=E2b4310c75fa4adc7348a54e4aea6521"></script>
	<!--加载鼠标绘制工具-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
	<!--加载检索信息窗口-->
<!-- 	<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script> -->
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
</head>

<body>

<div class="KY_Main KY_W">
     
     <div class="MainRight">
          <div  style="display:table; width:100%; height:100%; overflow:hidden;" class="not-select">
            <div id="menuTop" style="display:inline-block;">
              <ul class="MainRightTop KY_W titlebar">
                  <li class="slect nobar" onclick="window.location='/map/xuequ.jsp'"><i class="i1"></i>学区</li>
                  <li class="line"></li>
              </ul>
              </div>
              
              <div id="allmap" style="overflow:hidden;zoom:1;position:relative;height:94%">	
				<div id="map" style="height:100%;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
			</div>
           </div>
           
           
     </div>
     <form style="display:none" name="form1">
     	<input name="name" type="hidden"  id="name"></input>
     	<input name="path" type="hidden"  id="path"></input>
     	<input name="center" type="hidden"  id="center"></input>
     	<input name="offsetX" type="hidden" value="${defaultOffsetX }"  id="offsetX"></input>
     	<input name="offsetY" type="hidden" value="${defaultOffsetY }"  id="offsetY"></input>
     </form>
     
	<script type="text/javascript">
	// 百度地图API功能
    var map = new BMap.Map('map');
    var poi = new BMap.Point(${initCordinate});
    map.centerAndZoom(poi, 18);
    map.enableScrollWheelZoom(); 
    
    var styleOptions = {
        strokeColor:"red",    //边线颜色。
        fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 3,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.01,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    }
    //实例化鼠标绘制工具
    var drawingManager = new BMapLib.DrawingManager(map, {
        isOpen: false, //是否开启绘制模式
        enableDrawingTool: true, //是否显示工具栏
        drawingToolOptions: {
            anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
            offset: new BMap.Size(5, 5), //偏离值
            drawingModes:[BMAP_DRAWING_POLYGON]
        },
        polygonOptions: styleOptions //多边形的样式
    });  
	 //添加鼠标绘制工具监听事件，用于获取绘制结果
    drawingManager.addEventListener('polygoncomplete', function(e){
    	//添加学区名
    	var point = e.getBounds().getCenter();
    	var label = new BMap.Label("点击编辑学区",{position : point});
    	label.setOffset(new BMap.Size(${defaultOffsetX}, ${defaultOffsetY}));
    	label.setStyle({cursor:"pointer"});
    	map.addOverlay(label);
    	addXueQu(e , function(id){
    		label.xqid=id;
    		e.K.xqid=id;
    		e.addEventListener('click' , function(){
        		art.dialog.open("/map/xuequ_edit.jsp?id="+id,{id:'xuequ_edit'});
        	});	
    		label.addEventListener('click' , function(){
        		art.dialog.open("/map/xuequ_edit.jsp?id="+id,{id:'xuequ_edit'});
        	});	
    	});
    	
    });
    
    function addXueQu(polygon , callback){
    	//polygon.setStrokeColor(getRandomColor());
    	var paths = polygon.getPath();
    	var center = polygon.getBounds().getCenter();
    	var path='';
    	for(var i=0;i<paths.length;i++){
    		var xx = paths[i].lng+','+paths[i].lat;
    		path+=xx;
    		path+=';';
    	}
    	$('#path').val(path);
    	$('#name').val('点击编辑学区');
    	$('#center').val(center.lng+','+center.lat);
    	
    	var a=$('form[name=form1]').serialize();
    	  YW.ajax({
    	    type: 'POST',
    	    url: '/c/schoolDistrict/add',
    	    data:a,
    	    mysuccess: function(data){
    	        alert('添加学区成功');
    	        var json = JSON.parse(data);
    	        callback(json.id);
    	    }
    	  });
    }
var xuequList = JSON.parse('${list}');
$(function(){
	for(var i=0;i<xuequList.length;i++){
		initXueQu(xuequList[i]);
	}
});

function initXueQu(xq){
	var arr = xq.center.split(',');
	var point = new BMap.Point(arr[0] , arr[1]);
	var label = new BMap.Label(xq.name,{position : point});
	label.setOffset(new BMap.Size(xq.offsetX, xq.offsetY));
	label.xqid = xq.id;
	label.setStyle({cursor:"pointer"});
	label.addEventListener('click' , function(){
		art.dialog.open("/map/xuequ_edit.jsp?id="+xq.id,{id:'xuequ_edit'});
	});
	map.addOverlay(label);
	var points = xq.path.split(';');
	var paths = [];
	for(var i=0;i<points.length;i++){
		if(points[i]==''){
			continue;
		}
		var yy = points[i].split(',');
		var p = new BMap.Point(yy[0] , yy[1]);
		paths.push(p);
	}
	var polygon = new BMap.Polygon(paths, styleOptions);
	//polygon.setStrokeColor(getRandomColor());
	polygon.xqid = xq.id;
	polygon.addEventListener('click' , function(){
		art.dialog.open("/map/xuequ_edit.jsp?id="+xq.id,{id:'xuequ_edit'});
	});
	map.addOverlay(polygon);
}

function updateXueQu(xq){
	var xx = findLabelById(xq.id);
	if(xx){
		xx.setContent(xq.name);
		xx.setOffset(new BMap.Size(xq.offsetX, xq.offsetY));
	}
}

function findLabelById(xqid){
	var arrayOverlays = map.getOverlays();
	for (var i = 0; i < arrayOverlays.length; i++) {
		if (arrayOverlays[i].xqid ==xqid) {
			return arrayOverlays[i];
		}
	}
}

function removeXueQu(id){
	var xx = findOverlayById(id);
	if(xx){
		$(xx).each(function(i,obj){
			map.removeOverlay(obj);	
		});
	}
}
function findOverlayById(xqid){
	var arrayOverlays = map.getOverlays();
	var findObjs= [];
	for (var i = 0; i < arrayOverlays.length; i++) {
		if (arrayOverlays[i].xqid ==xqid) {
			findObjs.push(arrayOverlays[i]);
		}
		if(arrayOverlays[i].K && arrayOverlays[i].K.xqid == xqid){
			findObjs.push(arrayOverlays[i]);
		}
	}
	return findObjs;
}

var colors = ["red" , "blue" , "black","green" , "yellow"];
function getRandomColor(){
	var index = Math.floor(Math.random()*5);
	return colors[index];
}
</script>
</div>

</body>
</html>

