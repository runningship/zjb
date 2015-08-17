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
<jsp:include page="data2.jsp"></jsp:include>
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
<script type="text/javascript">
</script>
</head>
<body>
<div class="bodyer">
    <div class="header">
		<jsp:include page="top.jsp?type=chuzu"></jsp:include>
        <div class="search">
        <form class="form" action="chuzu.jsp" id="searchForm">
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
                      <li><label><input type="checkbox" class="check" name="lxings" <c:if test="${s_lxings.contains(lxing.name)}">checked="checked"</c:if> value="${lxing.name}">${lxing.name}</label></li>
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
                    <strong class="">方式<em class="iconRight"><i class="iconfont">&#xe60f;</i></em></strong>
                    <ul class="more ">
                      <li><label><input type="radio" class="check" name="fangshi" <c:if test="${s_fangshi eq null}">checked="checked"</c:if> value="">不限</label></li>
					  <c:forEach items="${fangshis }" var="fangshi"  varStatus="status">
                      <li><label><input type="radio" class="check" name="fangshi" <c:if test="${s_fangshi == fangshi.code}">checked="checked"</c:if> value="${fangshi.code}">${fangshi.name}</label></li>
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
                <a href="#" class="btn btns btn_act search" data-type="submit">搜索</a>
                <input type="submit" class="hidden submit" value="submit">
            </div>
            <div class="wrap">
                <span class="searchItem w15">
                    <input type="text" class="input" placeholder="路段" name="address" value="${address}">
                </span>
                <span class="searchItem w15">
                    <label class="inputTit" for="mji">面积</label>
                    <div class="inputBox">
                        <input type="text" id="mji" class="input" placeholder="" maxlength="3" name="mjiStart" value="${mjiStart}">
                    </div>
                    <span class="inputLab">-</span>
                    <div class="inputBox">
                        <input type="text" class="input" placeholder="" maxlength="4" name="mjiEnd" value="${mjiEnd}">
                    </div>
                </span>
                <span class="searchItem w20">
                    <label class="inputTit" for="zjia">租金</label>
                    <div class="inputBox">
                        <input type="text" id="zjia" class="input" placeholder="" maxlength="3" name="zjiaStart" value="${zjiaStart}">
                    </div>
                    <span class="inputLab">-</span>
                    <div class="inputBox">
                        <input type="text" class="input" placeholder="" maxlength="3" name="zjiaEnd" value="${zjiaEnd}">
                    </div>
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
                <a href="list.jsp" class="btn btns empty">清空</a>

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

            <div class="listgroup">
                <table border="0" cellspacing="0" cellpadding="0" class="tableList2 table-hover"> 
                    <tbody>
						<c:forEach items="${list }" var="house"  varStatus="status">
                        <tr data-hid="1" class="a${status.index%7} active"> 
                            <th>
                                <h2>
                                    <span class="icons">
                                    <i class="iconfont my <c:if test="${house_owner.tel != house.tel || house_owner.tel == null}">no</c:if>" data-class="no" value="${house.tel }" title="我的房子">&#xe60d;</i>
                                    </span>
                                    ${house.area}
                                    <span class="icons">
                                        <i class="iconfont collect <c:if test="${!fn:contains(buyFavStr, ','.concat(house.id.toString()).concat(','))}"> no </c:if> btn_act" data-type="SC"  cuzu="0" uid="${house_owner.id }" hid="${house.id }" title="点我收藏">&#xe60c;</i>
                                    </span>
                                    <span class="bhao">编号：${house.id}</span>
                                </h2>
                                <p class="xq">
                                    <span>${house.hxf}室${house.hxt}厅${house.hxw}卫</span>
                                    <span>${house.mji}㎡</span>
                                    <span>${house.lxing} ${house.zxiu}</span>
                                    <span>${house.lceng}层 总层${house.zceng}</span>
                                </p>
                                <p class="dz"><span>${house.quyu} ${house.address} </span> </p>
                            </th> 
                            <td>
                                <p class="kong">&nbsp;</p>
                                <p class="zjia"><b>${house.zjia}</b> 元/月</p>
                            </td> 
                            <td>
                                <p class="kong">&nbsp;</p>
                                <p class="kong">&nbsp;</p>
                                <span class="time"><fmt:formatDate value="${house.dateadd}" pattern="yyyy-MM-dd"/></span>
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
<div class="">
    <div class="loginbox hidden">
      <div class="form-box">
        <ul class="form-ul forms_login">
          <li class=""><label class="form-loo form-active"><strong class="input-label"><i class="iconfont">&#xe600;</i></strong><input id="tel_input" type="text" class="input u" placeholder="用户名/手机"></label></li>
          <li class=""><label class="form-loo form-active"><strong class="input-label"><i class="iconfont">&#xe601;</i></strong><input type="password" class="input p" placeholder="密码"></label></li>
          <li class="">
            <a href="#" class="btn btn_act btn_block blue" data-type="submit_login">登陆</a>
            <input type="submit" class="submit hidden" value="submit">
          </li>
          <li class="">
            <a href="#" class="btn_act btn_link" data-type="getPwds">忘记密码</a>
            <a href="#" class="btn_act btn_link fr" data-type="reg">立即注册</a>
          </li>
        </ul>
      </div>
    </div>
    <div class="regbox hidden">
      <div class="form-box">
        <ul class="form-ul forms_reg">
          <li class=""><label class="form-section form-active"><strong class="input-label">手机号</strong><input type="text" class="input placeholder u" placeholder="您的手机号码"></label></li>
          <li class=""><label class="form-section tow form-active"><strong class="input-label">验证码</strong><input type="text" class="input placeholder c" placeholder="收到的验证码"><a href="#" class="btn btn_act code" data-type="regCode" data-txt="发送验证码">发送验证码</a></label></li>
          <li class=""><label class="form-section form-active"><strong class="input-label">密码</strong><input type="password" class="input placeholder p" placeholder="登陆密码"></label></li>
          <li class=""><label class="form-section form-active"><strong class="input-label">重复密码</strong><input type="password" class="input placeholder p2" placeholder="重复输入密码"></label></li>
          <li class="">
            <a href="#" class="btn btn_act btn_block blue" data-type="submit_reg">注册</a>
            <input type="submit" class="submit hidden" value="submit">
          </li>
          <li class="">
            <a href="#" class="btn_act btn_link fr" data-type="login">立即登陆</a>
          </li>
        </ul>
      </div>
    </div>
    <div class="citybox hidden">
      <ul>
<!--         <li class="active"><a href="#" ><i  class="iconfont">&#xe607;</i> 合肥</a></li> -->
        <c:forEach items="${citys}" var="city">
        	<c:if test="${city.status eq 'on' }">
       			<li><a py="${city.py }" href="javascript:void(0)" onclick="switchCity('${city.py}' , '${city.name }' );">${city.name }</a></li>
       		</c:if>
       	</c:forEach>
      </ul>
    </div>
</div>
<script type="text/javascript">
$(document).ready(function() {
    $(document).find('.form-active').find('.input').focusin(function(){
        $(this).parent().addClass('active').addClass('focus');
    }).focusout(function(){
        if(!$(this).val()){
            $(this).parent().removeClass('active');
        }
        $(this).parent().removeClass('focus');
    }).hover(function() {
        $(this).parent().addClass('hover');
    }, function() {
        $(this).parent().removeClass('hover');
    });
});
</script>
</body>
</html>