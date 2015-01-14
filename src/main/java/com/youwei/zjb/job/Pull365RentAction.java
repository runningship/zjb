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

public class Pull365RentAction implements PullRentHouseAction{

	@Override
	public String getArea(Element elem) {
		Element ar = elem.getElementsMatchingOwnText("小区：").first();
		String area = ar.nextElementSibling().text();
		return area.split(" ")[0].trim();
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
		Element mj = elem.getElementsMatchingOwnText("面积：").first();
		String text = mj.nextElementSibling().ownText();
			if(text.contains("㎡")){
				return text.replace("㎡", "").trim();
			}
		return "";
	}

	@Override
	public String getZujin(Element elem) {
		Elements zj = elem.getElementsMatchingOwnText("价格：");
		Element zjin = zj.first().nextElementSibling().child(0);
		return zjin.text().trim();
	}

	@Override
	public String getLceng(Element elem) {
		Element lc = elem.getElementsMatchingOwnText("楼层：").first();
		String text = lc.nextElementSibling().text();
		return text.split("/")[0];
	}

	@Override
	public String getZceng(Element elem) {
		Element lc = elem.getElementsMatchingOwnText("楼层：").first();
		String text = lc.nextElementSibling().text();
		return text.split("/")[1];
	}

	@Override
	public String getLxing(Element elem) {
		return "";
	}

	@Override
	public String getHxing(Element elem) {
		Elements hx = elem.getElementsMatchingOwnText("户型：");
		if(hx.isEmpty()){
			return "";
		}
		String text = hx.first().nextElementSibling().text();
		String hxing = text.replace("室", "房").replace(" ", "");
		return hxing.trim();
	}

	@Override
	public String getZxiu(Element elem) {
		Element zx = elem.getElementsMatchingOwnText("装修：").first();
		String text = zx.nextElementSibling().text();
		if(text.contains("豪华装")){
			return text.replace("豪华装", "豪装").trim();
		}
		return text.trim();
	}

	@Override
	public String getYear(Element elem) {
		return "";
	}

	@Override
	public String getQuyu(Element elem) {
		Element qy = elem.getElementsMatchingOwnText("区域：").first();
		Element quyu = qy.nextElementSibling().child(0);
		return quyu.text().trim();
	}

	@Override
	public String getAddress(Element elem) {
		return "";
//		Element add = elem.getElementsMatchingOwnText("位置：").first();
//		String address = add.parent().nextElementSibling().child(1).text();
//		return address.split("\\(")[0].trim();
	}

	@Override
	public String getLxr(Element elem) {
		Element lx = elem.getElementById("personal");
		Element lxr = lx.child(1);
		return lxr.text().trim();
	}

	@Override
	public void getTel(HouseRent house , Element elem) {
		String phone = elem.getElementsByClass("fd_telephone").text();
		house.tel = phone.trim();
	}

	@Override
	public String getWo(Element elem) {
		return "";
	}

	@Override
	public String getXianZhi(Element elem) {
		Elements hx = elem.getElementsMatchingOwnText("面积：");
		String text = hx.first().nextElementSibling().child(0).text();
		for(String str : text.split("，")){
			if(str.contains("限")){
				return str.trim();
			}
		}
		return "";
	}

	@Override
	public String getPeiZhi(Element elem) {
		Elements not = elem.getElementsByClass("facility").select("dd").select(".not");
		not.remove();
		String text = elem.getElementsByClass("facility").text();
		return text;
	}

	@Override
	public String getTitle(Element doc) {
		return doc.getElementsByClass("titleT").first().child(0).text();
	}

	@Override
	public Element getDetailSumary(String url) throws IOException {
		URL url1 = new URL(url);
		URLConnection conn = url1.openConnection();
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		String result = IOUtils.toString(conn.getInputStream(),"GBK");
//		System.out.print(result);
		Document doc = Jsoup.parse(result);
		if(doc.getElementsMatchingOwnText("页面可能被删除").isEmpty()==false){
			return null;
		}
		Element sumary = doc.getElementsByClass("houseInformation").first().parent();
		return sumary;
	}

	@Override
	public Date getPubTime(Element elem) {
		return new Date();
	}

	@Override
	public String getSiteName() {
		return "365";
	}

}
