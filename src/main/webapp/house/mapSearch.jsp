<%@page import="java.util.List"%>
<%@page import="org.bc.sdak.utils.JSONHelper"%>
<%@page import="com.youwei.zjb.house.entity.SchoolDistrict"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
 	request.setAttribute("initCordinate", ThreadSessionHelper.getCityCordinate());
	request.setAttribute("xuequ", request.getParameter("xuequ"));
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
	
	//加载学
	CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	List<SchoolDistrict> list = dao.listByParams(SchoolDistrict.class, "from SchoolDistrict where 1=1");
	if(!list.isEmpty()){
		request.setAttribute("list", JSONHelper.toJSONArray(list));	
	}else{
		request.setAttribute("list", "[]");
	}
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
	<script type="text/javascript">
var annis;
        function anni(){
            var Thi=$('.tipbox');
            Thi.animate({'top':'80px', 'box-shadow': '10px 10px 30px #000'}, 500,function(e) {
                Thi.animate({   
                    'top': '70px',
                    'box-shadow': '5px 5px 20px #000'},
                    500, function() {
                    //clearInterval(annis);
                });
            })
        }
    $(document).ready(function() {
        annis=setInterval('anni()',1000);
    });

var styleOptions = {
        strokeColor:"red",    //边线颜色。
        fillColor:"red",      //填充颜色。当参数为空时，圆形将没有填充效果。
        strokeWeight: 2,       //边线的宽度，以像素为单位。
        strokeOpacity: 0.3,	   //边线透明度，取值范围0 - 1。
        fillOpacity: 0.08,      //填充的透明度，取值范围0 - 1。
        strokeStyle: 'solid' //边线的样式，solid或dashed。
    }
var xuequList = JSON.parse('${list}');
$(function(){
	if(${xuequ}){
		for(var i=0;i<xuequList.length;i++){
			initXueQu(xuequList[i]);
		}	
	}
	
});

    function initXueQu(xq){
    	var arr = xq.center.split(',');
    	var point = new BMap.Point(arr[0] , arr[1]);
    	var label = new BMap.Label(xq.name,{position : point});
        var color = getRandomColor();
    	label.setOffset(new BMap.Size(xq.offsetX, xq.offsetY));
    	label.xqid = xq.id;
    	label.setStyle({cursor:"pointer",border:"none" , background:"none" , color:"red"});
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
    	
    	polygon.setStrokeColor(color);
    	polygon.setFillColor(color);
    	polygon.xqid = xq.id;
    	map.addOverlay(polygon);
    }
    var colors = ["red" , "blue" , "black","green" , "yellow"];
    function getRandomColor(){
    	var index = Math.floor(Math.random()*5);
    	return colors[index];
    }
</script>
<style type="text/css">
.allmap{position: relative;}
.tipbox{ position: absolute; top: 70px; right: 15px; border: 2px solid #09F; background-color: #FFF; border-radius: 6px; padding:0 20px; box-shadow: 5px 5px 20px #000;  color: #09F; height: 26px; line-height: 26px; }
.tips{ position: relative; }
.tips:before, .tips:after { 
content: ' '; 
height: 0; 
position: absolute; 
width: 0; 
border: 10px solid transparent;
}
.tips:before { 
border-bottom-color: #fff;
position: absolute; 
top: -19px; 
right: 3px; 
z-index: 2; 
} 
.tips:after { 
border-bottom-color: #09F;
position: absolute; 
top: -22px; 
right: 3px; 
z-index: 1; 
} 
</style>
	<title>地图找房</title>
</head>
<body>
	<div id="allmap" style="overflow:hidden;zoom:1;position:relative;width: 100%; height: 100%;">
		<div id="map" style="height:100%;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
		<div class="tipbox"><div class="tips">框选找房</div></div>
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
    var result = e.overlay.Qe['3'];
    //var lt=e.overlay.Rn[0]
  //01
  //32
  //alert(result[0].lng)
  var lngStart = result[0].lng;
  var lngEnd = result[2].lng;
  var latStart = result[2].lat;
  var latEnd = result[0].lat;
  if(lngStart>lngEnd){
		var tmp = lngStart;
		lngStart = lngEnd;
		lngEnd = tmp;
	}
  if(latStart>latEnd){
		var tmp = latStart;
		latStart = latEnd;
		latEnd = tmp;
	}
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
    
    map.centerAndZoom(poi, 13);
    //再定位一次,artdialog有问题
    setTimeout(function(){
    	if(${lngStart} && ${latStart}){
    		var rectangle = new BMap.Polygon([
    			                          		new BMap.Point(${lngStart},${latEnd}),
    			                          		new BMap.Point(${lngEnd},${latEnd}),
    			                          		new BMap.Point(${lngEnd},${latStart}),
    			                          		new BMap.Point(${lngStart},${latStart})
    			                          	], {strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5});  //创建矩形
    			map.addOverlay(rectangle);
    	}
    	map.centerAndZoom(poi, 13);
    },500);
</script>
</body>
</html>
