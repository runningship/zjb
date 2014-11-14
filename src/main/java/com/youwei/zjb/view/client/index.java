package com.youwei.zjb.view.client;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.User;

public class index {

	public Document initPage(Document doc , HttpServletRequest req){
//		User u = ThreadSession.getUser();
//		boolean auth = UserHelper.hasAuthority(u, "ky_edit");
//		String html = doc.html();
//		html = html.replace("$${auth}", String.valueOf(auth));
		return doc;
	}
}
