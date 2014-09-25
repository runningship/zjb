package com.youwei.zjb.view;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;


public class home {
	public Document initPage(Document doc , HttpServletRequest req){
		User u = ThreadSession.getUser();
		Department dept = u.Department();
		Department comp = dept.Company();
		String html = doc.html();
		html = html.replace("$${uname}", u.uname==null ? "":u.uname);
		html = html.replace("$${lname}", u.lname);
		html = html.replace("$${dname}", dept.namea);
		html = html.replace("$${cname}", comp.namea);
		Role role = u.getRole();
		html = html.replace("$${role}", role==null?"":role.title);
		html = html.replace("$${tel}", u.tel==null? "" : u.tel);
		
		html = html.replace("$${version}", "5.0.925");
		return Jsoup.parse(html);
	}
}
