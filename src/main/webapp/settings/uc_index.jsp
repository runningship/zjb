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
<link href="${refPrefix }/style/css.css" rel="stylesheet">
<link href="${refPrefix }/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="${refPrefix }/js/zTree_v3/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link href="${refPrefix }/style/style.css" rel="stylesheet">
<script src="${refPrefix }/js/jquery.js" type="text/javascript"></script>
<script src="${refPrefix }/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="${refPrefix }/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="${refPrefix }/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="${refPrefix }/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${refPrefix }/js/jquery.timers.js" type="text/javascript"></script>
<script src="${refPrefix }/js/jquery.input.js" type="text/javascript"></script>
<script src="${refPrefix }/js/jquery.j.tool.js" type="text/javascript"></script>
<script src="${refPrefix }/js/buildHtml.js" type="text/javascript" ></script>
<script src="${refPrefix }/js/pagination.js" type="text/javascript" ></script>
<script src="${refPrefix }/js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
<script src="${refPrefix }/js/zTree_v3/js/jquery.ztree.all-3.5.js" type="text/javascript" ></script>
<script type="text/javascript">
var queryOptions;
try{
}catch(e){}

function doSearch(page){
	if(page){
		  Page.currentPageNo=page;
		  $('.pageInput').val(1);
	  }
  var a=$('form[name=form1]').serialize();
  
  YW.ajax({
    type: 'POST',
    url: '/c/user/list',
    data:a,
    mysuccess: function(data){
        var houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        setTableFix();
        // if(event==undefined || $(event.srcElement).attr('action')!='page'){
        //   $('.pageInput').val(1);
        // }
        if(houseData['page']['data']<=0){
          $('.id_House_list').parents('.maxHW').addClass('list_noThing');
        }else{
          $('.id_House_list').parents('.maxHW').removeClass('list_noThing');
        }
    }
  });
}

function getSider(){
    $.get('/v/settings/uc_side_company.html', function(data) {
        var sideTop=parseInt(getSideH())*-1+6;
        $('#sideCont').html(data).find('.sideHead').css({top:sideTop});
        var sideHeadHeight=$('#sideCont').find('.sideHead').innerHeight()+1;
        $('#sideCont').css({'padding-top':sideHeadHeight})
        $('#search').click();
    });
}

function uc_edit(id){
  art.dialog.open('/v/settings/uc_index_u_edit.html?id='+id)
}

function uc_delete(id){
  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
      YW.ajax({
        type: 'POST',
        url: '/c/user/delete?id='+id,
        data:'',
        mysuccess: function(data){
            doSearch();
            alert('删除成功');
        }
      });
  },function(){},'warning');
}

