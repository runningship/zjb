<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" src="/oa/js/messagesBox.js"></script>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link rel="stylesheet" type="text/css" href="style/cssOa.css" />
<link rel="stylesheet" type="text/css" href="style/cocoWinLayer.css" />
</head>
<script type="text/javascript">
function selectZan(id,obj){
  $(obj).toggleClass('zan');
  $(obj).toggleClass('zanSel');
  YW.ajax({
    type: 'POST',
    url: '/c/oa/toggleZan?id='+id,
    mysuccess: function(data){
    	
    }
  });
}

</script>
<body>
<div class="oa_Main oa_W">
  <div class="oaMain">
    <div class="table w100 h100">
      <div class="tr w100">
        <div class="td oaTit oaTitBgInfo">
          <ul class="titBox">
            <li><i class=" Bg whq"></i><span class="color1">文化墙</span></li>
            <li><span class="line"></span></li>
            <li><i class=" Bg gg"></i><span class="color2">文化墙</span></li>
            <li><span class="line"></span></li>
            <li><i class=" Bg wzsc"></i><span class="color3">文化墙</span></li>
          </ul>
        </div>
        <div class="td oaTit oaTitBgInfo">
        </div>
      </div>
      <div class="tr w100">
          <div class="td oaInfoTit">
            <div class="txt Fleft"><span class="Fleft">文化墙最新文章</span><i class="Bg add Fleft"></i></div>  <a href="#">更多></a>  
          </div>
          <div class="td oaInfoTit">
            <div class="txt2 Fleft"><span class="Fleft">最近发布公告</span><i class="Bg add Fleft"  onclick="openNewWin('addGg','660','添加公告','notice/add.jsp')" ></i></div><a href="#" style="margin-right:25px;">更多></a></div>
          </div>
          <div class="tr w100">
            <div class="td" style="width:50%; height:100%;overflow:hidden;">
              <div class="oaInfomain" style="overflow:hidden; overflow-y:auto;">
                <div class="whqMain" style="padding-bottom:10px;">
                  <c:forEach items="${articleList}"  var="article">
                    <div class="textInfoBox">
                      <div class="infoBoxLine"><img src="images/pageLineicon.png" /></div>
                      <div class="infoBoxTit">
                        <div class="infoListImg Fleft"><img src="images/mr.jpg" /></div>
                        <div class="Fleft">      
                          <p><span class="yh">${article.senderId }</span><span class="time">${article.addtime }</span></p>
                          <p><a href="#" class="tit">${article.title }</a></p>
                        </div>
                        <div class="infoCaozuo">
                          <i class="Bg xg">修改</i>
                          <i class="Bg <c:if test="${article.zan==0}">zan</c:if> <c:if test="${article.zan==1}">zanSel</c:if>" onclick="selectZan(${article.id},this);return false;">点赞</i>
                          <span class="zan_count">123</span>
                          <i class="Bg hf">回复</i>
                        </div>
                      </div>
                      <div class="infoBoxContent">
                        <span class="marginRight55 marginLeft20">${article.conts}</span>
                        <span class="infoMore">...</span>                
                      </div>
                    </div>
                  </c:forEach>
                </div>
              </div>
            </div>
            <div class="td" style="width:50%; vertical-align:top;">
              <div class="oaInfomain table">
                <div class="tr">
                  <div class="td ggMain" style="height:100%;">
                    <div style=" margin-right:22px; height:100%; overflow:hidden; overflow-y:auto; padding-right:2px;">
                      <c:forEach items="${noticeList}"  var="notice">
                        <div class="ggBox">
                          <div class="ggBoxTime">
                            <p class="month"><fmt:formatDate value="${notice.addtime}" pattern="MMM"/></p>
                            <p class="day"><fmt:formatDate value="${notice.addtime}" pattern="dd"/></p>
                          </div>
                          <div class="ggBoxContent">
                            <p><span class="titL marginLeft10">Tit:</span><span class="tit marginLeft10">${notice.title}</span></p>
                            <!-- <p><span class="marginLeft10 con">${notice.conts}</span></p> -->
                          </div>
                          <div class="ggBoxCaozuo">
                            <i class="Bg xgSel" onclick="openNewWin('editGg','660','编辑公告','notice/edit.jsp?id=${notice.id}')">修改</i>
                            <i class="Bg zanSel">点赞</i>
                            <i class="Bg hfSel">回复</i>
                          </div>
                        </div>
                      </c:forEach>
                    </div>
                  </div>
                </div>
                <div class="tr">
                  <div class="ggMain td" style="height:230px;">
                    <jsp:include page="site/index.jsp" />
                  </div>
                </div>
              </div> 
            </div>
          </div>
        </div>
      </div>
</div>

<!-- <div class="cocoLayer" id="cocoLayerWebLine" style="width:660px; display:none;height:800px;">
   
   <div class="cocoLayerTit"><span>编辑我的个人网址</span><i class="closeBg close" onclick="LayerCloseBox('cocoLayerWebLine')" title="关闭"></i></div>
   
   <div class="cocoLayerMain">
   
     <div class="mainTit"><span class="select"><input type="checkbox" /></span><span class="webSiteName">网站名称</span><span class="webSiteLine">网址链接</span><span class="caozuo">操作</span></div>
     <ul class="mainCon">
    
        <li><span class="select"><input type="checkbox" /></span><span class="webSiteName">百度</span><span class="webSiteLine">www.baidu.com</span><span class="caozuo"><i>×</i></span></li>
        <li><span class="select"><input type="checkbox" /></span><span class="webSiteName">百度</span><span class="webSiteLine">www.baidu.com</span><span class="caozuo"><i>×</i></span></li>
        <li><span class="select"><input type="checkbox" /></span><span class="webSiteName">百度</span><span class="webSiteLine">www.baidu.com</span><span class="caozuo"><i>×</i></span></li>
        <li><span class="select"><input type="checkbox" /></span><span class="webSiteName">百度</span><span class="webSiteLine">www.baidu.com</span><span class="caozuo"><i>×</i></span></li>
        <li><span class="select"><input type="checkbox" /></span><span class="webSiteName">百度</span><span class="webSiteLine">www.baidu.com</span><span class="caozuo"><i>×</i></span></li>
        <li><span class="select"><input type="checkbox" /></span><span class="webSiteName">百度</span><span class="webSiteLine">www.baidu.com</span><span class="caozuo"><i>×</i></span></li>
       
        
    </ul>
    
    <div class="btnMain">
         
         <button class="scBtn">删除</button>
    
    </div>
    
   </div>
   
   <div class="cocoLayerTit"><span>发布公告</span><i class="closeBg close" onclick="LayerCloseBox('cocoLayerWebLine')" title="关闭"></i></div>
   <iframe src="notice/add.jsp" style="width:100%;border:0px;height:410px;"></iframe>
   <div class="cocoLayerTit"><span>修改公告</span><i class="closeBg close" onclick="noticeEdit('${notice.id}')" title="关闭"></i></div>
   <iframe src="notice/edit.jsp" style="width:100%;border:0px;height:410px;"></iframe>
</div> -->

</body>
</html>
