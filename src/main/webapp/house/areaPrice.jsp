<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String area = request.getParameter("area");
	CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	Calendar now = Calendar.getInstance();
	now.add(Calendar.YEAR, -2);
	List<Map>list = dao.listAsMap("select Floor(mji/10) as mji ,AVG(djia) as junjia from House where djia>0 and area=? and dateadd>=? group by floor(mji/10))", area, now.getTime());
	StringBuilder categories = new StringBuilder("[");
	StringBuilder prices = new StringBuilder("[");
	for(int i=0;i<list.size();i++){
		Float mji = (Float)list.get(i).get("mji");
		if(mji.intValue()*10<10){
			categories.append("'<10㎡'");
		}else{
			categories.append("'").append(mji.intValue()*10).append("~").append((mji.intValue()+1)*10).append("㎡'");	
		}
		
		Double junjia = (Double)list.get(i).get("junjia");
		prices.append(junjia.intValue());
		if(i<list.size()-1){
			categories.append(",");
			prices.append(",");
		}
	}
	categories.append("]");
	prices.append("]");
	request.setAttribute("categories", categories.toString());
	request.setAttribute("prices", prices.toString());
	request.setAttribute("area", area);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>楼盘均价</title>
<meta name="description" content="">
<meta name="keywords" content="">
<script src="http://libs.baidu.com/jquery/1.11.3/jquery.min.js"></script>
<script src="../js/highcharts.js" type="text/javascript"></script>
<script type="text/javascript">
$(function () {
    $('#main').highcharts({
        chart: {
            type: 'column'
        },
        title: {
            text: '${area} 均价分布图(一年内)'
        },
//         subtitle: {
//             text: '数据统计图'
//         },
        xAxis: {
            //categories: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
            name:'面积',
        	categories: ${categories}
        },
        yAxis: {
            title: {
                text: '均价'
            }
        },
        plotOptions: {
            line: {
                dataLabels: {
                    enabled: true
                },
                enableMouseTracking: false
            }
        },
        series: [{
            name: '均价',
            //data: [7.0, 6.9, 9.5, 14.5, 18.4, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
            data:${prices}
        }]
    });
});
</script>
</head>
<body>
<div class="bodyer">
<!-- 	<div class="header"> -->
<!-- 		<a href="#" title="" class="active">图1</a> -->
<!-- 		<a href="#" title="">图2</a> -->
<!-- 		<a href="#" title="">图3</a> -->
<!-- 		<a href="#" title="">图4</a> -->
<!-- 	</div> -->
	<div class="mainer">
		<div class="echartbox" id="main"style="height:500px;border:0px solid #ccc;padding:10px;"></div>
	</div>
</div>
</body>
</html>
