<%@page import="com.youwei.zjb.KeyConstants"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="com.youwei.zjb.sys.entity.City"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.youwei.zjb.sys.CityService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="data.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝</title>
<meta name="description" content="">
<meta name="keywords" content="">
<link href="css/reset.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/layer/layer.js"></script>
<script type="text/javascript" src="js/jQuery.resizeEnd.min.js"></script>
<script type="text/javascript" src="js/javascript.js"></script>
<script type="text/javascript" src="js/buildHtml.js"></script>
<script type="text/javascript" src="js/list.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript">
function reloadWindow(){
	window.location.reload();
}

</script>
</head>
<body>
<div class="bodyer">
    <div class="header">
        <jsp:include page="top.jsp?type=chushou"></jsp:include>
        <div class="search">
        <form class="form" action="chushou.jsp" id="searchForm">
        	<input type="hidden" name="action" id="action"  value="${action }"/>
        	<input type="hidden" name="currentPageNo" id="currentPageNo"/>
            <div class="wrap">
                <span class="searchItem">
                    <input type="text" class="input" placeholder="楼盘名称" name="area" value="${area}">
                    
                </span>
                <span class="searchItem selects">
                    <strong class="">区域 <em class="iconRight"><i class="iconfont">&#xe60f;</i></em></strong>
                    <ul class="more ">
                      <li>
                        <div class="btnbox">
                            <a href="#" class="btna btn_act"><i class="iconfont">&#xe606;</i></a>
                            <a href="#" class="btna btn_act bor"><i class="iconfont">&#xe606;</i></a>
                            <a href="#" class="btna btn_act"><i class="iconfont">&#xe606;</i></a>
                        </div>
                      </li>
					  <c:forEach items="${quyus }" var="quyu"  varStatus="status">
                      <li><label><input type="checkbox" class="check" name="quyus" <c:if test="${s_quyus.contains(quyu.name)}">checked="checked"</c:if> value="${ quyu.name}">${quyu.name}</label></li>
                      </c:forEach>
                    </ul>
                </span>
                <span class="searchItem selects">
                    <strong class="">楼型 <em class="iconRight"><i class="iconfont">&#xe60f;</i></em></strong>
                    <ul class="more ">
					  <c:forEach items="${lxings }" var="lxing"  varStatus="status">
                      <li><label><input type="checkbox" class="check" name="lxings" <c:if test="${s_lxings.contains(','.concat(lxing.name).concat(','))}">checked="checked"</c:if> value="${lxing.name}">${lxing.name}</label></li>
                      </c:forEach>
                    </ul>
                </span>
                <span class="searchItem selects">
                    <strong class="">户型 <em class="iconRight"><i class="iconfont">&#xe60f;</i></em></strong>
                    <ul class="more ">
					  <c:forEach items="${hxings }" var="hxing"  varStatus="status">
                      <li><label><input type="checkbox" class="check" name="hxings" <c:if test="${s_hxings.contains(hxing.value)}">checked="checked"</c:if> value="${hxing.value}">${hxing.name}</label></li>
                      </c:forEach>
                    </ul>
                </span>
                <span class="searchItem selects">
                    <strong class="">装潢 <em class="iconRight"><i class="iconfont">&#xe60f;</i></em></strong>
                    <ul class="more ">
					  <c:forEach items="${zxius }" var="zxiu"  varStatus="status">
                      <li><label><input type="checkbox" class="check" <c:if test="${s_zxius.contains(zxiu.name)}">checked="checked"</c:if> name="zxius" value="${zxiu.name}">${zxiu.name}</label></li>
                      </c:forEach>
                    </ul>
                </span>
                <span class="searchItem">
                    <input type="text" class="input" placeholder="路段" name="address" value="${address}">
                </span>
                <a href="#" class="btn btns btn_act search" data-type="submit">搜索</a>
                <input type="submit" class="hidden submit" value="submit">
            </div>
            <div class="wrap">
                <span class="searchItem w15">
                    <label class="inputTit" for="mji">面积</label>
                    <div class="inputBox">
                        <input type="text" id="mji" class="input" placeholder="" maxlength="3" name="mjiStart" value="${mjiStart}">
                    </div>
                    <span class="inputLab">-</span>
                    <div class="inputBox">
                        <input type="text" class="input" placeholder="" maxlength="4" name="mjiEnd" value="${mjiEnd}">
                    </div><span class="tip">㎡</span>​
                </span>
                <span class="searchItem w20">
                    <label class="inputTit" for="zjia">总价</label>
                    <div class="inputBox">
                        <input type="text" id="zjia" class="input" placeholder="" maxlength="3" name="zjiaStart" value="${zjiaStart}">
                    </div>
                    <span class="inputLab">-</span>
                    <div class="inputBox">
                        <input type="text" class="input" placeholder="" maxlength="3" name="zjiaEnd" value="${zjiaEnd}">
                    </div><span class="tip">​万​</span>​
                </span>
                <span class="searchItem w20">
                    <label class="inputTit" for="djia">单价</label>
                    <div class="inputBox">
                        <input type="text" id="djia" class="input" placeholder="" maxlength="4" name="djiaStart" value="${djiaStart}">
                    </div>
                    <span class="inputLab">-</span>
                    <div class="inputBox">
                        <input type="text" class="input" placeholder="" maxlength="4" name="djiaEnd" value="${djiaEnd}">
                    </div><span class="tip">​元​</span>​
                </span>
                <span class="searchItem w15">
                    <label class="inputTit" for="lceng">楼层</label>
                    <div class="inputBox">
                        <input type="text" id="lceng" class="input" placeholder="" maxlength="2" name="lcengStart" value="${lcengStart}">
                    </div>
                    <span class="inputLab">-</span>
                    <div class="inputBox">
                        <input type="text" class="input" placeholder="" maxlength="2" name="lcengEnd" value="${lcengEnd}">
                    </div>
                </span>
                <a href="#" class="btn btns empty btn_act"  data-type="empty">清空</a>

            </div>
            </form>
        </div>
        <div class="searchLine">
            <div class="wrap">
            </div>
        </div>
    </div>
    <div class="mainer">
        <div class="wrap">
			<c:if test="${totalCount == 0}">
            <div class="nogroup">
                <div class="noIcon" value="${totalCount}"><i class="iconfont">&#xe629;</i></div>
                <div class="noText">
                    <h2>没找到你要的，换个搜索条件试试！</h2>
                    <p>真的没找到</p>
                </div>
            </div>
			</c:if>
            <div class="listgroup">
                <table border="0" cellspacing="0" cellpadding="0" class="tableList2 table-hover"> 
                    <tbody>
						<c:forEach items="${list }" var="house"  varStatus="status">
                        <tr data-hid="${house.id }" class="a${status.index%7} active"> 
                            <th>
                                <h2>
                                    <span class="icons">
                                    <i class="iconfont my <c:if test="${house_owner.tel != house.tel || house_owner.tel == null}">no</c:if>" data-class="no" value="${house.tel }" title="我的房子">&#xe60d;</i>
                                    </span>
                                    ${house.area}
                                    <span class="icons">
                                        <i class="iconfont collect <c:if test="${!fn:contains(buyFavStr, ','.concat(house.id.toString()).concat(','))}"> no </c:if> btn_act" data-type="SC"  cuzu="0" uid="${house_owner.id }" hid="${house.id }" title="点我收藏">&#xe60c;</i>
                                        <c:if test="${house_owner.tel == house.tel && house_owner.tel != null}">
                                        <i class="iconfont edit  btn_act" data-type="editHouse" title="修改">&#xe624;</i>
                                        <i class="iconfont del  btn_act" data-type="delHouse" title="删除">&#xe613;</i>
                                        </c:if>
                                    </span>
                                    <span class="bhao">编号：${house.id}</span>
                                </h2>
                                <p class="xq">
                                    <span>${house.hxf}室${house.hxt}厅${house.hxw}卫</span>
                                    <span><fmt:formatNumber  value="${house.mji}"  type="number"  pattern="###.##" />㎡</span>
                                    <span>${house.lxing}</span>
                                    <span>${house.zxiu}</span>
                                    <span>${house.lceng}层 总层${house.zceng}</span>
                                </p>
                                <p class="dz"><span>${house.quyu} ${house.address} </span> </p>
                            </th> 
                            <td>
                                <p class="kong">&nbsp;</p>
                                <p class="zjia"><b><fmt:formatNumber  value="${house.zjia}"  type="number"/></b> 万</p>
                                <p class="hx"><fmt:formatNumber  value="${house.djia}"  type="number"  pattern="#####.#" />元/㎡</p>
                            </td> 
                            <td>
                                <p class="kongs">&nbsp;</p>
                                <p class=""><a href="#" class="btn btn_act seePhone blue" data-type="seePhone"><i class="iconfont"></i> 联系电话</a></p>
                                <span class="time" title="发布时间"><fmt:formatDate value="${house.dateadd}" pattern="yyyy-MM-dd hh:mm"/></span>
                            </td> 
                        </tr>
                            </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    
    <div class="footer">
    	<jsp:include page="pagination.jsp"></jsp:include>
    </div>
</div>
<jsp:include page="common.jsp"></jsp:include>
</body>
</html>