$(document).ready(function() {
  //var bodyW = $(window.top.document).width()-50;
  //$(document.body).width(bodyW);
// 加载侧边
   	getSider();
// 列表内容
    $.get('/c/config/getQueryOptions', function(data) {
        queryOptions=JSON.parse(data);
        // buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
        Page.Init();
        doSearch(1);
    });
// // 
//     $('.id_House_list').on('click', 'a', function(event) {
//         var Thi=$(this),
//         ThiType=Thi.data('type'),
//         ThiPtr=Thi.parents('tr'),
//         ThiUid=ThiPtr.data('uid');
//         if(ThiType=='edit'){
//           uc_edit(ThiUid);
//         }else{
//           return false;
//         }
//     });
    $.get('/v/settings/menuTop.html?'+new Date().getTime(), function(data) {
        $('#menuTop').html(data);
        //设置拖动栏
        var bodyW = $(window.top.document).width()-50;
        //-200为margin-right:200
        $(window.top.document).find('#dragbar').width(bodyW-$('#menuTop').width()-200);
    });
});
$(window).resize(function() {      //类别
});
</script>
</head>
<body>
<div class="html list title addSide">
    <div class="header">
        <div class="maxHW title">
            <ul class="menuLi clearfix title" id="menuTop">
            </ul>
        </div>
  <form class="form-horizontal form1 hidden" onsubmit="doSearch();return false;" role="form" name="form1">
    <input type="hidden" name="cid" id="cid" value="${cid}">
    <input type="hidden" name="did" id="did" value="">
    <input type="hidden" class="hidden" name="pageInput" value="">
    <input type="hidden" class="hidden" name="order" value="desc">
    <input type="hidden" class="hidden" name="orderBy" value="u.id">
    <div class="form-group clearfix">
      <div class="btn-group">
        <button type="submit" class="btn btn-sms btn-primary btn_subnmit">搜索提交</button>
      </div>
    </div>
  </form>

  <table class="table table-nopadding TableH  mainCont" >
    <tr>
      <th>帐号</th>
      <th>姓名</th>
      <th>联系电话</th>
      <th>部门-职位</th>
      <th>最后登陆时间</th>
      <th>最后ＩＰ</th>
      <th>操作</th>
    </tr>
  </table>
 
    </div>
    <div class="bodyer mainCont">
      <div class="sideCont" id="sideCont">
      
      </div>
      <div class="maxHW" style="min-width: 700px;">

        <table class="table table-hover table-striped table-nopadding TableB" >
          <tbody>
          <tr data-hid="$[id]" style="display:none;" class="id_House_list">
            <td>$[lname]</td>
            <td>$[uname]</td>
            <td>$[tel]</td>
            <td>$[dname]-$[role]</td>
            <td>$[lasttime]</td>
            <td>$[ip]</td>
            <td><span class="btn-group null-float">
              <a href="javascript:void(0)" auth="sz_user_edit" class="btns btn btn-xs btn-primary" data-type="edit" onclick="uc_edit($[uid])">编辑</a>
              <a href="javascript:void(0)" auth="sz_user_del" class="btns btn btn-xs btn-danger" data-type="del" onclick="uc_delete($[uid])">删除</a>
              </span>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="footer">
        <div class="maxHW mainCont ymx_page foot_page_box"></div>
    </div>
</div>
<div id="rMenu" class="list-group" style="position:absolute;z-index:999;display:none;">
  <a href="javascript:void(0)" auth="sz_comp_edit" id="m_edit_comp" data-type="editThis" class="list-group-item">修改公司</a>
  <a href="javascript:void(0)" auth="sz_comp_del" id="m_del_comp" data-type="delThis" class="list-group-item">删除公司</a>
  <a href="javascript:void(0)" auth="sz_group_add" id="m_add_group" data-type="addGroup" class="list-group-item" >添加区域</a>
  <a href="javascript:void(0)" auth="sz_group_edit" id="m_edit_group" data-type="editThis" class="list-group-item">修改区域</a>
  <a href="javascript:void(0)" auth="sz_group_del" id="m_del_group" data-type="delThis" class="list-group-item">删除区域</a>
  <a href="javascript:void(0)" auth="sz_dian_add" id="m_add_dept" data-type="adddept" class="list-group-item" >添加门店</a>
  <a href="javascript:void(0)" auth="sz_dian_edit" id="m_edit_dept" data-type="editThis" class="list-group-item">修改门店</a>
  <a href="javascript:void(0)" auth="sz_dian_del" id="m_del_dept" data-type="delThis" class="list-group-item">删除门店</a>
  <a href="javascript:void(0)" auth="sz_group_set" id="m_move_to" data-type="moveTo" class="list-group-item">移动门店</a>
  <a href="javascript:void(0)" auth="sz_user_add" id="m_add_user" data-type="addUser" class="list-group-item" >添加员工</a>
  <a href="javascript:void(0)" auth="sz_record_on" id="m_list_records" data-type="seeRecords" class="list-group-item disabled">查看操作记录</a>
  <a href="javascript:void(0)" auth="sz_genjin_on" id="m_list_genjin" data-type="seeGenjin" class="list-group-item">查看跟进记录</a>
  <a href="javascript:void(0)"  id="m_auth_pc"  data-type="auth_pc"  class="list-group-item">查看授权</a>
</div>
</body>
</html>