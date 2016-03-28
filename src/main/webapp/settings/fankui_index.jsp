<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script src="${refPrefix}/js/jquery.js" type="text/javascript"></script>
<script src="${refPrefix}/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="${refPrefix}/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.cookie.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.timers.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.input.js" type="text/javascript"></script>
<script src="${refPrefix}/js/jquery.j.tool.js" type="text/javascript"></script>
<script src="${refPrefix}/js/buildHtml.js" type="text/javascript" ></script>
<script src="${refPrefix}/js/pagination.js" type="text/javascript" ></script>
<script src="${refPrefix}/js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
<script type="text/javascript">
var queryOptions;
try{
}catch(e){}

function doSearch(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/feedback/list',
    data:a,
    mysuccess: function(data){
        var houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_Feedback_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        setTableFix();
    }
  });
}

function editFeedBack(id){
  art.dialog.open('/v/settings/feedback_huifu.html?showDel=true&id='+id,{
      id:'EditHouse',
      title:'回复反馈',
      width:600,
      height:350
    });
}

$(document).ready(function() {
    $(document).on('click', '.tbtn', function(event) {
        var Thi=$(this);
        var ThiQ=Thi.data('q');
        if(ThiQ=='menu'){

        }else if(ThiQ=='min'){
            WinMin();
        }else if(ThiQ=='max'){
            WinMax();
        }else if(ThiQ=='rev'){
            WinRevert();
        }else if(ThiQ=='close'){
            WinClose();
        }else if(ThiQ=='tab1'){
        }
        return false;
    });

// 列表内容
    $.get('/c/config/getQueryOptions', function(data) {
        queryOptions=JSON.parse(data);
        // buildHtmlWithJsonArray("id_lxing",queryOptions['lxing'],true);
        Page.Init();
        doSearch();
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
          	<jsp:include page="menuTop.jsp"></jsp:include>
          </ul>
      </div>
      <form class="form-horizontal form1 " onsubmit="doSearch();return false;" role="form" name="form1">
        <input type="hidden" class="hidden" name="pageInput" value="">
      </form>

      <table class="table table-nopadding TableH " >
        <tr>
          <th>反馈问题</th>
          <th>反馈人</th>
          <th>添加</th>
          <!-- <th>问题状态</th> -->
          <th>操作</th>
        </tr>
      </table>
    </div>
    <div class="bodyer ">
      <div class="maxHW" style="min-width: 700px;">

        <table class="table table-hover table-striped table-nopadding TableB" >
          <tbody>
          <tr data-hid="$[id]" style="display:none;" class="id_Feedback_list">
            <td style="width:200px">$[conts]</td>
            <td style="width:100px">$[deptName]-$[uname]</td>
            <td style="width:100px">$[addtime]</td>
            <!-- <td style="width:50px">$[province]</td> -->
            <td style="width:100px">
              <a href="javascript:void(0)" class="btns" data-type="edit" onclick="editFeedBack($[id])">回复</a>
              <!-- <a href="javascript:void(0)" class="btns" data-type="del" onclick="deleteCity($[id])">删除</a> -->
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