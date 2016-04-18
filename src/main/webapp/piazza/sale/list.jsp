<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="/oa/style/cssOa.css" />
<script src="/js/jquery.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" src="/js/pagination.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript" src="/oa/js/messagesBox.js"></script>
<link rel="stylesheet" type="text/css" href="/oa/style/cocoWinLayer.css" />
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<!--<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/layerBox.js"></script>-->
</head>
<script type="text/javascript">
function doSearch(){
  var a=$('form[name=form1]').serialize();
  // pushQueryToHistory($('form[name=form1]').serializeArray());
  YW.ajax({
    type: 'POST',
    url: '/c/piazza/listSail?pageSize=20',
    data:a,
    mysuccess: function(data){
        houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_notice_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        
    }
  });
}

function deleteThis(id){
  event.cancelBubble=true;
  art.dialog.confirm('删除后不可恢复，确定要删除吗？', function () {
    YW.ajax({
      type: 'POST',
      url: '/c/oa/delete?noticeId='+id,
      mysuccess: function(data){
        doSearch();
        alert('删除成功');
      }
    });
  },function(){},'warning');
}

$(function(){
  Page.Init();
  doSearch();
})
</script>

<body>

<!--添加网址窗口-->


   
   <div class="ggLayerMain">
      <form class="form-horizontal form1" onsubmit="doSearch();return false;" role="form" name="form1">
        <div class="ggLayerMainScol">
          
          <div class="ggListBox read$[hasRead] id_notice_list" style="display:none">
               <div class="ggListBoxTit ggBorLeftRed">
                    <div class="tit">
                        <h2 onclick="window.location='/piazza/sale/view.jsp?id=$[id]'">$[title]</h2>
                        <div class="conTime"><!-- <i class="person">$[senderName]</i> --><i class="clock">$[addtime]</i></div>
                    </div>
                    <div class="ggListBoxBot">
                    <span class="tuBox"><i class="Bg zan"></i>$[replys]</span>
                    <!-- <span class="tuBox"><i class="Bg zan" onclick="selectZan($[id],this);return false;"></i>$[replys]</span> -->
                    <i show="${user.id}==$[senderId]" class="Bg  xgSel" onclick="openNewWin('editSale','800','600','编辑内容','edit.jsp?id=$[id]')"></i>
                    <c:if test="${authNames.contains('squar_sale_del')}">
                      <i class="Bg hfSel" onclick="deleteThis($[id]);return false;" ></i>
                    </c:if>
                    <c:if test="${!authNames.contains('squar_sale_del')}">
                      <i show="${user.id}==$[senderId]" class="Bg hf" onclick="deleteThis($[id]);return false;" ></i>
                    </c:if>
                    </div>
               </div>
          </div>
        </div>
        <div>
             <button class="" type="submit" onclick="doSearch();return false;" value="enter" style="display:none;">搜索</button>
        </div>
        <div class="ggLayerMainPage">
              <div style="display:table-row; height:33px;">
                                
                   <div class="divPage"  style=" width:100%; float:left;">
                   
                        <div class="pageMain footer">
                          <div class="maxHW mainCont ymx_page foot_page_box"></div>
                        </div>
                   
                   </div>              
                                
              </div>
              
              
         </div>
        </form>
   </div>
   

<!--添加网址窗口-->


</body>
</html>
