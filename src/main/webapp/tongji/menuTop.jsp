<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<li class="act_tj hover"><a href="trackStatistic.jsp?act=tj" data-type="url"><i class="icon iconfont font_icon">统</i> 统计</a></li>
      <c:if test="${authNames.contains('tongji_ov_on')}">
      	<li class="act_ov hover"><a href="overVisit.jsp?act=ov" data-type="url"><i class="icon iconfont font_icon">统</i> 访问80%</a></li>
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