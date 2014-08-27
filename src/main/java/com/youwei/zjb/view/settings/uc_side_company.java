package com.youwei.zjb.view.settings;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class uc_side_company {
	public Document initPage(Document doc, HttpServletRequest req) {
		//只有中介宝才能添加公司
		Element elem = doc.getElementById("add_company");
		if(elem!=null){
			elem.remove();
		}
		return doc;
	}
}
