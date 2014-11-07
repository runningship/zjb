package com.youwei.zjb.view.house;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.entity.Role;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;

public class house_v2 {

	public Document initPage(Document doc, HttpServletRequest req) {
		//业务员，默认为自己
		User u = ThreadSession.getUser();
		Department comp = u.Company();
		if(comp==null || comp.share!=1){
			try{
				doc.getElementById("seeAll").remove();
				doc.getElementById("seeGX").remove();
				doc.getElementById("seeComp").getElementsByTag("input").attr("checked", "checked");
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		String html=doc.html();
		if(UserHelper.hasAuthority(u, "fy_sh")){
			html = html.replace("$${sh}", "");
		}else{
			//没有审核权的用户只能看到审核通过对数据
			html = html.replace("$${sh}", "1");
		}
		if(u.cid!=1){
			List<Role> role = SimpDaoTool.getGlobalCommonDaoService().listByParams(Role.class, "from Role where title='管理员' and cid<>1 and cid=?", u.cid);
			if(role!=null && role.size()>0){
				html = html.replace("$${seeAds}", "0");
			}else{
				html = html.replace("$${seeAds}", "1");
			}
		}else{
			html = html.replace("$${seeAds}", "1");
		}
		return Jsoup.parse(html);
	}
}
