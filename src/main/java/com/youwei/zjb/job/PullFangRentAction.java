package com.youwei.zjb.job;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.util.DataHelper;

public class PullFangRentAction implements PullRentHouseAction{

	@Override
	public String getArea(Element elem) {
		Elements ar = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("小 区：");
		String area = ar.first().nextElementSibling().text();
		return area.trim();
	}

	private Element getElementsByMatchingText(Element elem,String text){
		for(Element e : elem.getElementsMatchingOwnText(text)){
			if(text.equals(e.text())){
				return e;
			}
		}
		return null;
	}

	@Override
	public String getMji(Element elem) {
		Elements mj = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("面 积：");
		String text = mj.first().nextElementSibling().text();
		return text.replace("平米", "").trim();
	}

	@Override
	public String getZujin(Element elem) {
		Elements zj = elem.getElementsByClass("num");
		Element zjin = zj.first();
		return zjin.text().trim();
	}

	@Override
	public String getLceng(Element elem) {
		Elements mj = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("楼 层：");
		if(mj.isEmpty()){
			return "";
		}
		String text = mj.first().nextElementSibling().text();
		return text.replace("层", "").split("/")[0].trim();
	}

	@Override
	public String getZceng(Element elem) {
		Elements mj = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("楼 层：");
		if(mj.isEmpty()){
			return "";
		}
		String text = mj.first().nextElementSibling().text();
		if(text.contains("/")){
			return text.replace("层", "").split("/")[1].trim();
		}else {
			return "";
		}
	}

	@Override
	public String getLxing(Element elem) {
		return "";
	}

	@Override
	public String getHxing(Element elem) {
		Elements hx = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("户 型：");
		String text = "";
		if(hx.isEmpty()){
			hx = elem.getElementsMatchingOwnText("房屋概况：");
			text = PullDataHelper.getHxingByText(hx.text().trim());
		}else{
			text = hx.first().nextElementSibling().text();
		}
		return text.replace("室", "房").trim();
	}

	@Override
	public String getZxiu(Element elem) {
		Elements zx = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("装 修：");
		if(zx.isEmpty()){
			return "";
		}
		String text = zx.first().nextElementSibling().text();
		if(text.contains("简装修")){
			return "简装";
		}else if(text.contains("中等装修")){
			return "中装";
		}else if(text.contains("精装修")){
			return "精装";
		}else if(text.contains("豪华装修")){
			return "豪装";
		}else{
			return "毛坯";
		}
	}

	@Override
	public String getYear(Element elem) {
		return "";
	}

	@Override
	public String getQuyu(Element elem) {
		String quyu = elem.getElementById("gerenzfxq_B04_03").text();
		if(quyu.isEmpty()){
			return "";
		}
		String text = quyu+"县";
		String text2 = quyu+"区";
		if(quyu.contains("巢湖")){
			return "巢湖市";
		}else if(quyu.contains("经济")){
			return "经开区";
		}else if(quyu.contains("高新")){
			return "高新区";
		}else if(!quyu.contains("区")){
			if(!quyu.contains("肥东")&&!quyu.contains("肥西")&&!quyu.contains("长丰")&&!quyu.contains("庐江")){
				return text2.trim();
			}
			return text.trim();
		}
		return quyu.trim();
	}

	@Override
	public String getAddress(Element elem) {
		return "";
	}

	@Override
	public String getLxr(Element elem) {
		Element lx = elem.getElementById("Span2");
		return lx.text().trim();
	}

	@Override
	public void getTel(HouseRent house , Element elem) {
		String phone = elem.getElementById("agtphone").child(0).text();
		house.tel = phone.trim();
	}

	@Override
	public String getWo(Element elem) {
		Elements wo = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("出租间：");
		if(wo.isEmpty()){
			return "";
		}
		String text = wo.first().nextElementSibling().ownText();
		return text.trim();
	}

	@Override
	public String getXianZhi(Element elem) {
		Elements hx = elem.getElementsByClass("baseInfo").first().getElementsMatchingOwnText("合租条件：");
		if(hx.isEmpty()){
			return "";
		}
		String text = hx.first().nextElementSibling().ownText();
		return text.trim();
	}

	@Override
	public String getPeiZhi(Element elem) {
		Elements pz = elem.getElementsMatchingOwnText("配套设施：");
		if(pz.isEmpty()){
			return "";
		}
		String peizhi = pz.first().text().replace("配套设施：", "");
		return peizhi.trim();
	}

	@Override
	public String getTitle(Element doc) {
		Element title = doc.getElementsByClass("houseInfo").first().child(0).child(0).child(0);
		return title.text().trim();
	}

	@Override
	public Element getDetailSumary(String url) throws IOException {
		CloseableHttpClient client = HttpClientBuilder.create().build(); 
		RequestConfig requestConfig = RequestConfig.custom()  
	    .setConnectionRequestTimeout(10000).setConnectTimeout(10000)
	    .setSocketTimeout(10000).build();
	    HttpGet request = new HttpGet(url);
	    request.setConfig(requestConfig);
		CloseableHttpResponse response = client.execute(request);
		String result = EntityUtils.toString(response.getEntity() , "gb2312");
		response.close();
		client.close();
		Document doc = Jsoup.parse(result);
		if(doc.getElementsMatchingOwnText("您的访问速度太快了").isEmpty()==false){
			throw new TooFastException("扫网速度太快了");
		}else if(!doc.getElementsByClass("guoqi-box").isEmpty()){
			return null;
		}
		Element su = doc.getElementsByClass("houseInfo").first();
		Element sumary = su.parent();
		return sumary;
	}

	@Override
	public Date getPubTime(Element elem) {
		Element time = elem.getElementsByClass("houseInfo").first();
		String times = time.child(0).child(0).child(1).text().trim();
		String text = times.split("\\(")[1];
		if(text.contains("天前")){
			text = text.split("天前")[0];
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.DAY_OF_MONTH, 0-Integer.valueOf(text));
			return ca.getTime();
		}else if(text.contains("分钟前")){
			text = text.split("分钟前")[0];
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.MINUTE, 0-Integer.valueOf(text));
			return ca.getTime();
		}else if(text.contains("小时前")){
			text = text.split("小时前")[0];
			Calendar ca = Calendar.getInstance();
			ca.add(Calendar.SECOND, 0-Integer.valueOf(text));
			return ca.getTime();
		}else {
			return new Date();
		}
	}

	@Override
	public String getSiteName() {
		return "fang";
	}

	@Override
	public String getBeizhu(Element elem) {
		Element intr = elem.select(".Introduce").first();
		if(intr!=null && intr.children().size()>0){
			return intr.child(0).text();
		}
		return "";
	}
	
}
