<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<jsp:include page="../inc/top.jsp" />
<title>中介宝房源软件系统</title>
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" src="/js/pagination.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script type="text/javascript">
var rowId;
var dataScope="ky";
function doSearch(callback,isdel){
  if(event==undefined || $(event.srcElement).attr('action')!='page'){
    if(isdel==false){
      $('.pageInput').val(1);
    }
  }
  var a=$('form[name=form1]').serialize();
  YW.ajax({
    type: 'POST',
    url: '/c/weixin/houseOwner/listData',
    data:a,
    mysuccess: function(data){
        houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_client_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        if(callback){
          callback();
        }
    }
  });
}

function getShenHeText(lock){
  if(lock==1){
    return "审";
  }else{
    return "未";
  }
}

function shenheFy(id , obj){
  // event.cancelBubble=true;
  YW.ajax({
    type: 'POST',
    url: '/c/weixin/houseOwner/toggleShenHe?id='+id,
    mysuccess: function(data){
        var json = JSON.parse(data);
        if(json.sh==1){
          $(obj).text('审');
          $(obj).css('color','green');
        }else{
          $(obj).text('未');
          $(obj).css('color','red');
        }
    }
  });
}

$(document).ready(function() {
  Page.Init();
  doSearch();
});
</script>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<style type="text/css">
  .aui_content{padding:10px 15px; font-size: 14px;}
  .KY_TableMain td a.shenhe_0{color:red;}
</style>
</head>
<body>


<div class="KY_Main KY_W">
     <div class="MainRight">
          
          <div style="display:table; width:100%; height:100%; overflow:hidden;" class="not-select">
              <div class="MainRightInputMain KY_W not-select">
              
      <form class="form-horizontal form1" onsubmit="doSearch();return false;" role="form" name="form1">
                   <ul class="InputMainLine KY_W not-select" style="margin-bottom:8px;">
                        <li><input class="input-sm w110" type="text" placeholder="姓名" name="name"/></li>
                        <li><input class="input-sm w110" type="text" placeholder="电话" name="tel"/></li>
                        <li class="LiBoxW1">
                             <button class="ButtonW1 ButtonS hand not-select" type="submit" onclick="doSearch();return false;" value="enter">搜索</button>
                             <button class="ButtonW1 ButtonQ hand not-select" type="button" style="display:none" onclick="window.location.reload();">清空</button>
                        </li>
                   </ul>
                   
              </form>
              </div>
               
               
              <div class="KY_W" style="display:table-row;overflow:hidden;">            
                   <div style="margin:0 0.5%; width:99%; height:31px; margin-bottom:-1px; border-bottom:1px solid #dddddd; border-left:1px solid #d1d1d1; float:left; border-right:1px solid #d1d1d1; background-color:#e9e9e9; overflow-y:scroll;">
                           
                                   <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain" style=" margin-bottom:-1px;">
                                    <tr>
                                            <th width="160">房主电话</th>
                                            <th width="30">操作</th>
                                            
                                     </tr>
                                          
                                    </table>
                           
                    </div>   
              </div>      
            
              <div class="MainRightConMain KY_W" style="display:table-row; position:relative; cursor:default;">
                <div class="KY_MainRCon" style="height:100%; margin:0 0.5%; width:99%; display:table-cell; border-bottom:none; border-top:none; overflow:hidden; overflow-y:scroll;">
                  <table id="KY_TableListKy" border="0" cellspacing="0" cellpadding="0" class="KY_TableMain table-hover">
                    <tr data-cid="$[id]" style="display:none;" class="id_client_list"  >
                      <td width="160" runscript = "true">$[tel]</td>
                      <td width="30"> 
                        <a href="##" class="shenhe_$[sh]" data-hid="$[id]" runscript="true" data-rel="del" onclick="shenheFy($[id],this)">getShenHeText($[sh])</a>
                      </td>
                    </tr>
                  </table>
                </div>
              </div>  
              
              
              <div style="display:table-row; height:33px;">
                                
                   <div class="divPage"  style=" width:99%; margin:0 0.5%;">
                   
                        <div class="pageMain footer">
                          <div class="maxHW mainCont ymx_page foot_page_box"></div>
                        </div>
                   
                   </div>              
                                
              </div>
              
               
           </div>
     </div>


</div>

</body>

</html>
