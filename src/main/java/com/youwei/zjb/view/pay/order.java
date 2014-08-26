package com.youwei.zjb.view.pay;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class order {

	public static final String partner = "2088801295914922";
	public final static String key="5itxn2rr0fkwxbixlasbu7wco0ngzgpy";
	public final static String input_charset = "utf-8";
	static final String payment_type = "1";
	static final String sign_type="MD5";
	public Document initPage(Document doc , HttpServletRequest req){
		String html = doc.html();
		html = html.replace("$${trade_no}", String.valueOf(System.currentTimeMillis()));
		return  Jsoup.parse(html);
	}
}
