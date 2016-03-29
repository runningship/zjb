<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                <li class="act_cy hover"><a href="uc_index.jsp?act=cy" data-type="url"><i class="icon iconfont font_icon">员</i> 成员</a></li>
                <li  class="act_qx hover"><a href="uc_purview.jsp?act=qx" data-type="url"><i class="icon iconfont font_icon">权</i> 职位</a></li>
                <c:if test="${authNames.contains('sz_pc_on')}">
                    <li auth="sz_pc_on" class="act_sq hover"><a href="pc_index.jsp?act=sq" data-type="url"><i class="icon iconfont font_icon">机</i> 授权</a></li>
                </c:if>
                <c:if test="${authNames.contains('sz_phone_on')}">
                    <li auth="sz_phone_on" class="act_phone hover"><a href="phone_index.jsp?act=phone" data-type="url"><i class="icon iconfont font_icon">手</i> 手机</a></li>
                </c:if>
                <c:if test="${authNames.contains('sz_area_on')}">
                    <li auth="sz_area_on" class="act_area hover"><a href="area_index.jsp?act=area" data-type="url"><i class="icon iconfont font_icon">楼</i> 楼盘</a></li>
                </c:if>
                <c:if test="${authNames.contains('sz_tongji_normal')}">
                    <li auth="sz_tongji_normal" class="act_tj hover"><a href="trackStatistic.jsp?act=tj" data-type="url"><i class="icon iconfont font_icon">统</i> 统计</a></li>
                </c:if>
                <c:if test="${authNames.contains('sz_tongji_overvisit')}">
                    <li auth="sz_tongji_overvisit" class="act_ov hover"><a href="overVisit.jsp?act=ov" data-type="url"><i class="icon iconfont font_icon">统</i> 访问80%</a></li>
                </c:if>
                <c:if test="${authNames.contains('sz_feedback_on')}">
                    <li auth="sz_feedback_on" class="act_cs hover"><a href="fankui_index.jsp?act=cs" data-type="url"><i class="icon iconfont font_icon">意</i> 反馈</a></li>
                </c:if>
                <c:if test="${authNames.contains('sz_map_xuequ')}">
                    <li auth="sz_map_xuequ" class="act_map hover"><a href="xuequ.jsp?act=map" data-type="url"><i class="icon iconfont font_icon">map</i> 地图</a></li>
                </c:if>
<script type="text/javascript">
$(document).ready(function() {
    menuTopFun();/* 顶部自动选中 */
    $('.menuLi').on('click', 'a', function(event) {
        var Thi=$(this),
        ThiUl=Thi.parents('ul'),
        ThiLi=Thi.parents('li'),
        ThiUrl=Thi.attr('href'),
        ThiType=Thi.data('type');
        if(ThiType=='url'){
            menuSetCurr(ThiLi);
        }else if(ThiType=='dialog'){
            art.dialog.open(ThiUrl);
            return false;
        }else{
            return false;
        }
    });
});
</script>