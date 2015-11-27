<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	request.setAttribute("cid", ThreadSessionHelper.getUser().cid);
%>
<jsp:include page="../inc/resource.jsp"></jsp:include>
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
<link href="${refPrefix}/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<script src="${refPrefix}/js/jquery.js" type="text/javascript"></script>
<script src="${refPrefix}/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.timers.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.input.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.j.tool.js" type="text/javascript"></script>
<script type="text/javascript" src="${refPrefix}/js/buildHtml.js"></script>
<script type="text/javascript" src="${refPrefix}/js/pagination.js"></script>
<script type="text/javascript" src="${refPrefix}/js/jquery.SuperSlide.2.1.1.js" ></script>
<script src="${refPrefix}/js/zTree_v3/js/jquery.ztree.all-3.5.js" type="text/javascript" ></script>
<script type="text/javascript">
var roleId=-1;
function getSider(id){
    $.get('/v/settings/uc_side_company_purview.html', function(data) {
        var sideTop=parseInt(getSideH())*-1+6;
        $('#sideCont').html(data).find('.sideHead').css({top:sideTop});
        var sideHeadHeight=$('#sideCont').find('.sideHead').innerHeight()+1;
        $('#sideCont').css({'padding-top':sideHeadHeight})
        $('#search').click();
    });
}

function buildAuthorities(modules){
  buildHtmlWithJsonArray("auth_first",modules);
  var tmps = $('.auth_second_tmp');
  tmps.attr('name','$[name]');
  tmps.attr('value','$[text]');
   tmps.text('$[text]');
  
  for(var i=0; i<modules.length;i++){
    var module = modules[i];
    $('.auth_third_'+i).addClass('auth_third_'+i+'_$[rowIndex]');
    buildHtmlWithJsonArray("auth_second_"+i,module.children);
    
  }


  tmps = $('.auth_third_tmp');
  tmps.attr('name','$[name]');
  tmps.attr('value','$[text]');
  // tmps.attr('checked','$[checked]');
  tmps.text('$[text]');
  for(var i=0; i<modules.length;i++){
    var module = modules[i];
    // if(module['checked']=='checked'){
    //   //选择第一级
    //   $('[name='+module.name+']').attr('checked','checked');
    // }
    for(var j=0;j<module.children.length;j++){
      var subModule = module.children[j];
      buildHtmlWithJsonArray("auth_third_"+i+"_"+j,subModule.children);
      for(var k=0;k<subModule.children.length;k++){
        var menu = subModule.children[k];
        if(menu['checked']=='checked'){
          $('[name='+menu.name+']').attr('checked','checked');
        }
      }
    }
  }
}

function setAuthority(input){
  YW.ajax({
    type: 'POST',
    url: '/c/sys/update?roleId='+roleId+'&name='+input.name+'&value='+input.checked,
    mysuccess: function(data){
      // alert('设置成功');
    }
  });
}

function loadAuthorites(){
  YW.ajax({
      type: 'get',
      url: '/c/sys/getRoleMenus?roleId='+roleId+'&authName=sz_unit_shouquan',
      mysuccess: function(data){
        var json = JSON.parse(data);
        buildAuthorities(json['modules']);
      }
    });
}
$(document).ready(function() {
    getSider();
    // loadAuthorites();
    $('.id_purview_list').on('click', 'a', function(event) {
        var Thi=$(this),
        rel=Thi.data('rel'),
        this_hid=Thi.data('hid');
    });
    $.get('menuTop.html?'+new Date().getTime(), function(data) {
        $('#menuTop').html(data);
    });
});

</script>
</head>
<body>
<div class="html list title addSide settings">
    <div class="header">
        <div class="maxHW title">
            <ul class="menuLi clearfix title" id="menuTop">
            </ul>
        </div>

  <table class="table table-nopadding TableH  mainCont" >
    <tr>
      <th>模块</th>
      <th>功能</th>
    </tr>
  </table>
 
    </div>
    <div class="bodyer mainCont">
  <div class="sideCont" id="sideCont"></div>
        <div class="maxHW" style="min-width: 700px;">
          <table class="table table-hover table-striped table-nopadding TableB" >
            <tbody>
            <tr data-hid="$[id]" style="display:none;" class="id_purview_list auth_first">
              <td class="purview_title">
                <ul class="purview_list">
                  <li><label>$[text]</label></li>
                </ul>
              </td>
              <td>
                <ul class="purview_cont clearfix">
                  <li class="auth_second_$[rowIndex] clearfix">
                      <dl class="purview_conts clearfix">
                        <dt><label><span class="auth_second_tmp"></span></label></dt>
                        <dd>
                          <label class="auth_third_$[rowIndex]"><input onclick="setAuthority(this)" class="auth_third_tmp" type="checkbox" name="" value=""> <span class="auth_third_tmp"></span></label>
                        </dd>
                      </dl>
                  </li>
                  
                </ul>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
    </div>
    <div class="footer">
        <div class="maxHW mainCont">
        </div>
    </div>
</div>
<div id="rMenu" class="list-group">
  <a href="javascript:void(0)" auth="sz_unit_add" data-type="add_role" id="add_role" class="list-group-item" onclick="addRole();">添加职位</a>
  <a href="javascript:void(0)" auth="sz_unit_edit" data-type="editThis" id="editThis" type="zw" class="list-group-item" onclick="editRole();">修改</a>
  <a href="javascript:void(0)" auth="sz_unit_del" data-type="delThis" id="delThis" type="zw" class="list-group-item">删除</a>
</div>
</body>
</html>