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
    dataType:'json',
    mysuccess: function(data){
      var b = $(obj).next();
      var c =Number.parseInt(b.text()) + data.zan;
      b.text(c);
    }
  });
}

function doSearch(){
  setTimeout(function(){
    window.location.reload();
  },1000);
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
            <li><i class=" Bg gg"></i><span class="color2">公告</span></li>
            <li><span class="line"></span></li>
            <li><i class=" Bg wzsc"></i><span class="color3">网址收藏</span></li>
          </ul>
        </div>
        <div class="td oaTit oaTitBgInfo">
        </div>
      </div>
      <div class="tr w100">
          <div class="td oaInfoTit">
            <div class="txt Fleft"><span class="Fleft">文化墙最新文章</span><i class="Bg add Fleft" onclick="openNewWin('addArt','600','430','添加文章','article/add.jsp')"></i></div>  <a href="#" onclick="openListWin('listArticle','980','650','全部文章','article/list.html')">更多></a>  
          </div>
          <div class="td oaInfoTit">
            <div class="txt2 Fleft"><span class="Fleft">最近发布公告</span><i class="Bg add Fleft"  onclick="openNewWin('addGg','600','480','添加公告','notice/add.jsp')" ></i></div><a href="#" style="margin-right:25px;" onclick="openListWin('listGg','980','650','全部公告','notice/list.html')">更多></a></div>
          </div>
          <div class="tr w100">
          
            <script>
				$(function(){
					  var conNum = $(".WZcon").size();
					  for(var i=0;i<conNum;i++){
						  $(".WZcon").eq(i).attr("id","WZcon"+i);
					  }
				});
			</script>
            <div class="td" style="width:50%; height:100%;overflow:hidden;">
              <div class="oaInfomain" style="overflow:hidden; overflow-y:auto;">
                <div class="whqMain" style="padding-bottom:10px;">
                  <c:forEach items="${articleList}"  var="article">
                    <div class="textInfoBox">
                      <div class="infoBoxLine"><img src="images/pageLineicon.png" /></div>
                      <div class="infoBoxTit">
                        <div class="infoListImg Fleft"><img src="/oa/images/avatar/${article.senderAvatar }.jpg" /></div>
                        <div class="Fleft userSelectTrue">      
                          <p><span class="yh">${article.senderName }</span><span class="time"><fmt:formatDate value="${article.addtime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
                          <p><a href="#" class="tit" onclick="openNewWin('viewGg','980','650','查看文章','article/view.html?id=${article.id}')">${article.title }</a></p>
                        </div>
                        <div class="infoCaozuo">
                          <c:if test="${article.senderId==myId}">
                            <i class="Bg xgSel" onclick="openNewWin('editArt','700','450','编辑文章','article/edit.jsp?id=${article.id}')">修改</i>  
                          </c:if>
                          
                          <i class="Bg <c:if test="${article.zan==0}">zan</c:if> <c:if test="${article.zan==1}">zanSel</c:if>" onclick="selectZan(${article.id},this);return false;">点赞</i>
                          <span class="zan_count" >${article.zans}</span>
                          <!-- <i class="Bg hf">回复</i> -->
                        </div>
                      </div>
                      <div class="infoBoxContent userSelectTrue">
                        <!--<span class="marginRight55 marginLeft20">${article.conts}</span>-->
                        <span class="WZcon" style="display:none">${article.conts}</span>
                        <span class="infoMore">...</span>                
                      </div>
                    </div>
                  </c:forEach>
                  
                      <script>
								 /*document.write("<p><span class='marginLeft10 con'>"+$("#GGcon"+i).text()+"</span></p>");*/
								 $(function(){
										  var conNum = $(".WZcon").size();
										  for(var i=0;i<conNum;i++){
											  $("#WZcon"+i).parent().append("<p><span class='marginLeft10 con'>"+$("#WZcon"+i).text()+"</span></p>");
										  }
								 });
					  </script>
                  
                  
                </div>
              </div>
            </div>
            
		  <script>
		    $(function(){
				  var conNum = $(".GGcon").size();
				  for(var i=0;i<conNum;i++){
					  $(".GGcon").eq(i).attr("id","GGcon"+i);
				  }
			});
          </script>
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
                            <p><span class="titL marginLeft10">Tit:</span><span class="tit marginLeft10 userSelectTrue" onclick="openNewWin('viewGg','980','650','查看公告','notice/view.html?id=${notice.id}')">${notice.title}</span></p><!--
                            <p><span class="marginLeft10 con">${notice.conts}</span></p>-->
                            <span class="GGcon" style="display:none">${notice.conts}</span>
							
                          </div>
                          <div class="ggBoxCaozuo">
                            <c:if test="${notice.senderId==myId}">
                              <i class="Bg xgSel" onclick="openNewWin('editGg','700','450','编辑公告','notice/edit.jsp?id=${notice.id}')">修改</i>
                            </c:if>
                            <i class="Bg zanSel">查看</i>
                            <!-- <i class="Bg hfSel">回复</i> -->
                          </div>
                        </div>
                      </c:forEach>
                      <script>
								 /*document.write("<p><span class='marginLeft10 con'>"+$("#GGcon"+i).text()+"</span></p>");*/
								 $(function(){
										  var conNum = $(".GGcon").size();
										  for(var i=0;i<conNum;i++){
											  $("#GGcon"+i).parent().append("<p><span class='marginLeft10 con'>"+$("#GGcon"+i).text()+"</span></p>");
											 // $(".GGcon"+i).text();
											  /*document.write("<p><span class='marginLeft10 con'>"+$("#GGcon"+i).text()+"</span></p>");*/
										  }
								 });
					  </script>

                      
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

</body>
</html>
