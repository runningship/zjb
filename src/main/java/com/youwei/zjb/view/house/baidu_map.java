package com.youwei.zjb.view.house;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;

public class baidu_map{
	
	public Document initPage(Document doc, HttpServletRequest req) {
		String html = doc.toString();
		html = html.replace("$${city}", ThreadSession.getCity());
		return Jsoup.parse(html);
	}
}
