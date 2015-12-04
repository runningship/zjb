<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("type", request.getParameter("type"));
%>
<script type="text/javascript">
function _open(url){
	window.top.beforeIframeChange('iframe_house' , url);
}
</script>
<div id="menuTop" style="display:inline-block;">
      <ul class="MainRightTop KY_W titlebar">
          <li class=" <c:if test="${type eq 'chushou' }">slect</c:if>   nobar" onclick="_open('/house/house_v2.jsp?type=chushou')"><i class="i1"></i>出售</li>
          <li class="line"></li>
          <li class=" <c:if test="${type eq 'chuzu' }">slect</c:if> nobar" onclick="window.location='/house/house_rent_v2.jsp?type=chuzu'"><i class="i2"></i>出租</li>
          <li class="line"></li>
          <li class="MenuBox nobar" style="position:relative;" onclick="openAddHouse('/house/house_add.jsp?act=add&chuzu=0')">
               <i class="i3" ></i>登记
               <div class="topMenuChid">
                    <span></span>
                    <a href="javascript:void(0)" onclick="openAddHouse('/house/house_add.jsp?act=add&chuzu=0')">出售登记</a> 
                    <a href="javascript:void(0)" onclick="openAddHouse('/v/house/house_rent_add.html?act=add&chuzu=1')">出租登记</a> 
               </div>
          </li>
          <li class="line"></li>
          <li class="MenuBox nobar <c:if test="${type eq 'my' }">slect</c:if>" style="position:relative;">
               <i class="i4"></i>我的
               <div class="topMenuChid">
                    <span></span>
                    <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=favShou&chuzus=0'">我收藏的出售</a> 
                    <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=favZu&chuzus=1'">我收藏的出租</a>
                    <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=addShou&chuzus=0'">我发布的出售</a> 
                    <a href="javascript:void(0)" onclick="window.location='/house/house_my_v2.jsp?act=my&flag=addZu&chuzus=1'">我发布的出租</a>  
               </div>
          </li>
          <li class="line"></li>
      </ul>
</div>
<!-- openMaxPic -->
<script src="../js/layer_photo/layer.min.js" type="text/javascript"></script>
<script src="../js/layer_photo/extend/layer.ext.js" type="text/javascript"></script>
<script type="text/javascript">

function openMaxPic(imgJSON){
//此处为异步请求模式，具体的json格式，请等待文档更新。或者你直接通过请求看photos.json
// var conf = {};
// $.getJSON('pic.json', {}, function(json){
//     conf.photoJSON = json; //保存json，以便下次直接读取内存数据
//     layer.photos({
//         //html: '这里传入自定义的html，也可以不用传入（这意味着不会输出右侧区域）。相册支持左右方向键、Esc关闭',
//         htmls:'',
//         json: json
//     });
// });
//imgJSON.start = start;
layer.photos({
       //html: '这里传入自定义的html，也可以不用传入（这意味着不会输出右侧区域）。相册支持左右方向键、Esc关闭',
       htmls:'',
       json: imgJSON
});

}
</script>