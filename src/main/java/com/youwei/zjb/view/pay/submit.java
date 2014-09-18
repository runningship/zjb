package com.youwei.zjb.view.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.user.entity.Charge;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;

public class submit {

	public static final String partner = "2088801295914922";
	public final static String key="5itxn2rr0fkwxbixlasbu7wco0ngzgpy";
	public final static String input_charset = "utf-8";
	static final String payment_type = "1";
	static final String sign_type="MD5";
	public Document initPage(Document doc , HttpServletRequest req){
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", partner);
        sParaTemp.put("_input_charset", input_charset);
		sParaTemp.put("payment_type", payment_type);
		sParaTemp.put("return_url", req.getParameter("return_url"));
		sParaTemp.put("notify_url", req.getParameter("return_url"));
		sParaTemp.put("seller_email", req.getParameter("seller_email"));
		sParaTemp.put("out_trade_no", req.getParameter("out_trade_no"));
		sParaTemp.put("subject", req.getParameter("subject"));
		sParaTemp.put("total_fee", req.getParameter("total_fee"));
		String body = req.getParameter("body");
		if(StringUtils.isEmpty(body)){
			body = req.getParameter("subject");
		}
		sParaTemp.put("body", body);
//		sParaTemp.put("show_url", req.getParameter("show_url"));
		sParaTemp.put("anti_phishing_key", "");
		sParaTemp.put("exter_invoke_ip", "");
		
//		try{
//			sParaTemp.put("service", "create_direct_pay_by_user");
//	        sParaTemp.put("partner", partner);
//	        sParaTemp.put("_input_charset", input_charset);
//			sParaTemp.put("payment_type", payment_type);
//			sParaTemp.put("return_url", new String(req.getParameter("return_url").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("notify_url", new String(req.getParameter("notify_url").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("seller_email", new String(req.getParameter("seller_email").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("out_trade_no", new String(req.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("subject", new String(req.getParameter("subject").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("total_fee", new String(req.getParameter("total_fee").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("body", new String(req.getParameter("body").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("show_url", new String(req.getParameter("show_url").getBytes("ISO-8859-1"),"UTF-8"));
//			sParaTemp.put("anti_phishing_key", "");
//			sParaTemp.put("exter_invoke_ip", "");
//			}catch(UnsupportedEncodingException ex){
//				ex.printStackTrace();
//			}
		
		 Map<String, String> sPara = buildRequestPara(sParaTemp);

		String html = doc.html();
		for(String key : sPara.keySet()){
			html = html.replace("$${"+key+"}", sPara.get(key));
		}
		String trade_no = sParaTemp.get("out_trade_no");
		Charge po = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(Charge.class,"tradeNO" , trade_no);
		if(po==null){
			req.getParameter("uid");
			User user = SimpDaoTool.getGlobalCommonDaoService().get(User.class, Integer.valueOf(req.getParameter("uid")));
			Department dept = user.Department();
			Department comp = dept.Company();
			Charge charge = new Charge();
			charge.uid = user.id;
			charge.uname = user.uname;
			charge.did = dept.id;
			charge.dname = dept.namea;
			charge.cid = comp.id;
			charge.cname = comp.namea;
			charge.tradeNo = sParaTemp.get("out_trade_no");
			charge.fee = Float.valueOf(sParaTemp.get("total_fee"));
			charge.payType = 1;
			charge.addtime = new Date();
			charge.finish = 0;
			SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(charge);
		}else{
			if(po.finish==1){
				html="<html><head><meta charset='utf-8' /></head>订单已经完成</html>";
			}
		}
		return  Jsoup.parse(html);
	}
	
	 /**
     * 生成要请求给支付宝的参数数组
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
    private static Map<String, String> buildRequestPara(Map<String, String> sParaTemp) {
        //除去数组中的空值和签名参数
        Map<String, String> sPara = AlipayCore.paraFilter(sParaTemp);
        //生成签名结果
        String mysign = buildRequestMysign(sPara);

        //签名结果与签名方式加入请求提交参数组中
        sPara.put("sign", mysign);
        sPara.put("sign_type", sign_type);

        return sPara;
    }
    
    /**
     * 生成签名结果
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
	public static String buildRequestMysign(Map<String, String> sPara) {
    	String prestr = AlipayCore.createLinkString(sPara); //把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
        String mysign = "";
    	mysign = MD5.sign(prestr, key, input_charset);
        return mysign;
    }
}
