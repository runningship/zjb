<%@page import="com.youwei.zjb.util.DataHelper"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="org.bc.sdak.TransactionalServiceHelper"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%

	//获取当天的房源起始 id
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	String today = DataHelper.sdf.format(cal.getTime());
	System.out.println(today);
	String sql = "SELECT top 1 id FROM house where dateadds>='"+today+"' order by id asc";
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	List<Map> hList = dao.listSqlAsMap(sql);
	String hid="999999";
	if(!hList.isEmpty()){
		hid = hList.get(0).get("id").toString();
	}
	String hql = "SELECT comp.namea as cname, dept.namea as dname, u.id,u.uname,u.lname , xx.total FROM uc_user u , uc_comp comp ,  uc_comp dept, "
						  +"(select uid,count(*) as total from ( "
							 + "select uid ,hid ,COUNT(*) as count "
							  +"FROM  ViewHouseLog "
							  +"where hid>= "+hid
								+"group by uid,hid "
						  +") tt group by uid ) xx "
						  +"where u.id=xx.uid and u.did=dept.id and u.cid=comp.id and xx.total>200 "
						  +"order by xx.total desc";
  	
  	List<Map> list = dao.listSqlAsMap(hql);
  	request.setAttribute("list", list);
%>
<jsp:include page="../inc/resource.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="pragram" content="no-cache"> 
<meta http-equiv="cache-control" content="no-cache, must-revalidate"> 
<meta http-equiv="expires" content="0"> 
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>中介宝房源软件系统</title>
<meta name="description" content="中介宝房源软件系统">
<meta name="keywords" content="房源软件,房源系统,中介宝">
<link href="../style/css.css" rel="stylesheet">
<link href="../bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="../style/style.css" rel="stylesheet">
<script src="../js/jquery.js" type="text/javascript"></script>
<script src="../bootstrap/js/bootstrap.js" type="text/javascript"></script>
<script src="../js/dialog/jquery.artDialog.source.js?skin=win8s" type="text/javascript"></script>
<script src="../js/dialog/plugins/iframeTools.source.js" type="text/javascript"></script>
<script src="../js/DatePicker/WdatePicker.js" type="text/javascript" ></script>
<script src="../js/jquery.j.tool.js" type="text/javascript"></script>
<script src="../js/buildHtml.js" type="text/javascript" ></script>
<script src="../js/pagination.js" type="text/javascript" ></script>
<script src="../js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
<script type="text/javascript">
</script>
</head>
<body>
<div class="winThree list title addSide">
    <div class="winHeader">
      <div class="maxHW title">
      		<ul class="winMenuTop menuLi clearfix title" id="menuTop">
      			<jsp:include page="menuTop.jsp"></jsp:include>
          </ul>
          
      </div>

      <table class="table table-nopadding TableH " >
        <tr>
        	<th>公司</th>
        	<th>店面</th>
          	<th>账号</th>
          	<th>姓名</th>
          	<th>访问次数</th>
        </tr>
      </table>
    </div>
    <div class="winBodyer " style=" top: 127px;">
      <div class="winMainer maxHW" style="min-width: 700px;">

        <table class="table table-hover table-striped table-nopadding TableB" >
          <tbody>
          	<c:forEach items="${list }" var="row">
		          <tr  class="id_track_list">
		          	<td style="width:100px">${row.cname }</td>
		          	<td style="width:100px">${row.dname }</td>
		            <td style="width:100px">${row.lname }</td>
		            <td style="width:150px">${row.uname }</td>
		            <td style="width:200px">${row.total }</td>
		          </tr>
          	</c:forEach>
          </tbody>
        </table>
      </div>
    </div>
</div>
</body>
</html>