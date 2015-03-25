<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="height:570px;">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/validate.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/keyEnter.js"></script>
<script type="text/javascript">
var id;
function save(){
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/task/save',
    data:a,
    mysuccess: function(data){
      art.dialog.close();
      art.dialog.opener.doSearch();
      alert('添加成功');
    }
  });
}

$(document).ready(function() {
   
});

</script>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
</head>

<body style="height:300px;">

<form name="form1" role="form">
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="layerTable1 TableBgColor1" style=" border-top:4px solid #e9e9e9;">

  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">项目名称：</td>
    <td width="300" class="TableTdBgColor"><input type="text" class="w1" name="name" placeholder="如：58-合肥-二手房"/></td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">城市拼音：</td>
    <td width="300" class="TableTdBgColor"><input type="text" class="w1" name="cityPy"/></td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">城市名称：</td>
    <td width="300" class="TableTdBgColor"><input type="text" class="w1" name="city"/></td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">任务地址：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="siteUrl" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">列表选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="listSelector" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">详情页面选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="detailLink" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">楼盘选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="area" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">区域选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="quyu" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">楼层选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="lceng" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">总层选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="zceng" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">户型室选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="hxf" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">户型厅选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="hxt" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">户型卫选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="hxw" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">装修选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="zxiu" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">面积选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="mji" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">总价选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="zjia" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">发布时间选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="pubtime" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">联系人选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="lxr" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">电话选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="tel" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">建筑年代选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="dateyear" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">备注选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="beizhu" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">地址选择器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="address" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td width="70" align="right" class="TableTdBorBot TableTdBorTop">置顶过滤器：</td>
    <td class="TableTdBgColor" colspan="3" width="450" height="35" style="position:relative;">
          <input type="text" class="w1" name="filterSelector" autocomplete="off" style="float:left; width:390px; position:absolute; left:8px; top:5px; z-index:1;" />
    </td>
  </tr>
  <tr>
    <td align="right" class="" style="background-color:#e9e9e9;padding:0px;"> </td>
    <td colspan="3" class="" height="50" style=" background-color:#e9e9e9;padding:0px;">
       <button onclick="save();return false;" class="save btn btn-primary btn-block" >保存</button>
    </td>
  </tr>
  
</table>
</form>
</body>
</html>
