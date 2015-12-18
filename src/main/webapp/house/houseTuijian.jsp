<%@page import="org.bc.sdak.utils.LogUtil"%>
<%@page import="com.youwei.zjb.util.MailUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="org.bc.sdak.TransactionalServiceHelper"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="com.youwei.zjb.trial.Trial"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);

String conts = request.getParameter("conts");
String tel = request.getParameter("tel");
Trial trial = new Trial();
trial.conts = conts;
trial.tel = tel;
trial.finish = 0;
dao.saveOrUpdate(trial);
String msg = "电话:"+tel+";详情:"+conts;
try{
	List<String> toList = new ArrayList<String>();
	toList.add("894350008@qq.com");
	toList.add("253187898@qq.com");
	MailUtil.send_email(toList, "新房带看申请",msg);
}catch(Exception ex){
	LogUtil.warning(msg.toString());
}
out.write("success");
%>
