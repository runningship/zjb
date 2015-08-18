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
<div class="">
    <div class="loginbox hidden">
      <div class="form-box">
        <ul class="form-ul forms_login">
          <li class=""><label class="form-loo form-active"><strong class="input-label"><i class="iconfont">&#xe600;</i></strong><input id="tel_input" type="text" class="input u" placeholder="用户名/手机"></label></li>
          <li class=""><label class="form-loo form-active"><strong class="input-label"><i class="iconfont">&#xe601;</i></strong><input type="password" class="input p" placeholder="密码" data-keysubmit="true" data-keysubmitto=".submit_login"></label></li>
          <li class="">
            <a href="#" class="btn btn_act btn_block blue submit_login" data-type="submit_login">登录</a>
            <input type="submit" class="submit  hidden" value="submit">
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
          <li class=""><label class="form-section form-active"><strong class="input-label">密码</strong><input type="password" class="input placeholder p" placeholder="登录密码"></label></li>
          <li class=""><label class="form-section form-active"><strong class="input-label">重复密码</strong><input type="password" class="input placeholder p2" placeholder="重复输入密码"></label></li>
          <li class="">
            <a href="#" class="btn btn_act btn_block blue" data-type="submit_reg">注册</a>
            <input type="submit" class="submit hidden" value="submit">
          </li>
          <li class="">
            <a href="#" class="btn_act btn_link fr" data-type="login">立即登录</a>
          </li>
        </ul>
      </div>
    </div>
    <div class="getPwdbox hidden">
      <div class="form-box">
        <ul class="form-ul forms_pwds">
          <li class=""><label class="form-section form-active"><strong class="input-label">手机号</strong><input type="text" class="input placeholder u" placeholder="您的手机号码"></label></li>
          <li class=""><label class="form-section tow form-active"><strong class="input-label">验证码</strong><input type="text" class="input placeholder c" placeholder="收到的验证码"><a href="#" class="btn btn_act code" data-type="reset_pwd_code" data-txt="发送验证码">发送验证码</a></label></li>
          <li class=""><label class="form-section form-active"><strong class="input-label">密码</strong><input type="password" class="input placeholder p" placeholder="登录密码"></label></li>
          <li class="">
            <a href="#" class="btn btn_act btn_block blue" data-type="reset_pwd">重置密码</a>
            <input type="submit" class="submit hidden" value="submit">
          </li>
          <li class="">
            <a href="#" class="btn_act btn_link fr" data-type="login">登录</a>
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