<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
request.setAttribute("cityCordinate", ThreadSessionHelper.getCityCordinate());
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<title>无标题文档</title>
<style type="text/css">
html, body, .body {height: 100%;padding: 0;margin: 0;}
.errTip{ position:absolute; top:40%; left:30%; z-index:999; background:#F60; color:#FFF; height:100px; width:300px; line-height:100px; text-align:center; font-size:30px; font-family:"微软雅黑","黑体"; display:none;}
</style>
<script type="text/javascript">
function getParam(name){
var reg = new RegExp("(^|\\?|&)"+ name +"=([^&]*)(\\s|&|$)", "i");
return (reg.test(location.search))? encodeURIComponent(decodeURIComponent(RegExp.$2.replace(/\+/g, " "))) : '';
}
</script>
</head>

<body>
<div class="body">
<div id="dituContent" style="margin:0px auto;text-aglin:center;width:100%;height:100%;border:0px solid #666; text-align:center;"></div>
<div class="errTip" id="errTip">
(ㄒ_ㄒ) 暂未找到...
</div>
</div>

</style>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=9ad26b763c7cd0619e372f993cdc9849"></script>
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
<script type="text/javascript">
var areas=getParam('area');
var map = new BMap.Map("dituContent",{minZoom:10,maxZoom:21});            // 创建Map实例
map.centerAndZoom(new BMap.Point(${cityCordinate}), 15);
// map.centerAndZoom(new BMap.Point(118.402505,31.339376), 15);

// 初始化地图,设置中心点坐标和地图级别。
window.openInfoWinFuns;

var options = {
  selectFirstResult: false,
  isAccurate:false,
  onSearchComplete: function(results){
    if (local.getStatus() == BMAP_STATUS_SUCCESS){// 判断状态是否正确
        openInfoWinFuns = [];
        for (var i = 0; i < results.getCurrentNumPois(); i ++){
            var marker = addMarker(results.getPoi(i).point,i);
            var openInfoWinFun = addInfoWindow(marker,results.getPoi(i),i);
            openInfoWinFuns.push(openInfoWinFun);
            // 默认打开第一标注的信息窗口
            var selected = "";
            if(i == 0){
                selected = "background-color:#f0f0f0;";
                openInfoWinFun();
            }
        }
    }else{
		  document.getElementById("errTip").style.display="block";
      // window.location='house_see_map.html?area='+areas;
	  }
  }
};
// 添加标注
function addMarker(point, index){
  var myIcon = new BMap.Icon("http://api.map.baidu.com/img/markers.png", new BMap.Size(23, 25), {
    offset: new BMap.Size(10, 25),
    imageOffset: new BMap.Size(0, 0 - index * 25)
  });
  var marker = new BMap.Marker(point, {icon: myIcon});
  map.addOverlay(marker);
  return marker;
}
// 添加信息窗口
function addInfoWindow(marker,poi,index){
	var name = "";
    if(poi.type == BMAP_POI_TYPE_NORMAL){
        name = "地址:"
    }else if(poi.type == BMAP_POI_TYPE_BUSSTOP){
        name = "公交:"
    }else if(poi.type == BMAP_POI_TYPE_SUBSTOP){
        name = "地铁:"
    }
	poi_address="<b>"+name+"</b> "+poi.address
//	poi.title
/*	var searchInfoWindow = new BMap.InfoWindow(infoWindowHtml.join(""),{title:infoWindowTitle,width:200}); 
*/	searchInfoWindow = new BMapLib.SearchInfoWindow(map, poi_address, {
		title  : poi.title,      //标题
		width  : 290,             //宽度
		// height : 105,              //高度
		panel  : "panel",         //检索结果面板
		enableAutoPan : true,     //自动平移
		enableSendToPhone: true, //是否启用发送到手机
		searchTypes   :[
			//BMAPLIB_TAB_SEARCH,   //周边检索
			BMAPLIB_TAB_TO_HERE,  //到这里去
			BMAPLIB_TAB_FROM_HERE //从这里出发
		]
	});
	var openInfoWinFun = function(){
		searchInfoWindow.open(marker);
		map.panTo(new BMap.Point(marker.getPosition()));
    }
    marker.addEventListener("click", openInfoWinFun);
    return openInfoWinFun;
}
// map.setCurrentCity("$${city}");          // 设置地图显示的城市 此项是必须设置的
var local = new BMap.LocalSearch(map,options);
local.search(areas);

map.addControl(new BMap.NavigationControl());               // 添加平移缩放控件
map.addControl(new BMap.ScaleControl());                    // 添加比例尺控件
map.enableScrollWheelZoom();                            //启用滚轮放大缩小
map.addControl(new BMap.MapTypeControl({mapTypes: [BMAP_NORMAL_MAP,BMAP_HYBRID_MAP]}));          //添加地图类型控件

// map.addControl(new BMap.OverviewMapControl());              //添加默认缩略地图控件
// map.addControl(new BMap.OverviewMapControl({isOpen:true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT}));   //右下角，打开

//map.addControl(new BMap.MapTypeControl());          //添加地图类型控件

map.addEventListener("click", function(e){    
 //alert('经度:'+e.point.lng+',维度:'+e.point.lat);
});
</script>

</div>
</body>
</html>