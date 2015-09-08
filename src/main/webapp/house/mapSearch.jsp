<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
 	request.setAttribute("initCordinate", ThreadSessionHelper.getCityCordinate());
	Float latStart=0f;
	Float latEnd=0f;
	Float lngStart=0f;
	Float lngEnd=0f;
	String latStartStr = request.getParameter("latStart");
	String latEndStr = request.getParameter("latEnd");
	String lngStartStr = request.getParameter("lngStart");
	String lngEndStr = request.getParameter("lngEnd");
	try{
		latStart = Float.valueOf(latStartStr);
		latEnd = Float.valueOf(latEndStr);
		lngStart = Float.valueOf(lngStartStr);
		lngEnd = Float.valueOf(lngEndStr);
		float x = (lngStart+lngEnd)/2;
		float y = (latStart+latEnd)/2;
		request.setAttribute("initCordinate", x+","+y);
	}catch(Exception ex){
		
	}
	request.setAttribute("latStart", latStart);
	request.setAttribute("latEnd", latEnd);
	request.setAttribute("lngStart", lngStart);
	request.setAttribute("lngEnd", lngEnd);
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
	body, html{width: 100%;height: 100%;margin:0;font-family:"微软雅黑";}
	</style>
	<script type="text/javascript" src="/js/jquery.js"></script>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=E2b4310c75fa4adc7348a54e4aea6521"></script>
	<!--加载鼠标绘制工具-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
	<!--加载检索信息窗口-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
	<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
	<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
	<title>地图找房</title>
</head>
<body>
	<div id="allmap" style="overflow:hidden;zoom:1;position:relative;width: 100%; height: 100%;">
		<div id="map" style="height:100%;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
	</div>
	<script type="text/javascript">
	var moveCenter = false;
    var map = new BMap.Map('map');
    var poi = new BMap.Point(${initCordinate});
    
    map.enableScrollWheelZoom();
    var controller = new BMap.NavigationControl();
	map.addControl(controller);//缩放平移控件
	//var marker = new BMap.Marker(poi); // 创建点
	//map.addOverlay(marker);
    var overlays = [];
var overlaycomplete = function(e){
	clearAll();
    overlays.push(e.overlay);
    var result = e.overlay.Rn;
    //var lt=e.overlay.Rn[0]
  //01
  //32
  //alert(result[0].lng)
  var lngStart = result[0].lng;
  var lngEnd = result[2].lng;
  var latStart = result[2].lat;
  var latEnd = result[0].lat;
  art.dialog.opener.setMapSearch(lngStart , lngEnd , latStart , latEnd);
  art.dialog.close();
};
    //实例化鼠标绘制工具
    var drawingManager = new BMapLib.DrawingManager(map, {
        isOpen: false,
        enableDrawingTool: true,
		enableCalculate: false,
        drawingToolOptions: {
            anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
            offset: new BMap.Size(5, 5), //偏离值
            drawingModes:[BMAP_DRAWING_RECTANGLE],
            scale:1
        }
    });
    drawingManager.setDrawingMode(BMAP_DRAWING_RECTANGLE);
    drawingManager.addEventListener('overlaycomplete', overlaycomplete);
    function clearAll() {
		for(var i = 0; i < overlays.length; i++){
            map.removeOverlay(overlays[i]);
        }
        overlays.length = 0
    }
    
    map.centerAndZoom(poi, 15);
    //再定位一次,artdialog有问题
    setTimeout(function(){
    	if(${lngStart} && ${latStart}){
    		map.centerAndZoom(poi, 15);
    		var rectangle = new BMap.Polygon([
    			                          		new BMap.Point(${lngStart},${latEnd}),
    			                          		new BMap.Point(${lngEnd},${latEnd}),
    			                          		new BMap.Point(${lngEnd},${latStart}),
    			                          		new BMap.Point(${lngStart},${latStart})
    			                          	], {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});  //创建矩形
    			map.addOverlay(rectangle);
    	}	
    },100);
</script>
</body>
</html>
