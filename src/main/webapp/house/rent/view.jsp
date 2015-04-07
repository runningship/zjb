<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<link href="/style/css.css" rel="stylesheet">
<link href="/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="/style/style.css" rel="stylesheet">
<link href="/style/css_ky.css" rel="stylesheet">
<script src="/js/jquery.js" type="text/javascript"></script>
<script src="/bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="/js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="/js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="/js/jquery.input.js" type="text/javascript"></script>
<script src="/js/jquery.j.tool.js" type="text/javascript"></script>
<script type="text/javascript" src="/js/buildHtml.js"></script>
<script type="text/javascript">
var houseGJbox;
chuzu=getParam('chuzu');
function replaceAlls(a){
  var aa=a;
  aa=aa.replace(/ /g, '');
  aa=aa.replace(/　/g, '');
  aa=aa.replace(/，/g, ',');
  aa=aa.replace(/、/g, ',');
  aa=aa.replace(/\//g, ',');
  return aa;
}
function reload_genjin(){
  window.location.reload();
}
function selectArea(area){
  $(window.parent.document).find('#search').val(area);
  window.parent.doSearchAndSelectFirst();
}
function sideBtnFun(){
    $('.sideCont').on('click', '.btns', function(event) {
        var Thi=$(this),ThiHid=Thi.data('hid'),ThiType=Thi.data('type');
        if(ThiType=='addGenjin'){
            if(houseGJbox)houseGJbox.close();
            houseGJbox=art.dialog({
                follow: document.getElementById('addGenjinBtn'),
                content: '加载中...',
                title:'添加跟进',
                left:20,
                id:'houseGenjinBox',
                width:'250px',
                padding:'0'
            });
            // jQuery ajax   
            YW.ajax({
                url: '/v/house/house_genjin_add_v2.html',
                mysuccess: function (data) {
                    //blockAlert(data)
                    houseGJbox.content(data);
                },cache: false
            });
            return false;
        }else if(ThiType=='fav'){
            var ThiIsFav=Thi.attr('fav'),
            url,param={
                houseId:$('#dimHouseId').text(),
                chuzu:chuzu
            }
            if(ThiIsFav=='1'){
              //表示当前点击按钮的作用是收藏房源,因此此时房源是未收藏状态
                if(chuzu=='1'){
                  url='addRent';
                }else{
                  url='addSell';
                }
                
                alertStr="收藏成功";
                dimStr='<i class="iconfont">&#xe62e;</i> 已收藏';
                Thi.attr('fav',0).attr('title','已收藏');
            }else{
                if(chuzu=='1'){
                  url='deleteRent';
                }else{
                  url='deleteSell';
                }
                
                alertStr="已取消收藏";
                dimStr='<i class="iconfont">&#xe648;</i> 收藏';
                Thi.attr('fav',1).attr('title','点此收藏');
            }
            YW.ajax({
                type: 'POST',
                url: '/c/house/fav/'+url,
                data:param,
                mysuccess: function(data){
                    //var json = JSON.parse(data);
                    alert(alertStr);
                    Thi.html(dimStr);
                },complete:function(){
                    
                }
            });
            return false;
        }else if(ThiType=='map'){
            ThiArea=Thi.data('area');
            art.dialog.open('/v/house/baidu_map.html?area='+ThiArea,{id:'seemap',width:800,height:600});
            return false;
        }else if(ThiType=='copy'){
            alert('不给复制');
            return false;
        }else{
            return false;
        }
    });
}
$(document).ready(function() {
    sideBtnFun();
    $('[data-toggle=tooltip]').tooltip();

    var GenjinTbody=$('.see_house_genjin').find('tbody'),
    GenjinTbodyHtml=GenjinTbody.html().replace(/(\n)+|(\r\n)+/g, "");
    GenjinTbodyHtml=GenjinTbodyHtml.replace('   ','');

    if(GenjinTbodyHtml==' '){
      $('.see_house_genjin').find('tbody').html('<tr><td class="side_noThing">暂无跟进...</td></tr>');
      $('#addGenjinBtn').tooltip('show');
    }

});
$('#area').on('click',function(){
  var Thi=$(this),
  ThiVal=Thi.find('b').text();
  $('#search').val(ThiVal);
  $('#searchBtn').click();
})
</script>
</head>
<body >
<div class="sideCont" style="width:100%; display:table; height:100%;-webkit-user-select: text;">

     <div style="display:table-row;">   
        <div class="sideHead TableMainLeftTit" style="display:table-cell;">
            房源详细
        </div>
     </div>
        
        
     <div style="display:table-row;">     
        <div style="display:table-cell; width:100%;">
        <div style="height:100%; overflow:hidden; overflow-y:auto;">
        <table width="100%" class="TableMainL" id="seeHouse ">
        <tbody>
          <tr>
            <td class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">编号：</td>
                  <td class="neirong TextColor1" id="dimHouseId">${house.id}</td>
                </tr>
              </table>
            </td>
            <td class="You">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">状态：</td>
                  <td class="neirong TextColor1">${ztai} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti biaotiMax">发布人员：</td>
                  <td class="neirong TextColor1">${dname}${fbr}</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti biaotiMax">发布时间：</td>
                  <td class="neirong TextColor1"><fmt:formatDate value="${house.dateadd}" pattern="yyyy-MM-dd HH:mm"/> &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti" selectstart="yes">楼盘：</td>
                  <td class="neirong"><span onclick="selectArea('${house.area}')" class="onselect" id="area" style=" cursor: pointer; color:#06C; font-weight:bold; font-size:14px;text-decoration: underline;"><b>${house.area}</b> <c:if test="${house.seeFH==1}">${fhao}#${dhao}</c:if> <c:if test="${house.seeFH!=1}">${dhao}</c:if></span>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <c:if test="${!empty house.title}">
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">标题：</td>
                  <td class="neirong TextColor1">${house.title} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          </c:if>
          <tr>
            <td class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">面积：</td>
                  <td class="neirong TextColor1"><span style=" color:#F00; font-weight:bold;">${house.mji}</span> 平方 </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
				  <td class="biaoti">租金：</td>
				  <td class="neirong">
				  ${house.zjia} 元
				  </td>
				</tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">楼型：</td>
                  <td class="neirong TextColor1">${house.lxing} &nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">装潢：</td>
                  <td class="neirong TextColor1">${house.zxiu} &nbsp;</td>
                </tr>
              </table></td>
            <td class="You">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
				  <td class="biaoti">方式：</td>
				  <td class="neirong">
				  <span style=" color:#F00; font-weight:bold;">${fangshi}</span>
				  </td>
				</tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">楼层：</td>
                  <td class="neirong TextColor1">${house.lceng}/${house.zceng} &nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">户型：</td>
                  <td class="neirong TextColor1">${house.hxf}室${house.hxt}厅${house.hxw}卫 &nbsp;</td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">其他：</td>
                  <td class="neirong TextColor1">
                  	<c:if test="${house.wo!=''}">${house.wo}</c:if> <c:if test="${house.xianzhi!=''}">${house.xianzhi}</c:if>
                  </td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">区域：</td>
                  <td class="neirong TextColor1">${house.quyu} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <c:if test="${!empty house.peizhi}">
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">配置：</td>
                  <td class="neirong TextColor1">${house.peizhi} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          </c:if>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">地址：</td>
                  <td class="neirong TextColor1">${house.address} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti">备注：</td>
                  <td class="neirong TextColor1" style="padding-right:10px;">${house.beizhu} &nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
          <tr>
            <td colspan="2" class="Zuo">
            
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td>
                    <table width="100%">
                    	<c:if test="${house.seeHM==1}">
                    	  <tr>
	                        <td class="biaoti">房主：</td>
	                        <td >${house.lxr} 
                            <c:choose> 
                              <c:when test="${empty house.telImg}">   
                                ${house.tel}
                              </c:when>
                              <c:otherwise>
                                <img src="${house.telImg}" />
                              </c:otherwise> 
                            </c:choose>
	                        </td>
	                      </tr>		
                    	</c:if>
                      
                    </table>
                  </td>
                </tr>
              </table>
              <table width="100%" border="0" cellspacing="0" cellpadding="0" class="zhuyao">
                <tr>
                  <td class="biaoti biaotiMax">业务人员：</td>
                  <td class="neirong TextColor1">${house.forlxr} ${house.fortel}&nbsp;</td>
                </tr>
              </table>
            </td>
          </tr>
        </tbody>
        </table>
        
        <table class="TableMainLGj see_house_genjin" width="100%">
          <thead>
            <tr>
              <th><h2>跟进列表</h2></th>
            </tr>
          </thead>
          <tbody>
          <c:forEach items="${gjList}" var="gj">
          	<tr class="list">
              <td style="padding-left:5px;padding-right:5px">
                <div style="margin-bottom:4px;">${gj.conts}</div>
                <p style="display:inline-block;width:110px;white-space:nowrap;text-overflow:ellipsis;overflow:hidden"><span title="${gj.uname}">${gj.dname}-${gj.uname}</span></p> <span style="float:right;color:#999999"><fmt:formatDate value="${gj.addtime}" pattern="yyyy-MM-dd HH:mm"/></span>
              </td>
            </tr>
          </c:forEach>
          
          </tbody>
        </table>
        </div>
        </div>
     </div>
        
        
        
        
     <div style="display:table-row; height:30px; overflow:hidden;">     
        <div class="sideFoot" style="display:table-cell; height:32px; overflow:hidden;">
        
          <ul class="bottomAction" style="margin-bottom:-3px;">
            <li>
              <a href="#" data-toggle="tooltip" title="添加跟进" class="btns" data-type="addGenjin" id="addGenjinBtn"><i class="iconfont">&#xe640;</i> 添加跟进</a>
              <c:if test="${fav==0}">
              	<a href="#" data-toggle="tooltip" title="收藏房源" class="btns" data-type="fav" fav="1"><i fav="1" class="iconfont">&#xe648;</i> 收藏</a>	
              </c:if>
              <c:if test="${fav==1}">
              	<a href="#" data-toggle="tooltip" title="已收藏房源" class="btns" data-type="fav" fav="0"><i fav="0" class="iconfont">&#xe62e;</i> 已收藏</a>
              </c:if>
              <a href="#" data-toggle="tooltip" title="地图查看" class="btns" data-type="map" data-area="${house.area}"><i class="iconfont">&#xe60e;</i> 地图</a>
            </li>
          </ul>
        </div>
     </div>


</div>
</body>
</html>

