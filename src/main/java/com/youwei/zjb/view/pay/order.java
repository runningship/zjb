package com.youwei.zjb.view.pay;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;

public class order {

//	public static final String partner = "2088801295914922";
//	public final static String key="5itxn2rr0fkwxbixlasbu7wco0ngzgpy";
//	public final static String input_charset = "utf-8";
//	static final String payment_type = "1";
//	static final String sign_type="MD5";
	public Document initPage(Document doc , HttpServletRequest req){
		String html = doc.html();
		User u = ThreadSession.getUser();
		Department d = u.Department();
		Department c = d.Company();
		String cname=c.namea==null?"":c.namea;
		String dname=d.namea==null?"":d.namea;
		String uname=u.uname==null?"":u.uname;
		html = html.replace("$${trade_no}", String.valueOf(System.currentTimeMillis()));
		html = html.replace("$${domainName}", ConfigCache.get("domainName", "localhost"));
		html = html.replace("$${user}", cname+dname+uname);
		html = html.replace("$${uid}", u.id.toString());
		return  Jsoup.parse(html);
	}
}
