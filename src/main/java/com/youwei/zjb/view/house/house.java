package com.youwei.zjb.view.house;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.User;

public class house {

	public Document initPage(Document doc, HttpServletRequest req) {
		String html=doc.html();
		//业务员，默认为自己
		User u = ThreadSession.getUser();
		if(UserHelper.hasAuthority(u, "fy_sh")){
			html = html.replace("$${sh}", "");
		}else{
			//没有审核权的用户只能看到审核通过对数据
			html = html.replace("$${sh}", "1");
		}
		
		return Jsoup.parse(html);
	}
}
