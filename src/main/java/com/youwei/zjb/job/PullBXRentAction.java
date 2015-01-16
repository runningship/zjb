package com.youwei.zjb.job;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.util.DataHelper;

public class PullBXRentAction implements PullRentHouseAction{

	@Override
	public String getArea(Element elem) {
		Elements ar = elem.getElementsMatchingOwnText("小区名：");
		if(ar.isEmpty()){
			return "";
		}
		String area = ar.first().child(0).text();
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
		Elements mj = elem.getElementsMatchingOwnText("面积：");
		if(mj.isEmpty()){
			return "";
		}
		String text = mj.first().text();
		return text.replace("面积：", "").replace("平米", "").trim();
	}

	@Override
	public String getZujin(Element elem) {
		Elements zj = elem.getElementsMatchingOwnText("租金：");
		String zjin = zj.first().text();
		return zjin.replace("租金：", "").replace("元", "").trim();
	}

	@Override
	public String getLceng(Element elem) {
		Elements lc = elem.getElementsMatchingOwnText("楼层：");
		if(lc.isEmpty()){
			return "";
		}
		String text = lc.first().text().replace("楼层：", "").replace("层", "");
		return text.split("/")[0];
	}

	@Override
	public String getZceng(Element elem) {
		Elements lc = elem.getElementsMatchingOwnText("楼层：");
		if(lc.isEmpty()){
			return "";
		}
		String text = lc.first().text().replace("楼层：", "").replace("层", "");
		if(text.contains("/")){
			return text.split("/")[1];
		}
		return "0";
	}

	@Override
	public String getLxing(Element elem) {
		return "";
	}

	@Override
	public String getHxing(Element elem) {
		Elements hx = elem.getElementsMatchingOwnText("房型：");
		if(hx.isEmpty()){
			return "";
		}
		String text = hx.first().text();
		String hxing = text.replace("房型：", "").replace("室", "房").replace("4室及以上", "4房");
		return hxing.trim();
	}

	@Override
	public String getZxiu(Element elem) {
		Elements zx = elem.getElementsMatchingOwnText("装修情况：");
		if(zx.isEmpty()){
			return "";
		}
		String text = zx.first().text();
			if(text.contains("简单装修")){
				return "简装";
			}else if(text.contains("中等装修")){
				return  "中装";
			}else if(text.contains("精装修")){
				return "精装";
			}else if(text.contains("豪华装修")){
				return "豪装";
			}else if(text.contains("毛坯")){
				return "毛坯";
			}
		return "";
	}

	@Override
	public String getYear(Element elem) {
		return "";
	}

	@Override
	public String getQuyu(Element elem) {
		Element qy = elem.getElementsMatchingOwnText("地区：").first();
		String qyu = qy.text().replace("地区：", "").trim();
		if(qyu.isEmpty()){
			return "";
		}else {
			String quyu = qyu.split("-")[0].trim();
			if(quyu.isEmpty()){
				return "";
			}
			String text = quyu.substring(0, 2)+"区";
			if(quyu.isEmpty()){
				return "";
			}else if(quyu.contains("庐江")||quyu.contains("肥东")||quyu.contains("肥西")||quyu.contains("长丰")){
				String q = quyu.substring(0, 2)+"县";
				return q.trim();
			}else if(quyu.contains("瑶海")||quyu.contains("庐阳")||quyu.contains("蜀山")||quyu.contains("包河")){
				String q = quyu.substring(0, 2)+"区";
				return q.trim();
			}else if(quyu.contains("滨湖新区")){
				return "滨湖新区";
			}else if(quyu.contains("经济")){
				return "经开区";
			}else{
				return text.trim();
			}
		}
	}

	@Override
	public String getAddress(Element elem) {
		return "";
	}

	@Override
	public String getLxr(Element elem) {
		return "";
	}

	@Override
	public void getTel(HouseRent house , Element elem) {
		house.tel = "";
	}

	@Override
	public String getWo(Element elem) {
		return "";
	}

	@Override
	public String getXianZhi(Element elem) {
		return "";
	}

	@Override
	public String getPeiZhi(Element elem) {
		Elements pz = elem.getElementsMatchingOwnText("房屋配置：");
		if(pz.isEmpty()){
			return "";
		}
		String peizhi = pz.first().text();
		return peizhi.replace("房屋配置：", "").trim();
	}

	@Override
	public String getTitle(Element doc) {
		return doc.getElementsByClass("viewad-title").text();
	}

	@Override
	public Element getDetailSumary(String url) throws IOException {
		URL url1 = new URL(url);
		URLConnection conn = url1.openConnection();
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		String result = IOUtils.toString(conn.getInputStream(),"utf-8");
		Document doc = Jsoup.parse(result);
		if(doc.getElementsMatchingOwnText("您的访问速度太快了").isEmpty()==false){
			throw new TooFastException("扫网速度太快了");
		}
		Element sumary = doc.getElementsByClass("viewad-title").first().parent().parent();
		return sumary;
	}

	@Override
	public Date getPubTime(Element elem) {
		Element time = elem.getElementsByClass("viewad-actions").first();
		String Time = time.child(1).text();
		String text = Time.replace("月", "-").replace("日", "-").trim();
		text ="2015-"+text;
		try {
			return DataHelper.sdf3.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	@Override
	public String getSiteName() {
		return "baixing";
	}

}
