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
var artPage = 1;
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

function getMore(){
  $('.jzMore').text('加载中...');
  artPage ++;
  YW.ajax({
    type: 'POST',
    url: '/c/piazza/listKnowledge?currentPageNo='+artPage,
    dataType:'json',
    mysuccess: function(data){
      buildArticle(data);
      $('.jzMore').text('点击加载更多');
    }
  });
}

function buildArticle(page){
  var json = page['page']['data'];
  if(json.length==0){
    $('.jzMore').css('display','none');
  }
  for (var i = 0; i < json.length; i++) {
    var conTxt = json[i]['conts'];
	
	//return false;
	//conTxt = conTxt.text();
    var html = '<div class="textInfoBox">'
             +  ' <div class="infoBoxLine"><img src="images/pageLineicon.png"></div>'
             +   '<div class="infoBoxTit">'
             +    ' <div class="infoListImg Fleft"><img src="/oa/images/avatar/'+json[i]['senderAvatar']+'.jpg"></div>'
             +    ' <div class="Fleft userSelectTrue">    '  
             +       '<p><span class="yh">'+json[i]['senderName']+'</span><span class="time">'+json[i]['addtime']+'</span></p>'
             +       '<p><a href="#" class="tit" onclick="openNewWin(\'viewSail\',\'980\',\'650\',\'查看知识\',\'sale/view.html?id='+json[i]['id']+'\')">'+json[i]['title']+'</a></p>'
             +     '</div>'
             +     '<div class="infoCaozuo">';
             // +     '<c:if test="${article.senderId==myId}">'
             // +        '<i class="Bg xgSel" onclick="openNewWin('editArt','700','450','编辑知识','/knowledge/edit.jsp?id=json[i]['id']')">修改</i>'  
             // +     '</c:if>'
    if(json[i].senderId==${myId}){
      html+='<i class="Bg xgSel" onclick="openNewWin(\'editKnowledge\',\'700\',\'450\',\'编辑知识\',\'/knowledge/edit.jsp?id='+json[i].id+'\')">修改</i>';
    }
    if(json[i].zan==0){
      html  +=     '<i class="Bg zan " style="margin-right:3px" onclick="selectZan('+json[i]['id']+',this);return false;">点赞</i>';
    }else{
      html  +=     '<i class="Bg zanSel " style="margin-right:3px" onclick="selectZan('+json[i]['id']+',this);return false;">点赞</i>';
    }
    html = html+       '<span class="zan_count">'+json[i]['zans']+'</span>'
             +     '</div>'
             +   '</div>'
             +   '<div class="infoBoxContent userSelectTrue">'
             +     '<span class="WZcon" style="display:none;" id="WZconAdd'+ i +'"><pre>'+conTxt+'</pre></span><div class="infoMore">...</div>'
             +   '</div>'
             + '</div>';
    $('.whqMain').append(html);
	
	$("#WZconAdd"+i).parent().append("<span class='marginLeft10 con' style=' white-space:nowrap; max-width:500px; overflow:hidden; display:inline-block;'>"+$("#WZconAdd"+i).text()+"</span>");
	$("#WZconAdd"+i).remove();
  }
}

