<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>房源</title>
<link rel="stylesheet" type="text/css" href="/style/css_ky.css" />
<link rel="stylesheet" type="text/css" href="/style/font/iconfont.css" />
<script type="text/javascript" src="/js/jquery.js"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript" src="/js/pagination.js"></script>
<script type="text/javascript" src="/js/DatePicker/WdatePicker.js"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.v2.js" type="text/javascript"></script>
<script type="text/javascript">
var id;
var houseData;
var houseGJbox;
var searching=false;
var chuzu=getParam('chuzu');
function doSearch(callback){
  if(searching){
    return;
  }
  if(event==undefined || $(event.srcElement).attr('action')!='page'){
    $('.pageInput').val(1);
  }
  searching=true;
  $('#searchBtn').attr('disabled','disabled');
  var a=$('form[name=form1]').serialize();
  // pushQueryToHistory($('form[name=form1]').serializeArray());
  YW.ajax({
    type: 'POST',
    url: '/c/client/matchHouse?cid='+id,
    data:a,
    mysuccess: function(data){
        houseData=JSON.parse(data);
        buildHtmlWithJsonArray("id_House_list",houseData['page']['data']);
        Page.setPageInfo(houseData['page']);
        
        if(callback){
          callback();
        }
    },
    complete:function(){
      searching=false;
      $('#searchBtn').removeAttr('disabled');
    }
  });
}

function doSearchAndSelectFirst(){
  doSearch(function(){
    if(houseData.page.data.length>0){
      $('.TableB').find('tr')[1].click();
    }
  });
}

function getSider(id){
    if(id){
        $('#sideCont').attr('src','/v/house/houseSee_v2.html?id='+id+'&chuzu='+chuzu);
    }
}

$(document).ready(function(){
  id = getParam('id');
    ChangeW();
    Page.Init();
    doSearchAndSelectFirst();
    $("button.selectMethod").parent().hover(function(){
      $(this).siblings().find("div.ulErMenu").hide();
      $(this).find("div.ulErMenu").show();
    },function(){
         $(this).find("div.ulErMenu").hide(); 
    });
    $('.id_House_list').on('click', 'a', function(event) {
        var Thi=$(this),
        rel=Thi.data('rel'),
        this_hid=Thi.data('hid');
    });
    $('.TableB').on('click', 'tr', function(event) {
        var Thi=$(this),
        ThiHid=Thi.data('hid');
        if(houseGJbox)houseGJbox.close();
        getSider(ThiHid);
        // Thi.toggleClass('selected');
        return false;
    });
    // tableHover();
});

</script>
<style type="text/css">
  .selected{
    background-color: blue;
  }

.KY_TableMain td a.shenhe_0{color:red;}
.ztai_已售{color:red;}
.ztai_在售{color:blue;}
.ztai_停售{color:orange;}
</style>
</head>

<body>

<div class="KY_Main KY_W">

     
     <div class="MainRight">
          
          <div style="display:table; width:100%; height:100%; overflow:hidden;">
              <div class="MainRightInputMain KY_W" style="margin-bottom:5px;padding:0px;">
                <form class="form-horizontal form1" onsubmit="doSearchAndSelectFirst();return false;" role="form" name="form1">
                   <ul class="InputMainLine KY_W">
                        <li class="LiBoxW1">
                             <button id="searchBtn" class="ButtonW1 ButtonS hand " style="display:none;" type="submit">搜索</button>
                        </li>
                   </ul>
                </form>
              </div>
              
              
              
              <div class="MainRightConMain KY_W" style="display: table-row; overflow:hidden;">

                <div class="MainRightConL" style=" height:100%; display:table-cell; overflow:hidden; overflow-y:auto;margin-top:0px;">
                  <!-- <div class="sideCont" id="sideCont"></div> -->
                    <iframe id="sideCont" src="" style="width:100%;height:100%;border:none"></iframe>
                </div>

                <div class="MainRightConR" id="FY_RCon" style="margin:0px;overflow:hidden; overflow-y:hidden; display:table-cell; float:left;">
                  <div style="width:100%; display:table; height:100%;">
                                <div style="display:table-row;">  
                                                  
                                      <div style=" width:100%; display:table-cell;">
                                     
                                             <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain" id="FY_TableTit">
                                                    <tr>
                                                      <th width="60">编号</th>
                                                      <th width="50">状态</th>
                                                      <th width="60">区域</th>
                                                      <th>楼盘名称</th>
                                                      <th width="50">楼型</th>
                                                      <th width="60">户型</th>
                                                      <th width="50">面积</th>
                                                      <th width="50">总价(万)</th>
                                                      <th width="50">单价</th>
                                                      <th width="50">楼层</th>
                                                      <th width="50">装潢</th>
                                                      <th width="90">发布时间</th>
                                                    </tr>
                                              </table>
                                             
                                     
                                      </div>
                                  
                                  </div>
                                    
                                 <div style="display:table-row;">           

                                            <div class="FY_RCon" style=" width:100%; display:table-cell;">
                                                <div style="height:100%; float:left; overflow:hidden; overflow-y:auto;">
                                                    <table border="0" cellspacing="0" cellpadding="0" class="KY_TableMain TableB table-hover" id="KY_TableMain">
                                                        <tr data-hid="$[id]" style="display:none;height:33px;" class="id_House_list">
                                                          <td width="60"><span class="piliang hidden"><input type="checkbox" name="ids" value="$[id]" style="display:none"> </span>$[id]</td>
                                                          <td width="50" class="ztai_$[ztai]">$[ztai]</td>
                                                          <td width="60">$[quyu]</td>
                                                          <td class="br_area">$[area] <span show="$[seeFH]==1">$[dhao]-$[fhao]</span></td>
                                                          <td width="50">$[lxing]</td>
                                                          <td width="60">$[hxf]-$[hxt]-$[hxw]</td>
                                                          <td width="50">$[mji]</td>
                                                          <td width="50">$[zjia]</td>
                                                          <td width="50" class="cs">$[djia]</td>
                                                          <td width="50">$[lceng]/$[zceng]</td>
                                                          <td width="50">$[zxiu]</td>
                                                          <td width="90" class="sy" runscript="true" title="$[dateadd]">'$[dateadd]'.split(' ')[0]</td>
                                                      </tr>
                                                    </table>
                                                </div>
                                            </div>
                                  </div>

                                <div style="display:table-row; height:35px; overflow:hidden;">     
                                     <div class="divPage"  style=" height:35px; margin:0; background:none;">  
                                        <div class="pageMain footer">
                                          <div class="maxHW mainCont ymx_page foot_page_box"></div>
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
