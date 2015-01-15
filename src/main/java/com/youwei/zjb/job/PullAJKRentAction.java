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

public class PullAJKRentAction implements PullRentHouseAction{

	@Override
	public String getArea(Element elem) {
		Elements ar = elem.getElementsByClass("cinfo");
		String area = ar.first().child(1).child(1).child(0).child(0).child(1).child(0).text();
		return area.split("（")[0].trim();
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
		Elements mj = elem.getElementsByClass("pinfo");
		String text = mj.first().child(2).child(0).child(1).child(1).child(1).text();
		return text.replace("平米", "");
	}

	@Override
	public String getZujin(Element elem) {
		Elements zj = elem.getElementsByClass("pinfo");
		String zjin = zj.first().child(2).child(0).child(0).child(0).child(1).text();
		return zjin.replace("元/月", "").trim();
	}

	@Override
	public String getLceng(Element elem) {
		Elements lc = elem.getElementsByClass("pinfo");
		String text = lc.first().child(2).child(0).child(1).child(3).child(1).ownText();
		return text.split("/")[0];
	}

	@Override
	public String getZceng(Element elem) {
		Elements lc = elem.getElementsByClass("pinfo");
		String text = lc.first().child(2).child(0).child(1).child(3).child(1).ownText();
		return text.split("/")[1];
	}

	@Override
	public String getLxing(Element elem) {
		return "";
	}

	@Override
	public String getHxing(Element elem) {
		Elements hx = elem.getElementsByClass("pinfo");
		String text = hx.first().child(2).child(0).child(0).child(2).child(1).text();
		String hxing = text.replace("室", "房");
		return hxing.trim();
	}

	@Override
	public String getZxiu(Element elem) {
		Element zx = elem.getElementsByClass("pinfo").first();
		String text = zx.child(2).child(0).child(1).child(0).child(1).ownText();
			if(text.contains("普通装修")){
				return text.replace("普通装修", "简装").trim();
			}else if(text.contains("精装修")){
				return text.replace("精装修", "精装").trim();
			}else if(text.contains("豪华装修")){
				return text.replace("豪华装修", "豪装").trim();
			}else if(text.contains("毛坯")){
				return text.trim();
			}
		return "";
	}

	@Override
	public String getYear(Element elem) {
		Element y = elem.getElementsByClass("cinfo").first();
		String year = y.child(1).child(1).child(1).child(2).child(1).ownText();
		if(year.contains("暂无")){
			return "";
		}else {
			return year.split("-")[0].trim();
		}
	}

	@Override
	public String getQuyu(Element elem) {
		Element qy = elem.getElementsByClass("cinfo").first();
		String quyu = qy.child(1).child(1).child(0).child(1).child(1).text();
		String text = quyu.split(" ")[0]+"区";
		if(quyu.contains("滨湖")){
			return "滨湖新区";
		}else if(quyu.contains("北城")){
			return "北城新区";
		}else if(quyu.contains("其他")){
			return quyu.split(" ")[1].trim();
		}else{
			return text.trim();
		}
	}

	@Override
	public String getAddress(Element elem) {
		Element add = elem.getElementsByClass("cinfo").first();
		String address = add.child(1).child(1).child(0).child(2).child(1).ownText();
		return address.trim();
	}

	@Override
	public String getLxr(Element elem) {
		String lxr = elem.getElementById("broker_true_name").text();
		return lxr.trim();
	}

	@Override
	public void getTel(HouseRent house , Element elem) {
		Element phone = elem.getElementsByClass("icon_tel").first().parent();
		house.tel = phone.ownText().replace(" ", "").trim();
	}

	@Override
	public String getWo(Element elem) {
		return "";
	}

	@Override
	public String getXianZhi(Element elem) {
		Element hx = elem.getElementsByClass("pro_detail").first();
		String text = hx.child(1).child(0).ownText();
		if(text.contains("限")){
			return text.trim();
		}
		return "";
	}

	@Override
	public String getPeiZhi(Element elem) {
		Elements pz = elem.getElementsMatchingOwnText("配置：");
		if(pz.isEmpty()){
			return "";
		}
		String peizhi = pz.first().text();
		return peizhi.replace("配置：", "").trim();
	}

	@Override
	public String getTitle(Element doc) {
		return doc.getElementsByClass("tit").text();
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
		Element sumary = doc.getElementsByClass("tit").first().parent();
		return sumary;
	}

	@Override
	public Date getPubTime(Element elem) {
		Element time = elem.getElementsByClass("pinfo").first();
		String Time = time.child(2).child(1).child(3).text();
		String text = Time.split("发布时间：")[1].replace("年", "-").replace("月", "-").replace("日", "-");
		try {
			return DataHelper.sdf3.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	@Override
	public String getSiteName() {
		return "anjuke";
	}

}
