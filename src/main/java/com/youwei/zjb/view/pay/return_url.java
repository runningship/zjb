package com.youwei.zjb.view.pay;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.user.entity.Charge;
import com.youwei.zjb.util.DataHelper;

public class return_url {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public Document initPage(Document doc , HttpServletRequest req){
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = req.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			try {
				valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			} catch (UnsupportedEncodingException e) {
			}
			params.put(name, valueStr);
		}
		if("TRADE_SUCCESS".equals(params.get("trade_status"))){
			Charge charge = dao.getUniqueByKeyValue(Charge.class, "tradeNo", params.get("out_trade_no"));
			if(charge==null){
				LogUtil.warning("处理充值异常,"+params);
				charge = new Charge();
			}
			try {
				charge.addtime = DataHelper.sdf.parse(params.get("notify_time"));
			} catch (Exception e) {
				charge.addtime = new Date();
			}
			charge.beizhu = params.get("body");
			charge.tradeNo = params.get("out_trade_no");
			charge.outTradeNo = params.get("trade_no");
			charge.buyerId=0;
			charge.buyerAccount= params.get("buyer_email");
			charge.feeStr = params.get("total_fee");
			try{
				charge.fee = Float.valueOf(params.get("total_fee"));
			}catch(Exception ex){
			}
			charge.payType=1;
			charge.clientType="pc";
			charge.status = params.get("trade_status");
			charge.finish=1;
			dao.saveOrUpdate(charge);
		}else{
			//该页面可做页面美工编辑
			LogUtil.log(Level.ERROR, "支付结果验证不成功,"+AlipayCore.createLinkString(params), null);
			//转到错误页面
		}
		String html = doc.html();
		String fee = params.get("total_fee");
		html= html.replace("$${total_fee}", fee==null?"":fee);
		return  Jsoup.parse(html);
	}
}
