<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="menuTop" style="display:inline-block;">
      <ul class="MainRightTop KY_W titlebar">
          <li class="slect nobar" onclick="window.location='/house/house_v2.jsp'"><i class="i1"></i>出售</li>
          <li class="line"></li>
          <li class="nobar" onclick="window.location='/house/house_rent_v2.jsp'"><i class="i2"></i>出租</li>
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
          <li class="MenuBox nobar" style="position:relative;">
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
