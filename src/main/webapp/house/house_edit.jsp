<%@page import="org.bc.sdak.TransactionalServiceHelper"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="org.bc.sdak.utils.LogUtil"%>
<%@page import="com.youwei.zjb.house.FangXing"%>
<%@page import="com.youwei.zjb.house.entity.House"%>
<%@page import="com.youwei.zjb.sys.ConfigService"%>
<%@page import="org.bc.web.ModelAndView"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
String idStr = request.getParameter("id");
int id = Integer.valueOf(idStr);
ConfigService cs = new ConfigService();
ModelAndView mv = cs.getQueryOptions();

CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
House po = dao.get(House.class, id);
FangXing fxing = FangXing.parse(po.hxf, po.hxt,po.hxw);
request.setAttribute("house", po);
if(fxing!=null){
	request.setAttribute("hxing", fxing.getName());	
}
request.setAttribute("data", mv.data);
%>
<jsp:include page="../inc/top.jsp" />
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
<c:if test="${useLocalResource!=1}">
<link href="/style/css.css" rel="stylesheet">
<link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/jquery.timers.js" type="text/javascript"></script>
<script src="/js/jquery.input.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
</c:if>
<c:if test="${useLocalResource==1}">
<link href="file:///resources/style/css.css" rel="stylesheet">
<link href="file:///resources/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="file:///resources/style/style.css" rel="stylesheet">
<script src="file:///resources/js/jquery.js" type="text/javascript"></script>
<script src="file:///resources/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="file:///resources/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="file:///resources/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.cookie.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.timers.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.input.js" type="text/javascript"></script>
<script src="file:///resources/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript" src="file:///resources/js/buildHtml.js"></script>
</c:if>
<script type="text/javascript">
var id;
var queryOptions;
id =getParam('id');
function save(){
  if(checkDateyear($('#dateyear')) ==false){
	  return;
  }
  if(checkTel()==false){
    return;
  }
  var a=$('form[name=form1]').serialize();
  if (($('#tel').val()==undefined||$('#tel').val()=='') && '${house.site}'!='58') {
    alert('请填写房主电话');
  }else{
      api.button({
          name: '保存',
          disabled:true
       });
      YW.ajax({
        type: 'POST',
        url: '/c/house/update',
        data:a,
        mysuccess: function(data){
            art.dialog.close();
            art.dialog.opener.updateHouse(id,data);
            alert('修改成功');
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

function getContent(){
	 $('#id_quyu').val('${house.quyu}');
     $('#id_ztai').val('${house.ztai}');
     $('#id_lxing').val('${house.lxing}');
     $('#id_hxing').val('${hxing}');
     $('#id_zxiu').val('${house.zxiu}');
     if(${house.seeGX}==1){
       $('#seeGX').attr('checked','checked');
     }
     if(${house.seeFH}==1){
       $('#seeFH').attr('checked','checked');
     }
     if(${house.seeHM}==1){
       $('#seeHM').attr('checked','checked');
     }
}
var apiTitle;
var parent = art.dialog.parent,       // 父页面window对象
api = art.dialog.open.api,  //      art.dialog.open扩展方法
form1=$('.form1');
//if (!api) return;
    api.title('修改房源').size(600, 500)
// 自定义按钮
    .button({
      name: '保存',
      callback: function () {
        save();
        return false;
      },focus: true
    },{
      name: '取消'
    });
if (id!='') {
    apiTitle='修改房源';
    getContent();
};
api.title(apiTitle);
function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a'),
    ThiAH=ThiA.eq(index);
    ThiA.removeClass('hover');
    var Vals=ThiAH.addClass('hover').attr('area');
    $('#area').val(Vals);
    $('#address').val(ThiAH.data('address'));
    $('#id_quyu option[value="'+ThiAH.data('quyu')+'"]').attr('selected',true);
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
// autocomplete
    autoComplete($('#area'))
    
    queryOptions=JSON.parse('${data}');
    buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
    buildHtmlWithJsonArray("id_hxing",queryOptions['fxing'],true);
    buildHtmlWithJsonArray("id_zxiu",queryOptions['zhuangxiu'],true);
    buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
    buildHtmlWithJsonArray("id_ztai",queryOptions['ztai_sell'],true, true);
    //doSearchAndSelectFirst();
    getContent();
});
</script>
</head>
<body style="height:500px; width:620px;">
<div class="html edit title" style="height:500px; width:620px;">
    <div class="bodyer mainCont">
        <div class="maxHW">

            <form class="form-horizontal form_label_right form1" name="form1" role="form" onsubmit="submits();return false;">
                <input type="hidden" value="${house.id }" name="id" id="id"/>
                <div class="row">
                    <div class="col-xs-6">

                        <div class="form-group">
                            <label for="area" class="col-xs-4 control-label">楼盘名称:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.area }" name="area" id="area" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">楼盘地址:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.address }" name="address" id="address" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">栋号房号:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.dhao }" name="dhao" id="dhao" desc="栋号" placeholder="">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.fhao }" name="fhao" id="fhao" desc="房号" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">楼层总层:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.lceng }" name="lceng" desc="楼层" id="lceng" placeholder="">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.zceng }" name="zceng" desc="总层" id="zceng" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">面积总价:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="<fmt:formatNumber  value="${house.mji}"  type="number"  pattern="###.##" />" name="mji" desc="面积" id="mji" placeholder="">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="<fmt:formatNumber  value="${house.zjia}"  type="number"  pattern="###.#" />" name="zjia" desc="总价" id="zjia" placeholder="">
                                <span style="position: absolute;top: 8px;right: -15px;">万元</span>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">建筑年代:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.dateyear }" name="dateyear"  desc="建筑年代"  id="dateyear" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">房主姓名:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.lxr }" name="lxr" id="lxr" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">业务人员:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.forlxr }" name="forlxr" id="forlxr" placeholder="">
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-6">

                        <div class="form-group" style=" visibility: hidden">
                            <label for="nulls" class="col-xs-4 control-label">隐藏占位:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="" id="nulls" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ztai" class="col-xs-4 control-label">状态:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="ztai" id="id_ztai" type="tabs">
                                    <option class="id_ztai" value="$[code]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="quyu" class="col-xs-4 control-label">区域:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="quyu" id="id_quyu" type="tabs">
                                    <option class="id_quyu" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lxing" class="col-xs-4 control-label">楼型:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="lxing" id="id_lxing" type="tabs">
                                    <option class="id_lxing" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="hxing" class="col-xs-4 control-label">户型:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="hxing" id="id_hxing" type="tabs">
                                    <option class="id_hxing" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="zxiu" class="col-xs-4 control-label" type="tabs">装潢:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="zxiu" id="id_zxiu">
                                    <option class="id_zxiu" value="$[name]">$[name]</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">房主号码:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.tel }" name="tel" id="tel" placeholder="房主号码 / 分割" onblur="checkTel()">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">业务号码:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.fortel }" name="fortel" id="fortel" placeholder="">
                            </div>
                        </div>

                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-2 control-label">备注:</label>
                            <div class="col-xs-10">
                                <textarea class="form-control" name="beizhu" id="beizhu" rows="3">${house.beizhu }</textarea>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-12">
                        
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-2 control-label">权限:</label>
                            <div class="col-xs-10">
                            <span class="btn-group">
                                <label class="btn btn-default">
                                    <input type="checkbox" value="1" name="seeGX" id="seeGX" autocomplete="off" > 发布至共享平台
                                </label>
                                <label class="btn btn-default">
                                    <input type="checkbox" value="1" name="seeFH" id="seeFH" autocomplete="off" > 显示房号栋号
                                </label>
                                <label class="btn btn-default">
                                    <input type="checkbox" value="1" name="seeHM" id="seeHM" autocomplete="off" > 显示房主号码
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
                    <button type="button" class="btn btn-primary btn-block btn_submit" tabindex="12"><span class="ladda-label">登　录</span></button>
                </div>
            </form>

        </div>
    </div>
</div>
</body>
</html>