package com.youwei.zjb.view;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;


public class start {
	public Document initPage(Document doc , HttpServletRequest req){
		String html = doc.html();
		String domainName = ConfigCache.get("domainName", "192.168.1.125");
		html = html.replace("$${domainName}", domainName);
		return Jsoup.parse(html);
	}
}