</script>
<body>
<div class="oa_Main oa_W" id="oaMainPage">
  <div class="oaMain">
    <div class="table w100 h100">
      <div class="tr w100">
        <div class="td oaTit oaTitBgInfo">
          <ul class="titBox">
            <li href="#" onclick="window.location.reload()"><i class=" Bg whq"></i><span class="color1">房源知识分享</span></li>
            <li><span class="line"></span></li>
            <li href="#" onclick="window.location.reload()"><i class=" Bg gg"></i><span class="color2">增值服务平台</span></li>
            <!-- <li><span class="line"></span></li>
            <li><i class=" Bg wzsc"></i><span class="color3">网址收藏</span></li> -->
          </ul>
        </div>
        <div class="td oaTit oaTitBgInfo">
        </div>
      </div>
      <div class="tr w100">
          <div class="td oaInfoTit">
            <div class="txt Fleft"><span class="Fleft">房源知识最新分享</span>
            <!-- <c:if test="${auths.indexOf('oa_article_add')>-1}"> -->
              <i class="Bg add Fleft" onclick="openNewWin('addKnowledge','800','600','添加知识','knowledge/add.jsp')"></i>
            <!-- </c:if> --><span style="color:#9cabb4;font-size:12px;">集齐50个赞，可获得50元现金</span>
            </div>  
            <form name="form1" style="float: right;"><div  style="margin-top: 14px;margin-right: 65px; "><input type="text" placeholder="关键字搜索" style="height: 25px;" /><img src="images/search.png" style="height: 25px;position: fixed;" onclick=""></div>
            <a href="#" style="margin-top: -42px;" onclick="openListWin('listKnowledge','980','650','全部知识','knowledge/list.jsp')">更多></a>  </form>
          </div>
          <div class="td oaInfoTit">
            <div class="txt2 Fleft"><span class="Fleft">增值服务平台最近信息</span>
            <!-- <c:if test="${auths.indexOf('oa_notice_add')>-1}"> -->
              <i class="Bg add Fleft"  onclick="openNewWin('addSail','800','600','添加转让','sale/add.jsp')" ></i>
            <!-- </c:if> --> <span style="color:#9cabb4;font-size:12px;">可发布物品出售、转让等</span>
            </div> 
            <form name="form2" style="float: right;"><div style="margin-top: 14px;margin-right: 90px; "><input type="text" name="title" placeholder="关键字搜索" style="height: 25px;" /><img src="images/search.png" style="height: 25px;position: fixed;" onclick=""></div>
            <a href="#" style="margin-right:25px;margin-top: -42px;" onclick="openListWin('listSail','980','650','全部转让','sale/list.jsp')">更多></a></div></form>
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
            <div class="td" style="width:50%; height:100%;overflow:hidden; position:relative;">
              
              <div class="oaInfomain" style="overflow:hidden; overflow-y:auto;">
                <div class="whqMain" style="padding-bottom:9px;">
                  <c:forEach items="${KnowledgeList}"  var="article">
                    <div class="textInfoBox">
                      <div class="infoBoxLine"><img src="images/pageLineicon.png" /></div>
                      <div class="infoBoxTit">
                        <div class="infoListImg Fleft"><img src="/oa/images/avatar/${article.senderAvatar }.jpg" /></div>
                        <div class="Fleft userSelectTrue">      
                          <p><span class="yh">${article.senderName }</span><span class="time"><fmt:formatDate value="${article.addtime}" pattern="yyyy-MM-dd HH:mm:ss"/></span></p>
                          <p><a href="#" class="tit" onclick="openNewWin('viewKnowledge','980','650','查看知识','knowledge/view.html?id=${article.id}')">${article.title }</a></p>
                        </div>
                        <div class="infoCaozuo">
                          <!-- <c:if test="${article.senderId==myId}"> -->
                            <i class="Bg xgSel" onclick="openNewWin('editKnowledge','800','600','编辑知识','knowledge/edit.jsp?id=${article.id}')">修改</i>  
                          <!-- </c:if> -->
                          
                          <i class="Bg <c:if test="${article.zan==0}">zan</c:if> <c:if test="${article.zan==1}">zanSel</c:if>" onclick="selectZan(${article.id},this);return false;">点赞</i>
                          <span class="zan_count" >${article.zans}</span>
                          <!-- <i class="Bg hf">回复</i> -->
                        </div>
                      </div>
                      <div class="infoBoxContent userSelectTrue">
                        <!--<span class="marginRight55 marginLeft20">${article.conts}</span>-->
                        <span class="WZcon" style="display:none;"><pre>${article.conts}</pre></span>
                        <div class="infoMore">...</div>
                      </div>
                    </div>
                  </c:forEach>
                  
                  <div class="jzMore" onclick="getMore();">点击加载更多</div>
                      <script>
								 /*document.write("<p><span class='marginLeft10 con'>"+$("#GGcon"+i).text()+"</span></p>");*/
								 $(function(){
										  var conNum = $(".WZcon").size();
										  for(var i=0;i<conNum;i++){
											  $("#WZcon"+i).parent().append("<span class='marginLeft10 con' style=''>"+$("#WZcon"+i).text()+"</span>");
											  //$("#WZcon"+i).remove();
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
                      <c:forEach items="${SailList}"  var="notice">
                        <div class="ggBox">
                          <div class="ggBoxTime">
                            <p class="month"><fmt:formatDate value="${notice.addtime}" pattern="MMM"/></p>
                            <p class="day"><fmt:formatDate value="${notice.addtime}" pattern="dd"/></p>
                          </div>
                          <div class="ggBoxContent">
                            <p><span class="titL marginLeft10">Tit:</span><a href="#" class="tit marginLeft10 userSelectTrue" onclick="openNewWin('viewSail','980','650','查看转让','sale/view.html?id=${notice.id}')">${notice.title}</a></p>
                            <!--<p><span class="marginLeft10 con">${notice.conts}</span></p>-->
                            <span class="GGcon" style="display:none">${notice.conts}</span>
							
                          </div>
                          <div class="ggBoxCaozuo">
                            <!-- <c:if test="${notice.senderId==myId}"> -->
                              <i class="Bg xgSel" onclick="openNewWin('editSail','800','600','编辑转让','sale/edit.jsp?id=${notice.id}')">修改</i>
                              <!-- <i class="Bg xgSel" onclick="openNewWin('editSail','800','600','编辑转让','sale/edit.jsp?id=${notice.id}')">审核</i> -->
                            <!-- </c:if> -->
                            <!-- <i class="Bg zanSel">查看</i> -->
                            <!-- <i class="Bg hfSel">回复</i> -->
                          </div>
                        </div>
                      </c:forEach>
                      <script>
								 /*document.write("<p><span class='marginLeft10 con'>"+$("#GGcon"+i).text()+"</span></p>");*/
								 $(function(){
										  var conNum = $(".GGcon").size();
										  for(var i=0;i<conNum;i++){
											  $("#GGcon"+i).parent().append("<p><span class='marginLeft10 con userSelectTrue' style='font-family:\"宋体\"; line-height:18px;'>"+$("#GGcon"+i).text()+"</span></p>");
											 // $(".GGcon"+i).text();
											  /*document.write("<p><span class='marginLeft10 con'>"+$("#GGcon"+i).text()+"</span></p>");*/
										  }
								 });
					  </script>

                      
                    </div>
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
