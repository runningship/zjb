package com.youwei.zjb.view.settings;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bc.sdak.utils.JSONHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.youwei.zjb.entity.Role;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;


public class uc_index_u_edit extends uc_index_u_add{

	@Override
	public Document initPage(Document doc, HttpServletRequest req) {
		String id = req.getParameter("id");
		User u = dao.get(User.class, Integer.valueOf(id));
		
		//分公司
		List<Department> depts = dao.listByParams(Department.class, "from Department where fid = ?", u.cid);
		
		Elements temp = doc.getElementsByClass("deptList");
		buildHtmlWithJsonArray(temp.first() , JSONHelper.toJSONArray(depts));
		temp.first().remove();
		
		List<Role> roles = dao.listByParams(Role.class, "from Role where cid=?", u.cid);
		Role r = new Role();
		r.title="请选择";
		roles.add(0,r);
		temp = doc.getElementsByClass("roleList");
		buildHtmlWithJsonArray(temp.first() , JSONHelper.toJSONArray(roles));
		temp.first().remove();
		
		String html = doc.html();
		html = html.replace("$${lname}", u.lname);
		return Jsoup.parse(html);
	}
	
}
