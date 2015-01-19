package com.youwei.zjb.view.house;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.user.entity.User;

public class house_rent_add {

	public Document initPage(Document doc, HttpServletRequest req) {
		String html=doc.html();
		//业务员，默认为自己
		User u = ThreadSessionHelper.getUser();
		if(u.cid!=1){
			html = html.replace("$${forlxr}", u.uname==null ? "": u.uname);
			html = html.replace("$${fortel}", u.tel==null ? "" : u.tel);
		}else{
			html = html.replace("$${forlxr}","");
			html = html.replace("$${fortel}", "");
		}
		return Jsoup.parse(html);
	}
}
