package com.youwei.zjb.house;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import sun.net.www.protocol.http.HttpURLConnection;

@Module(name="/phone/")
public class MobileSearchService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView getLocation(String tel){
		ModelAndView mv = new ModelAndView();
		String httpUrl = "http://apis.baidu.com/chazhao/mobilesearch/phonesearch";
		String httpArg = "phone="+tel;
		BufferedReader reader = null;
	    String result = null;
	    StringBuffer sbf = new StringBuffer();
	    httpUrl = httpUrl + "?" + httpArg;

	    try {
	        URL url = new URL(httpUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        // 填入apikey到HTTP header
	        connection.setRequestProperty("apikey",  "d77d10cd1abe511a78aed63c03ee24e8");
	        connection.connect();
	        InputStream is = connection.getInputStream();
	        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	        String strRead = null;
	        while ((strRead = reader.readLine()) != null) {
	            sbf.append(strRead);
	            sbf.append("\r\n");
	        }
	        reader.close();
	        result = sbf.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    mv.data.put("result", JSONObject.fromObject(result));
		return mv;
	}
	
	
	public static void main(String[] args){
		MobileSearchService mss = new MobileSearchService();
		mss.getLocation("15856985122");
	}
}