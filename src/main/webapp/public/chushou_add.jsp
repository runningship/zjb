<%@page import="com.youwei.zjb.house.SellState"%>
<%@page import="com.youwei.zjb.KeyConstants"%>
<%@page import="org.bc.web.ThreadSession"%>
<%@page import="com.youwei.zjb.house.entity.HouseOwner"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="com.youwei.zjb.sys.CityService"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="com.youwei.zjb.house.FangXing"%>
<%@page import="com.youwei.zjb.house.LouXing"%>
<%@page import="com.youwei.zjb.house.ZhuangXiu"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%

	String citypy=ThreadSessionHelper.getCityPinyin();
	CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	HouseOwner user = (HouseOwner)ThreadSession.getHttpSession().getAttribute(KeyConstants.Session_House_Owner);
	request.setAttribute("user", user);

	CityService cityService = new CityService();
	JSONArray citys = cityService.getCitys();
	for(int i=0;i<citys.size();i++){
		JSONObject city = citys.getJSONObject(i);
		if(city.getString("py").equals(citypy)){
			request.setAttribute("quyus",city.getJSONArray("quyu"));
		}
	}
	request.setAttribute("zxius", ZhuangXiu.toJsonArray());
	request.setAttribute("lxings", LouXing.toJsonArray());
	request.setAttribute("hxings", FangXing.toJsonArray());
	request.setAttribute("ztais", SellState.toJsonArray());
%>
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
<script type="text/javascript" src="js/buildHtml.js"></script>
<script type="text/javascript" src="js/javascript.js"></script>
<script type="text/javascript">
function save(){
   var a=$('form[name=form1]').serialize();
   YW.ajax({
   type: 'POST',
   url: '/c/weixin/houseOwner/addHouse',
   data:a,
   mysuccess: function(data){
		var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
		parent.reloadWindow();
		parent.layer.close(index); //再执行关闭   
       }
   });
}

$(document).on('click', '.btn_act', function(event) {
    var Thi=$(this),
    ThiType=Thi.data('type');
    if(ThiType=='submits'){
        save();
    }
    event.preventDefault();
    /* Act on the event */
});

</script>
</head>
<body>
<div class="form-house">
<form action="" name="form1">
    <table class="">
        <tr>
            <td width="50%">
              
    <ul class="form-ul forms_reg">
      <li class=""><label class="form-section form-active"><strong class="input-label">楼盘</strong><input type="text" class="input placeholder u" name="area" desc="楼盘名称" placeholder="楼盘名称"></label></li>

      <li class=""><label class="form-section form-active"><strong class="input-label">地址</strong><input type="text" class="input placeholder u" name="address" desc="楼盘地址" placeholder="楼盘地址"></label></li>

      <li class=""><label class="form-section form-section-tow one form-active"><strong class="input-label">栋号</strong><input type="text" name="dhao" class="input placeholder u" desc="栋号" placeholder="栋号"></label>
      <label class="form-section form-section-tow form-active"><strong class="input-label">房号</strong><input type="text" name="fhao" class="input placeholder u" desc="房号" placeholder="房号"></label></li>

      <li class=""><label class="form-section form-section-tow one form-active"><strong class="input-label">楼层</strong><input type="text" name="lceng" class="input placeholder u" desc="楼层" placeholder="楼层"></label>
      <label class="form-section form-section-tow form-active"><strong class="input-label">总层</strong><input type="text" name="zceng" class="input placeholder u" desc="总层" placeholder="总层"></label></li>

      <li class=""><label class="form-section form-section-tow one form-active"><strong class="input-label">面积</strong><input type="text" name="mji" class="input placeholder u" desc="面积" placeholder="面积"><span class="tip">M<sub>2</sub></span></label>
      <label class="form-section form-section-tow form-active"><strong class="input-label">总价</strong><input type="text" name="zjia" class="input placeholder u" desc="总价" placeholder="总价"><span class="tip">万元</span></label></li>

      <li class=""><label class="form-section form-active"><strong class="input-label">年代</strong><input type="text" name="dateyear" class="input placeholder u" desc="楼盘年代" placeholder="楼盘年代"></label></li>

      <li class=""><label class="form-section form-section-tow one w4 form-active"><strong class="input-label">房主姓名</strong><input type="text" name="lxr" class="input placeholder u" desc="房主姓名" placeholder="房主姓名"></label>
      <label class="form-section form-section-tow w6 form-active"><strong class="input-label">房主号码</strong><input type="text" name="tel" class="input placeholder u" desc="房主号码" placeholder="房主号码" value="${user.tel}"></label></li>


    </ul>

            </td>
            <td>
              
    <ul class="form-ul forms_reg">
      <li class=""><label class="form-section form-active"><strong class="input-label">状态</strong><select name="ztai" id="" class="input">
        <c:forEach items="${ztais}" var="ztai">
        	<option value="${ztai.name}">${ztai.name}</option>
        </c:forEach>
      </select></label></li>
      <li class=""><label class="form-section form-active"><strong class="input-label">区域</strong><select name="quyu" id="" class="input">
        <c:forEach items="${quyus}" var="quyu">
        	<option value="${quyu.name}">${quyu.name}</option>
        </c:forEach>
      </select></label></li>
      <li class=""><label class="form-section form-active"><strong class="input-label">楼型</strong><select name="lxing" id="" class="input">
        <c:forEach items="${lxings }" var="lxing">
        	<option value="${lxing.name}">${lxing.name}</option>
        </c:forEach>
      </select></label></li>
      <li class=""><label class="form-section form-active"><strong class="input-label">户型</strong><select name="hxing" id="" class="input">
        <c:forEach items="${hxings }" var="hxing">
        	<option value="${hxing.name}">${hxing.name}</option>
        </c:forEach>
      </select></label></li>
      <li class=""><label class="form-section form-active"><strong class="input-label">装潢</strong><select name="zxiu" id="" class="input">
        <c:forEach items="${zxius }" var="zxiu">
        	<option value="${zxiu.name}">${zxiu.name}</option>
        </c:forEach>
      </select></label></li>

      <li class=""><label class="form-section form-active"><strong class="input-label">备注</strong><textarea class="input placeholder" name="beizhu" id="" cols="30" rows="4" placeholder="备注其他信息"></textarea></label></li>

      <li class="">
        <a href="#" class="btn btn_act btn_block blue" data-type="submits">提交</a>
        <input type="submit" class="submit  hidden" value="submit">
      </li>
    </ul>

            </td>
        </tr>
    </table>
</form>
</div>
<script type="text/javascript">
</script>
</body>
</html>