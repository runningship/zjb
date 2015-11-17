<%@page import="org.bc.sdak.SimpDaoTool"%>
<%@page import="com.youwei.zjb.user.entity.User"%>
<%@page import="com.youwei.zjb.user.entity.Charge"%>
<%@page import="org.bc.sdak.CommonDaoService"%>
<%@page import="com.youwei.zjb.util.MailUtil"%>
<%@page import="com.youwei.zjb.ThreadSessionHelper"%>
<%@page import="com.youwei.zjb.util.DataHelper"%>
<%@page import="org.bc.sdak.TransactionalServiceHelper"%>
<%@page import="com.youwei.zjb.user.MobileUserService"%>
<%@page import="org.apache.log4j.Level"%>
<%@page import="org.bc.sdak.utils.LogUtil"%>
<%@page import="com.youwei.zjb.view.pay.AlipayNotify"%>
<%
/* *
 功能：支付宝服务器异步通知页面
 版本：3.3
 日期：2012-08-17
 说明：
 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 该代码仅供学习和研究支付宝接口使用，只是提供一个参考。

 //***********页面功能说明***********
 创建该页面文件时，请留心该页面文件中无任何HTML代码及空格。
 该页面不能在本机电脑测试，请到服务器上做测试。请确保外部可以访问该页面。
 该页面调试工具请使用写文本函数logResult，该函数在com.alipay.util文件夹的AlipayNotify.java类文件中
 如果没有收到该页面返回的 success 信息，支付宝会在24小时内按一定的时间策略重发通知
 //********************************
 * */
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%
	//获取支付宝POST过来反馈信息
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
		//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
		params.put(name, valueStr);
	}
	
	//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号

		String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//支付宝交易号

		String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
		
		String total_fee = new String(request.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8");
		
		String body = new String(request.getParameter("body").getBytes("utf8"),"UTF-8");
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		
		LogUtil.info("收到支付宝异步web回调："+trade_status +","+params);
		//if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
				CommonDaoService	dao = SimpDaoTool.getGlobalCommonDaoService();
				Charge po = dao.getUniqueByKeyValue(Charge.class,"tradeNO" , String.valueOf(out_trade_no));
				
				if(po==null){
					User user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, Integer.valueOf(request.getParameter("uid")));
					String monthAdd = new String(request.getParameter("monthAdd").getBytes("ISO-8859-1"),"UTF-8");
					Charge charge = new Charge();
					charge.uid = user.id;
					charge.uname = user.uname;
					charge.tradeNo = out_trade_no;
					charge.fee = Float.valueOf(total_fee);
					charge.payType = 1;
					charge.clientType = "mobile";
					charge.addtime = new Date();
					charge.finish = 0;
					charge.beizhu = body;
					charge.monthAdd = Integer.valueOf(monthAdd);
					SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(charge);
				}
				po = dao.getUniqueByKeyValue(Charge.class,"tradeNO" , String.valueOf(out_trade_no));
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
// 						RequestDispatcher rd = request.getRequestDispatcher("payOK.jsp");
// 						request.setAttribute("mobileDeadtime", DataHelper.dateSdf.format(user.mobileDeadtime));
// 						request.setAttribute("fee", po.fee);
// 						request.setAttribute("invitationActive", invitationActive);
// 						rd.forward(request, response);
						out.println("success");
					}catch(Exception ex){
						LogUtil.log(Level.WARN, "web charge fail", ex);
						out.println("success");
					}
				}else{
					out.println("success");
				}
			}
			
			//该页面可做页面美工编辑
			//out.println("验证成功<br />");
			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
			
			//////////////////////////////////////////////////////////////////////////////////////////
// 		}else{
// 			//该页面可做页面美工编辑
// 			out.println("订单验证失败，请联系中介宝客服 <br />");
// 		}
%>
