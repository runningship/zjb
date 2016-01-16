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
<script type="text/javascript" src="${refPrefix }/js/buildHtml.js"></script>
<script type="text/javascript" src="${refPrefix }/js/pagination.js"></script>
<script type="text/javascript" src="${refPrefix }/js/jquery.SuperSlide.2.1.1.js" ></script>
<script type="text/javascript" src="${refPrefix }/js/zTree_v3/js/jquery.ztree.all-3.5.js"></script>
<script type="text/javascript">
var queryOptions;
var cnum;
function doSearch(page){
	if(page){
		  Page.currentPageNo=page;
		  $('.pageInput').val(1);
	  }
  var a=$('form[name=form1]').serialize();
  // if(event==undefined || $(event.srcElement).attr('action')!='page'){
  //   $('.pageInput').val(1);
  // }
  
  YW.ajax({
    type: 'POST',
    url: '/c/pc/list',
    data:a,
    mysuccess: function(data){
        var houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        setTableFix();
        
        $('.shenhe-0').addClass('btn-warning');
        $('.shenhe-1').addClass('btn-primary');
        if(houseData['page']['data']<=0){
          $('.id_House_list').parents('.maxHW').addClass('list_noThing');
        }else{
          $('.id_House_list').parents('.maxHW').removeClass('list_noThing');
        }
    }
  });
}
function getSider(id){
    $.get('/v/settings/pc_side_company.html', function(data) {
        var sideTop=parseInt(getSideH())*-1+6;
        $('#sideCont').html(data).find('.sideHead').css({top:sideTop});
        var sideHeadHeight=$('#sideCont').find('.sideHead').innerHeight()+1;
        $('#sideCont').css({'padding-top':sideHeadHeight})
        $('#search').click();
        
    });
}
$(document).ready(function() {
  //var bodyW = $(window.top.document).width()-50;
  //$(document.body).width(bodyW);
  Page.Init();
// 加载侧边
    getSider();
// 列表内容
    doSearch(1);
// 
    $('.id_House_list').on('click', 'a', function(event) {
        var Thi=$(this),
        rel=Thi.data('rel'),
        this_hid=Thi.data('hid');
    });
    $.get('/v/settings/menuTop.html?'+new Date().getTime(), function(data) {
        $('#menuTop').html(data);
    });
});

function delPC(id){
  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
  YW.ajax({
    type: 'POST',
    url: '/c/pc/delete?id='+id,
    mysuccess: function(data){
        doSearch();
        alert('删除成功');
    }
  });
  },function(){},'warning');
}

function shenhePC(id){
  YW.ajax({
    type: 'POST',
    url: '/c/pc/shenhe?id='+id,
    mysuccess: function(data){
        doSearch();
    }
  });
}

function getShenHeText(lock){
  if(lock==1){
    return "已审核";
  }else{
    return "未审核";
  }
}

function setOrderField(field){
	$('#orderBy').val(field);
	doSearch(1);
}
</script>
<style type="text/css">
  .div1 {
    /*width: 200px;
    overflow: hidden;
    text-overflow: ellipsis;*/
    display: block;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    width: 280px;
    word-break: break-all;
    cursor: pointer;
  }
</style>
</head>
<body>
<div class="html list title addSide">
    <div class="header">
        <div class="maxHW title">
            <ul class="menuLi clearfix title" id="menuTop">
            </ul>
        </div>
    <form name="form1" onsubmit="doSearch();return false;" role="form" style="display:none">
      <input type="hidden" id="cid" name="cid" value="${cid}"/>
      <input type="hidden" id="did" name="did" />
      <input type="hidden" id="orderBy" name="orderBy" />
      <input type="hidden" class="hidden" name="pageInput" value="">
      <input type="hidden" id="act" name="act" />
      <div class="form-group clearfix">
        <div class="btn-group">
          <button type="submit" class="btn btn-sms btn-primary btn_subnmit">搜索提交</button>
          <!-- <button type="button" class="btn btn-sms" data-type="qingkong" onclick="window.location.href='pc_index.html?nav='+getParamS">清空</button> -->
        </div>
      </div>
    </form>

  <table class="table table-nopadding TableH  mainCont" >
    <tr>
      <th>电脑名称</th>
      <th>门店</th>
      <th>机器码</th>
      <th onclick="setOrderField('addtime')" style="cursor:pointer">申请时间</th>
      <th onclick="setOrderField('lasttime') " style="cursor:pointer">最后登录时间</th>
      <th>最后登录IP</th>
      <th>备注</th>
      <th>操作</th>
    </tr>
  </table>
 
    </div>
    <div class="bodyer mainCont">
  <div class="sideCont" id="sideCont"></div>
        <div class="maxHW" style="min-width: 700px;">

  <table class="table table-hover table-striped table-nopadding TableB" >
    <tbody>
    <tr data-hid="$[id]" style="display:none;" class="id_House_list">
      <td>$[pcname]</td>
      <td>$[dname]</td>
      <td><div class="div1">$[uuid]</div></td>
      <td>$[addtime]</td>
      <td>$[lasttime]</td>
      <td>$[ip]</td>
      <td>$[beizhu]</td>
      <td><span class="btn-group null-float">
        <a href="##" auth="sz_pc_sh" class="btn btn-xs shenhe-$[lock]" data-hid="$[id]" data-rel="del" runscript="true" onclick="shenhePC($[id])" >getShenHeText($[lock])</a>
        <a href="##" auth="sz_pc_del" class="btn btn-xs btn-danger del" data-hid="$[id]" data-rel="del" onclick="delPC($[id])">删</a>
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
</body>
</html>