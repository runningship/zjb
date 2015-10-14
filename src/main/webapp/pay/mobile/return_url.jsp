<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="com.youwei.zjb.util.MailUtil"%>
<%@page import="org.apache.log4j.Level"%>
<%@page import="org.bc.sdak.utils.LogUtil"%>
<%@page import="org.bc.sdak.TransactionalServiceHelper"%>
<%@page import="com.youwei.zjb.user.MobileUserService"%>
<%@page import="com.youwei.zjb.util.DataHelper"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="com.youwei.zjb.user.entity.User"%>
<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="com.youwei.zjb.user.entity.Charge"%>
<%@page import="com.youwei.zjb.view.pay.AlipayNotify"%>
<%
/* *
 功能：支付宝页面跳转同步通知页面
 版本：3.2
 日期：2011-03-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 该页面可在本机电脑测试
 可放入HTML等美化页面的代码、商户业务逻辑程序代码
 TRADE_FINISHED(表示交易已经成功结束，并不能再对该交易做后续操作);
 TRADE_SUCCESS(表示交易已经成功结束，可以对该交易做后续操作，如：分润、退款等);
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Map"%>
<html>
  <head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>支付宝页面跳转同步通知页面</title>
  </head>
  <body>
<%
	//获取支付宝GET过来反馈信息
	Map<String,String> params = new HashMap<String,String>();
	Map requestParams = request.getParameterMap();
	for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
		String name = (String) iter.next();
		String[] values = (String[]) requestParams.get(name);
		String valueStr = "";
		for (int i = 0; i < values.length; i++) {
			valueStr = (i == values.length - 1) ? valueStr + values[i]
					: valueStr + values[i] + ",";
		}
		//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
		valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		params.put(name, valueStr);
	}
	LogUtil.info("收到支付宝同步web回调："+params);
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
	//商户订单号
	String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//支付宝交易号
	String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

	//交易状态
	String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
	
	//计算得出通知验证结果
	boolean verify_result = AlipayNotify.verify(params);
	
	if(verify_result){//验证成功
		//////////////////////////////////////////////////////////////////////////////////////////
		//请在这里加上商户的业务逻辑程序代码

		//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
		if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
			//判断该笔订单是否在商户网站中已经做过处理
				//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
				//如果有做过处理，不执行商户的业务程序
			CommonDaoService	dao = SimpDaoTool.getGlobalCommonDaoService();
			Charge po = dao.getUniqueByKeyValue(Charge.class,"tradeNO" , String.valueOf(out_trade_no));
			if(po!=null){
				try{
					User user = dao.get(User.class, po.uid);
					if(po.finish!=1){
						po.finish=1;
						dao.saveOrUpdate(po);
						//加时间
						Calendar cal = Calendar.getInstance();
						if(user.mobileDeadtime==null){
							cal.add(Calendar.MONTH, po.monthAdd);
							user.mobileDeadtime = cal.getTime();
						}else{
							if(user.mobileDeadtime.after(cal.getTime())){
								cal.setTime(user.mobileDeadtime);
								cal.add(Calendar.MONTH, po.monthAdd);
								user.mobileDeadtime = cal.getTime();
							}else{
								//已过期,从当前时间算起
								cal.add(Calendar.MONTH, po.monthAdd);
								user.mobileDeadtime = cal.getTime();
							}
						}
						user.lastPaytime = new Date();
						dao.saveOrUpdate(user);
						try{
							List<String> toList = new ArrayList<String>();
							toList.add("253187898@qq.com");
							MailUtil.send_email(toList, "手机版费用", po.fee+"电话:"+user.tel+",城市:"+ThreadSessionHelper.getCityPinyin());
						}catch(Exception ex){
							LogUtil.warning("pay return ---");
						}
					}else{
						LogUtil.info("订单已处理,out_trade_no="+out_trade_no);
					}
					MobileUserService mService = TransactionalServiceHelper.getTransactionalService(MobileUserService.class);
					boolean invitationActive = mService.activeInvitation(user);
					RequestDispatcher rd = request.getRequestDispatcher("payOK.jsp");
					request.setAttribute("mobileDeadtime", DataHelper.dateSdf.format(user.mobileDeadtime));
					request.setAttribute("fee", po.fee);
					request.setAttribute("invitationActive", invitationActive);
					rd.forward(request, response);
				}catch(Exception ex){
					LogUtil.log(Level.WARN, "web charge fail", ex);
					out.println("订单验证失败，请联系中介宝客服. <br />");
				}
			}else{
				out.println("订单验证失败，请联系中介宝客服 <br />");
			}
		}
		
		//该页面可做页面美工编辑
		//out.println("验证成功<br />");
		//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
		
		//////////////////////////////////////////////////////////////////////////////////////////
	}else{
		//该页面可做页面美工编辑
		out.println("订单验证失败，请联系中介宝客服 <br />");
	}
%>
  </body>
</html>