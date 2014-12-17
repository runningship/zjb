<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="warp w_headerBg">
    <div class="conWarp">
         
         <span class="logo"><a href="#"><img src="images/logo.png" /></a></span>
         <span class="hotline"><img src="images/hotLine.png" /></span>
         
    </div>     
</div>

<div class="warp nav">
    
    <ul class="conWarp navUl">
         
        <li><a href="index.jsp"  id="index">首页</a></li>
    <c:forEach items="${modules}"  var="module">
        <li>
        	<a <c:if test="${module.id==topBoard.id}">class="s"</c:if> href="newsList.jsp?topId=${module.id}">${module.name}</a>
            <div class="down_menu" style="display:none;">
                <span class=" down_menu_head"></span>
                <span class="down_menu_con">
                    <c:forEach items="${module.children}"  var="lanmu">
                        <a href="newsList.jsp?topId=${module.id}&bid=${lanmu.id}">${lanmu.name}</a>
                    </c:forEach>
                </span>
                <span class="down_menu_foot"></span>
            </div>
        </li>
    </c:forEach>
         
    </ul>
    
</div>
