<%@page import="org.bc.web.ModelAndView"%>
<%@page import="com.youwei.zjb.sys.ConfigService"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="com.youwei.zjb.user.entity.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
User u = ThreadSessionHelper.getUser();
if(u.cid!=1){
	request.setAttribute("forlxr", u.uname==null ? "": u.uname);
	request.setAttribute("fortel", u.tel==null ? "" : u.tel);
}else{
	request.setAttribute("forlxr", "");
	request.setAttribute("fortel", "");
}
request.setAttribute("cid", u.cid);
ConfigService cs = new ConfigService();
ModelAndView mv = cs.getQueryOptions();
request.setAttribute("data", mv.data);
%>
<jsp:include page="../inc/top.jsp" />
<jsp:include page="../inc/resource.jsp" />
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝房源软件系统</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<link href="${refPrefix}/style/css.css" rel="stylesheet">
<link href="${refPrefix}/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${refPrefix}/style/style.css" rel="stylesheet">
<script src="${refPrefix}/js/jquery.js" type="text/javascript"></script>
<script src="${refPrefix}/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.timers.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.input.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript" src="${refPrefix}/js/buildHtml.js"></script>

<script type="text/javascript">
var id;
var queryOptions;
var apiTitle;
var parent = art.dialog.parent,       // 父页面window对象
api = art.dialog.open.api,  //      art.dialog.open扩展方法
form1=$('.form1');
function save(){
	if(checkDateyear($('#dateyear')) == false){
		  return;
    }
   if(checkTel()==false){
    return;
  }
var a=$('form[name=form1]').serialize();
  if ($('#tel').val()==undefined||$('#tel').val()=='') {
    alert('请填写房主电话');
  }else{
      api.button({
          name: '保存',
          disabled:true
      });
      YW.ajax({
        type: 'POST',
        url: '/c/house/add',
        data:a,
        mysuccess: function(data){
            // $('#saveBtn').removeAttr('disabled');
            art.dialog.close();
            art.dialog.opener.doSearchAndSelectFirst();
            alert('发布成功');
        },
        complete:function(){
            api.button({
                  name: '保存',
                  disabled:false
              });
        }
    });
    }
}


// 操作对话框
api.title('添加房源').size(600, 500).button({
  name: '保存',
  callback: function () {
    save();
    return false;
  },focus: true
},{
  name: '取消'
});

if(getParam('chuzu')=='0' || getParam('chuzu')==''){
    apiTitle='登记出售房源';
}else{
// 出租的信息
    apiTitle='登记出租房源';
}
api.title(apiTitle);

