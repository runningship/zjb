<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<link href="/style/css.css" rel="stylesheet">
<link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<link href="/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.cookie.js" type="text/javascript"></script>
<script src="/js/jquery.timers.js" type="text/javascript"></script>
<script src="/js/jquery.input.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript">
var id;
var queryOptions;
function save(){
	if(checkDateyear($('#dateyear')) == false){
		  return;
	  }
  if(checkTel()==false){
    return;
  }
  var a=$('form[name=form1]').serialize();
  if (($('#tel').val()==undefined||$('#tel').val()=='') && ${house.site}!='58') {
    alert('请填写房主电话');
  }else{
      api.button({
          name: '保存',
          disabled:true
       });
      YW.ajax({
        type: 'POST',
        url: '/c/house/rent/doRuku',
        data:a,
        mysuccess: function(data){
        	art.dialog.opener.updateRuku(id);
            art.dialog.close();
            // art.dialog.opener.doSearch();
            alert('修改成功');
        }
      });
  }
}

var parent = art.dialog.parent,       // 父页面window对象
api = art.dialog.open.api,  //      art.dialog.open扩展方法
form1=$('.form1');
//if (!api) return;
// 操作对话框
    api.title('添加房源')
    .size(600, 500)
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
id =getParam('id')
if (id!='') {
    api.title('修改房源');
};
function setSearchValue(index){
    var ThiA=$('#autoCompleteBox').find('a'),
    ThiAH=ThiA.eq(index);
    ThiA.removeClass('hover');
    var Vals=ThiAH.addClass('hover').attr('area');
    $('#area').val(Vals);
    $('#address').val(ThiAH.data('address'));
    $('#quyu option[value="'+ThiAH.data('quyu')+'"]').attr('selected',true);
}
$(document).ready(function() {
// autocomplete
    autoComplete($('#area'))
    
    $.get('/c/config/getQueryOptions', function(data) {
        queryOptions=JSON.parse(data);
        // buildHtmlWithJsonArray("id_rent_type",queryOptions['rent_type'],true, true);
        // buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
        // buildHtmlWithJsonArray("id_hxing",queryOptions['fxing'],true);
        // buildHtmlWithJsonArray("id_zxiu",queryOptions['zhuangxiu'],true);
        // buildHtmlWithJsonArray("id_quyu",queryOptions['quyu'],true);
        // buildHtmlWithJsonArray("id_ztai",queryOptions['ztai_rent'],true, true);
        //doSearchAndSelectFirst();
        // getContent();
    });
});
</script>
</head>
<body>
<div class="html edit title" style="height:500px; width:620px;">
    <div class="bodyer mainCont">
        <div class="maxHW">

            <form class="form-horizontal form_label_right form1" name="form1" role="form" onsubmit="submits();return false;">
                <input type="hidden" name="id" id="id" value="${house.id}"/>
                <div class="row">
                    <div class="col-xs-6">

                        <div class="form-group">
                            <label for="area" class="col-xs-4 control-label">楼盘名称:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.area}" name="area" id="area" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">楼盘地址:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.address}" name="address" id="address" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">栋号房号:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.dhao}" name="dhao" id="dhao" placeholder="">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.fhao}" name="fhao" id="fhao" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">楼层总层:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.lceng}" name="lceng" id="lceng" placeholder="">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.zceng}" name="zceng" id="zceng" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">面积租金:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.mji}" name="mji" id="mji" placeholder="">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.zjia}" name="zjia" id="zjia" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">建筑年代:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.dateyear}" name="dateyear" id="dateyear" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">房主姓名:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.lxr}" name="lxr" id="lxr" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">业务人员:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.forlxr}" name="forlxr" id="forlxr" placeholder="">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">房间/条件:</label>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.wo}" name="wo" placeholder="">
                            </div>
                            <div class="col-xs-4">
                                <input type="text" class="form-control" value="${house.xianzhi}" name="xianzhi" placeholder="男女不限">
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">

                        <div class="form-group">
                            <label for="nulls" class="col-xs-4 control-label">方式:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="fangshi" id="id_rent_type" type="tabs">
                                    <c:forEach items="${fangshi}" var="fs">
                                        <option <c:if test="${fs.code==house.fangshi}">selected="selected"</c:if> value="${fs.code}"> ${fs.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ztai" class="col-xs-4 control-label">状态:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="ztai" id="id_ztai" type="tabs" >
                                    <c:forEach items="${ztais}" var="zt">
                                        <option <c:if test="${zt.code==house.ztai}">selected="selected"</c:if> value="${zt.code}">${zt.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="quyu" class="col-xs-4 control-label">区域:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="quyu" id="id_quyu" type="tabs" >
                                    <c:forEach items="${quyus}" var="qy">
                                        <option <c:if test="${qy.name==house.quyu}">selected="selected"</c:if> class="" value="${qy.name}">${qy.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="lxing" class="col-xs-4 control-label">楼型:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="lxing" id="id_lxing" type="tabs">
                                    <c:forEach items="${lxing}" var="lx">
                                        <option <c:if test="${lx.name==house.lxing}">selected="selected"</c:if> value="${lx.name}">${lx.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="hxing" class="col-xs-4 control-label">户型:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="hxing" id="id_hxing" type="tabs">
                                    <c:forEach items="${hxings}" var="hx">
                                        <option <c:if test="${hx.name==hxing}">selected="selected"</c:if> value="${hx.name}">${hx.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="zxiu" class="col-xs-4 control-label" type="tabs">装潢:</label>
                            <div class="col-xs-8">
                                <select class="form-control" name="zxiu" id="id_zxiu" >
                                    <c:forEach items="${zxius}" var="zx">
                                        <option <c:if test="${zx.name==house.zxiu}">selected="selected"</c:if> value="${zx.name}">${zx.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">房主号码:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.tel}" name="tel" id="tel" placeholder="房主号码 / 分割" onblur="checkTel()">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-4 control-label">业务号码:</label>
                            <div class="col-xs-8">
                                <input type="text" class="form-control" value="${house.fortel}" name="fortel" id="fortel" placeholder="">
                            </div>
                        </div>
                        <c:if test="${house.telImg!=null}">
                            <div class="form-group">
                                <label for="idCnum" class="col-xs-4 control-label">电话:</label>
                                <div class="col-xs-8">
                                    <img src="${house.telImg}" />
                                </div>
                            </div>    
                        </c:if>
                        
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-2 control-label">原链接:</label>
                            <div class="col-xs-10">
                                 <textarea class="form-control" rows="1">${house.href}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-2 control-label">配置:</label>
                            <div class="col-xs-10">
                                <textarea class="form-control" name="peizhi" rows="1" >${house.peizhi}</textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-2 control-label">备注:</label>
                            <div class="col-xs-10">
                                <textarea class="form-control" name="beizhu" id="beizhu" rows="3">${house.beizhu}</textarea>
                            </div>
                        </div>

                    </div>
                    <div class="col-xs-12">
                        
                        <div class="form-group">
                            <label for="idCnum" class="col-xs-2 control-label">权限:</label>
                            <div class="col-xs-10">
                            <span class="btn-group">
                                <label class="btn btn-default">
                                    <input type="checkbox" value="1" name="seeGX" id="seeGX" checked="checked" autocomplete="off"> 发布至共享平台
                                </label>
                                <label class="btn btn-default">
                                    <input type="checkbox" value="1" name="seeFH" id="seeFH" checked="checked" autocomplete="off" > 显示房号栋号
                                </label>
                                <label class="btn btn-default">
                                    <input type="checkbox" value="1" name="seeHM" id="seeHM" checked="checked" autocomplete="off" > 显示房主号码
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