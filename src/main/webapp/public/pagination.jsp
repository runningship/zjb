<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.youwei.zjb.sys.CityService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	
%>
<script type="text/javascript">
function goPage(pageNo){
	$('#currentPageNo').val(pageNo);
	$('#searchForm').submit();
}
</script>
<div class="wrap">
			<h3 class="fr">轻量化房产生态链平台</h3>
		    <ul class="pageList">
		    	<li><a href="javascript:void(0)" >${p.totalCount }</a></li>
		    	<li><a href="javascript:void(0)" >${p.currentPageNo }/${p.totalPageCount }</a></li>
		        <li><a href="javascript:void(0)" onclick="goPage(1)">首页</a></li>
		        <li><a href="javascript:void(0)" onclick="goPage(${p.currentPageNo-1})">上一页</a></li>
		        <c:if test="${p.currentPageNo-6>1 }">
		        <li>...</li>
		        </c:if>
		        <c:forEach var="offset" begin="1" end="6" step="1">
		        	<c:if test="${p.currentPageNo+offset-6>0 }">
		        	<li <c:if test="${offset==6}">class="active"</c:if> ><a href="javascript:void(0)" onclick="goPage(${p.currentPageNo-(6-offset)})">${p.currentPageNo -(6-offset)}</a></li>	
		        	</c:if>
		        </c:forEach>
		        <c:forEach var="offset" begin="1" end="6" step="1">
		        	<c:if test="${p.currentPageNo+offset<=p.totalPageCount }">
		        	<li><a href="javascript:void(0)" onclick="goPage(${p.currentPageNo+offset})">${p.currentPageNo +offset}</a></li>
		        	</c:if>	
		        </c:forEach>
		         <c:if test="${p.currentPageNo+6<p.totalPageCount }">
		        	<li>...</li>
		        </c:if>
		        <c:if test="${p.currentPageNo<p.totalPageCount }">
		        <li><a href="javascript:void(0)" onclick="goPage(${p.currentPageNo+1})">下一页</a></li>
		        <li><a href="javascript:void(0)" onclick="goPage(${p.totalPageCount})">尾页</a></li>
		        </c:if>
		    </ul>
		</div>
		<div class="hidden">
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");document.write(unescape("%3Cspan id='cnzz_stat_icon_1256116529'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s4.cnzz.com/z_stat.php%3Fid%3D1256116529%26show%3Dpic1' type='text/javascript'%3E%3C/script%3E"));</script>
		</div>