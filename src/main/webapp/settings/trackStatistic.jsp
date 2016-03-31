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
<link href="../style/css.css" rel="stylesheet">
<link href="../bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="../style/style.css" rel="stylesheet">
<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="../js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="../js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="../js/DatePicker/WdatePicker.js" type="text/javascript" ></script>
<script src="../js/jquery.j.tool.js" type="text/javascript"></script>
<script src="../js/buildHtml.js" type="text/javascript" ></script>
<script src="../js/pagination.js" type="text/javascript" ></script>
<script src="../js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
<script type="text/javascript">
var queryOptions;
try{
}catch(e){}

function doSearch(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/house/trackStatistic',
    data:a,
    mysuccess: function(data){
        var trackData=JSON.parse(data);
        buildHtmlWithJsonArray("id_track_list",trackData['page']['data']);
        Page.setPageInfo(trackData['page']);
        setTableFix();
    }
  });
}

function seeViewLog(uid){
		var w=window.top.document.body.offsetWidth-200;
	  art.dialog.open('/v/tongji/viewLogList.html?uid='+uid , {width:w})
	}

$(document).ready(function() {
	Page.Init();
    doSearch();
});
</script>
</head>
<body>
<div class="winThree list title addSide">
    <div class="winHeader">
      <div class="maxHW title">
      		<ul class="winMenuTop menuLi clearfix title" id="menuTop">
      			<jsp:include page="menuTop.jsp"></jsp:include>
          </ul>
          
      </div>
      <form class="form-horizontal form1 " onsubmit="doSearch();return false;" role="form" name="form1">
        <input type="hidden" class="hidden" name="pageInput" value="">
        <div class="form-group clearfix">
	        <div class="input-group input-group-max" style="width:300px;">
                            <span class="SpanNameH2 not-select" style="float:left;margin-right:10px;margin-left:30px;"><em>日期</em></span>
                            <div class="DivBoxW1 Fleft" style="display:inline">
                            <input type="text" class="form-control input-left input-sm" name="viewTimeStart" id="idTimes" onFocus="var timeend=$dp.$('idTime');WdatePicker({lang:'zh-cn',onpicked:function(){idTime.focus();},maxDate:'#F{$dp.$D(\'idTime\')}'})" style="width:100px;height:35px;">
        <input type="text" class="form-control input-left input-sm" name="viewTimeEnd" id="idTime" onFocus="WdatePicker({lang:'zh-cn',minDate:'#F{$dp.$D(\'idTimes\')}'})" style="width:100px;height:35px;">
                            </div>
	        </div>
	      <div class="input-group input-group-min" style="width:150px;">
	        <span class="input-group-addon">来源</span>
	        <select class="form-control " name="isMobile" >
	          <option value="">全部</option>
	          <option value="1">手机端</option>
	          <option value="0">PC端</option>
	        </select>
	      </div>
	      <div class="btn-group">
	          <button type="button" class="btn btn-primary " onclick="doSearch(1);">搜索</button>
	      </div>
	    </div>
      </form>

      <table class="table table-nopadding TableH " >
        <tr>
        	<th>公司</th>
        	<th>账号</th>
          <th>用户名</th>
          <th>手机号码</th>
          <th>访问次数</th>
          <th>手机版</th>
        </tr>
      </table>
    </div>
    <div class="winBodyer " style=" top: 127px;">
      <div class="winMainer maxHW" style="min-width: 700px;">

        <table class="table table-hover table-striped table-nopadding TableB" >
          <tbody>
          <tr data-hid="$[id]" style="display:none;" class="id_track_list">
          <td style="width:100px">$[cname]</td>
          	<td style="width:100px">$[lname]</td>
            <td style="width:100px">$[uname]</td>
            <td style="width:150px">$[tel]</td>
            <td style="width:200px"><a href="javascript:void(0)" onclick="seeViewLog($[uid])">$[total]</a></td>
            <td style="width:100px">$[mobileON]</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="winFooter">
        <div class="maxHW mainCont ymx_page foot_page_box"></div>
    </div>
</div>
</body>
</html>