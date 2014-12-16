package com.youwei.zjb.view.swq;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;

public class index_58_wuhu {

	public Document initPage(Document doc , HttpServletRequest req){
		String html = doc.html();
		boolean swq = (Boolean)ThreadSession.getHttpSession().getAttribute("swq");
		if(swq){
			html = html.replace("$${pass}", "1");
		}
		return Jsoup.parse(html);
	}
}
