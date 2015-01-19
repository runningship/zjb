package com.youwei.zjb.view.house;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;

public class house {

	public Document initPage(Document doc, HttpServletRequest req) {
		//业务员，默认为自己
		User u = ThreadSessionHelper.getUser();
		Department comp = u.Company();
		if(comp==null || comp.share!=1){
			doc.getElementById("seeAll").remove();
			doc.getElementById("seeGX").remove();
			doc.getElementById("seeComp").getElementsByTag("input").attr("checked", "checked");
		}
		String html=doc.html();
		if(UserHelper.hasAuthority(u, "fy_sh")){
			html = html.replace("$${sh}", "");
		}else{
			//没有审核权的用户只能看到审核通过对数据
			html = html.replace("$${sh}", "1");
		}
		return Jsoup.parse(html);
	}
}
