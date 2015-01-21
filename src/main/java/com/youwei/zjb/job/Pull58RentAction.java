package com.youwei.zjb.job;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.house.ZhuangXiu;
import com.youwei.zjb.house.entity.HouseRent;

public class Pull58RentAction implements PullRentHouseAction{

	@Override
	public String getArea(Element elem) {
		String[] arr = getQuyuText(elem).split("-");
		if(arr.length<=1){
			return "";
		}
		return arr[arr.length-1];
	}

	
	
	@Override
	public String getMji(Element elem) {
		String text = getChuzuText(elem);
		for(String str: text.split(" ")){
			if(str.contains("㎡")){
				return str.replace("㎡", "");
			}
		}
		return "";
	}

	@Override
	public String getZujin(Element elem) {
		Elements prices = elem.getElementsByClass("bigpri");
		if(prices==null || prices.isEmpty()){
			return "";
		}
		return prices.first().text();
	}
	
	

	@Override
	public String getLceng(Element elem) {
		String text = getLouCengText(elem);
		String[] arr = text.split("/");
		return arr[0].replace("层", "");
	}

	@Override
	public String getLxing(Element elem) {
		//人工判断
		return "";
	}

	@Override
	public String getHxing(Element elem) {
		String text = getGuaiKuangText(elem);
		if(StringUtils.isEmpty(text)){
			return "";
		}
		String str = "";
		if(text.indexOf("室")>0){
			int pos = text.indexOf("室");
			str+=text.substring(pos-1, pos)+"房";
		}
		if(text.indexOf("厅")>0){
			int pos = text.indexOf("厅");
			str+=text.substring(pos-1, pos)+"厅";
		}
		if(text.indexOf("卫")>0){
			int pos = text.indexOf("卫");
			str+=text.substring(pos-1, pos)+"卫";
		}
		return str;
	}

	@Override
	public String getZxiu(Element elem) {
		String text = this.getGuaiKuangText(elem);
		
		if(StringUtils.isEmpty(text)){
			return "sd";
		}
		if(text.contains("精装修")){
			return ZhuangXiu.精装.name();
		}
		if(text.contains("中等装修")){
			return ZhuangXiu.中装.name();
		}
		if(text.contains("简单装修")){
			return ZhuangXiu.简装.name();
		}
		if(text.contains("毛坯")){
			return ZhuangXiu.毛坯.name();
		}
		if(text.contains("豪华装修")){
			return ZhuangXiu.豪装.name();
		}
		return null;
	}

	@Override
	public String getYear(Element elem) {
		return "";
	}

	@Override
	public String getQuyu(Element elem) {
		String text = getQuyuText(elem);
		String[] arr = text.split("-");
		String quyu = arr[0].trim()+"区";
		if(arr.length>=1){
			if(text.contains("合肥周边")){
				return "周边市区";
			}else if(quyu.contains("滨湖")){
				return "滨湖新区";
			}
			return quyu.trim();
		}
		return "";
	}

	@Override
	public String getAddress(Element elem) {
		Element addr = getElementsByMatchingText(elem,"地址");
		if(addr==null){
			return "";
		}
		String text = addr.nextElementSibling().text();
		return text.split("\\(")[0];
	}

	@Override
	public String getLxr(Element elem) {
		Element lxr = getElementsByMatchingText(elem,"联系");
		if(lxr==null ){
			return "";
		}
		return lxr.nextElementSibling().child(0).text();
	}

	@Override
	public void getTel(HouseRent house ,Element elem) {
		Element phone = elem.getElementById("t_phone");
		Elements img = phone.getElementsByTag("script");
		if(img!=null && img.first()!=null){
			String str = img.first().html();
			String[] arr = str.split("src=");
			if(arr.length>1){
				str = arr[1];
				arr = str.split("'");
				if(arr.length>2){
					str = arr[1];
					house.telImg = str;
				}
			}
		}
	}

	@Override
	public String getZceng(Element elem) {
		String text = getLouCengText(elem);
		String[] arr = text.split("/");
		if(arr.length>=2){
			return arr[1].replace("层", "");
		}else{
			return arr[0].replace("层", "");
		}
	}

	private String getGuaiKuangText(Element elem){
		Element tmp = getElementsByMatchingText(elem,"概况");
		if(tmp==null){
			tmp = getElementsByMatchingText(elem,"整体");
		}
		if(tmp==null){
			return "";
		}
		Element next = tmp.nextElementSibling();
		if(next==null){
			return "";
		}
		return next.text();
	}
	private String getQuyuText(Element elem){
		Element quyu = getElementsByMatchingText(elem,"区域");
		if(quyu==null){
			return "";
		}
		Element next = quyu.nextElementSibling();
		if(next==null){
			return "";
		}
		next.getElementsByTag("span").remove();
		if(StringUtils.isEmpty(next.text())){
			return "";
		}
		return next.text();
	}
	
	private String getLouCengText(Element elem){
		Element tmp = getElementsByMatchingText(elem,"楼层");
		if(tmp==null){
			return "";
		}
		return tmp.nextElementSibling().text();
	}

	private Element getElementsByMatchingText(Element elem,String text){
		for(Element e : elem.getElementsMatchingOwnText(text)){
			if(text.equals(e.text())){
				return e;
			}
		}
		return null;
	}
	
	private String getChuzuText(Element elem){
		Element tmp = getElementsByMatchingText(elem,"出租");
		if(tmp==null){
			tmp = getElementsByMatchingText(elem,"概况");
		}
		if(tmp==null){
			return "";
		}
		return tmp.nextElementSibling().text();
	}

	@Override
	public String getWo(Element elem) {
		String text = getChuzuText(elem);
		for(String str : text.split(" ")){
			if(str.contains("卧")){
				return str;
			}
		}
		return "";
	}

	@Override
	public String getXianZhi(Element elem) {
		String text = getChuzuText(elem);
		for(String str : text.split(" ")){
			if(str.contains("限")){
				return str;
			}
		}
		return "";
	}

	@Override
	public String getPeiZhi(Element elem) {
		Element peizhi = elem.getElementsByClass("peizhi").first();
		if(peizhi==null){
			return "";
		}
		String script = peizhi.getElementsByTag("script").toString();
		script = script.replace("\r\n", "").replace("\t", "");
		script = script.split(" document.write")[0];
		String[] arr = script.split(" tmp =");
		if(arr.length>1){
			return arr[1];
		}
		return "";
	}



	@Override
	public String getTitle(Element elem) {
		return elem.getElementsByClass("bigtitle").text();
	}



	@Override
	public Element getDetailSumary(String hlink) throws IOException {
		URL url = new URL(hlink);
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		String result = IOUtils.toString(conn.getInputStream(),"utf-8");
		Document doc = Jsoup.parse(result);
		if(doc.getElementsMatchingOwnText("页面可能被删除").isEmpty()==false){
			return null;
		}
		Element sumary = doc.getElementsByClass("col_sub sumary").first();
		if(sumary==null){
			sumary = doc.getElementsByClass("detailPrimary").first();
		}
		return sumary;
	}



	@Override
	public Date getPubTime(Element elem) {
		return new Date();
	}



	@Override
	public String getSiteName() {
		return "58";
	}



	@Override
	public String getBeizhu(Element elem) {
		return "";
	}
}