// autocomplete 必加
function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a'),
    ThiAH=ThiA.eq(index);
    ThiA.removeClass('hover');
    var Vals=ThiAH.addClass('hover').attr('area');
    $('#area').val(Vals);
    $('#address').val(ThiAH.data('address'));
    $('#quyu option[value="'+ThiAH.data('quyu')+'"]').attr('selected',true);
}
$(document).on('keydown', function(event) {
    //alert(event.keyCode)
    if(event.ctrlKey&&event.keyCode==13){
        save();
    }
}).on('blur', '#area,#dhao,#fhao', function(event) {
    var blurTime=setTimeout(function(){
        getHouseToo();
    },100);
}).on('focusin', '#area,#dhao,#fhao', function(event) {
    getHouseToo();
}).on('focusout', '#area,#dhao,#fhao', function(event) {
    getHouseToo();
});
$(document).ready(function() {
    $('[data-toggle=tooltip]').tooltip();
// autocomplete
    autoComplete($('#area'))
    
//添加提示框
    queryOptions=JSON.parse('${data}');
    buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
    buildHtmlWithJsonArray("id_hxing",queryOptions['fxing'],true);
    buildHtmlWithJsonArray("id_zxiu",queryOptions['zhuangxiu'],true);
    buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
    buildHtmlWithJsonArray("id_ztai",queryOptions['ztai_sell'],true, true);
    if(${cid}==1){
        $('#seeGX').attr('checked','checked');    
    }
});
</script>
</head>
<body>
<div class="html edit title" style="height:500px; width:620px;">
    <div class="bodyer mainCont">
        <div class="maxHW">

            <form class="form-horizontal form_label_right form1" name="form1" role="form" onsubmit="save();">
                <div class="row">
                    <div class="col-xs-6">

                        <div class="form-group">
                            <label for="area" class="col-xs-4 control-label">楼盘名称:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" desc="楼盘名称" name="area" id="area" placeholder="楼盘名称" tabindex="10" autofocus="autofocus">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address" class="col-xs-4 control-label">楼盘地址:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" desc="楼盘地址" name="address" id="address" placeholder="楼盘地址" tabindex="10">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dhao" class="col-xs-4 control-label">栋号房号:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="" name="dhao" desc="栋号" id="dhao" placeholder="栋号" tabindex="10">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="" name="fhao" desc="房号" id="fhao" placeholder="房号" tabindex="10">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lceng" class="col-xs-4 control-label">楼层总层:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="" name="lceng" desc="楼层" id="lceng" placeholder="楼层" tabindex="10">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="" name="zceng" desc="总层" id="zceng" placeholder="总层" tabindex="10">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mji" class="col-xs-4 control-label">面积总价:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="" desc="面积" name="mji" id="mji" placeholder="面积" tabindex="10">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="" desc="总价" name="zjia" id="zjia" placeholder="总价" tabindex="10">
                                <span style="position: absolute;top: 8px;right: -15px;">万元</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="dateyear" class="col-xs-4 control-label">建筑年代:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" onblur="checkDateyear($('#dateyear'));" name="dateyear" id="dateyear"  desc="建筑年代" placeholder="建筑年代" tabindex="10">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lxr" class="col-xs-4 control-label">房主姓名:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" name="lxr" id="lxr" placeholder="房主姓名" tabindex="11">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="forlxr" class="col-xs-4 control-label">业务人员:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${forlxr}" name="forlxr" id="forlxr" placeholder="业务员姓名" tabindex="12">
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-6">

                        <div class="form-group" style=" visibility: hidden">
                            <label for="nulls" class="col-xs-4 control-label">隐藏占位:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" name="" id="nulls" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ztai" class="col-xs-4 control-label">状态:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="ztai" id="ztai" type="tabs" tabindex="10">
                                    <option class="id_ztai" value="$[code]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="quyu" class="col-xs-4 control-label">区域:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="quyu" id="quyu" type="tabs" tabindex="10">
                                    <option class="id_quyu" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lxing" class="col-xs-4 control-label">楼型:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="lxing" id="lxing" type="tabs" tabindex="10">
                                    <option class="id_lxing" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="hxing" class="col-xs-4 control-label">户型:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="hxing" id="hxing" type="tabs" tabindex="10">
                                    <option class="id_hxing" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="zxiu" class="col-xs-4 control-label" type="tabs">装潢:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="zxiu" id="zxiu" tabindex="10">
                                    <option class="id_zxiu" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="tel" class="col-xs-4 control-label">房主号码:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" name="tel" id="tel" placeholder="房主号码 / 分割" tabindex="11" onblur="checkTel()" >
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="fortel" class="col-xs-4 control-label">业务号码:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${fortel}" name="fortel" id="fortel" placeholder="业务员号码" tabindex="12">
                            </div>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        
                        <div class="form-group">
                            <label for="beizhu" class="col-xs-2 control-label">备注:</label>
                            <div class="col-xs-10">
                                <textarea class="form-control" rows="3" name="beizhu" id="beizhu" placeholder="备注" tabindex="12"></textarea>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-12">
                        
                        <div class="form-group">
                            <label for="seeGX" class="col-xs-2 control-label">权限:</label>
                            <div class="col-xs-10">
                            <span class="btn-group">
                                <label class="btn btn-default" data-toggle="tooltip" title="☑为所有用户可看<br>□为本公司可看　" data-html="true">
                                    <input type="checkbox" value="1" name="seeGX" id="seeGX" autocomplete="off" tabindex="12"> 发布至共享平台
                                </label>
                                <label class="btn btn-default" data-toggle="tooltip" title="☑为所有用户可看<br>□为自己可看　　" data-html="true">
                                    <input type="checkbox" value="1" name="seeFH" id="seeFH" autocomplete="off" tabindex="12" checked> 显示房号栋号
                                </label>
                                <label class="btn btn-default" data-toggle="tooltip" title="☑共享房主号码　　<br>□仅显示业务员号码" data-html="true">
                                    <input type="checkbox" value="1" name="seeHM" id="seeHM" autocomplete="off" tabindex="12" checked> 显示房主号码
                                </label>
                            </span>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-offset-0 col-xs-12">
                        <div class="alert alert-info" role="alert" style="margin:0;padding:5px 15px;">★发布至共享平台，除本公司各分店外，其他公司（中介宝用户）也能看到该房源。</div>
                    </div>
                </div>
                <div class="form-group hidden">
                    <button type="button" class="btn btn-primary btn-block btn_submit" tabindex="12"><span class="ladda-label">提交</span></button>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>