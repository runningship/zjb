package com.youwei.zjb.view.settings;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.DeptGroup;
import com.youwei.zjb.view.page;

public class uc_set_group extends page{
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public Document initPage(Document doc, HttpServletRequest req) {
		
		String did = req.getParameter("did");
		Department dept = dao.get(Department.class, Integer.valueOf(did));
		int cid = dept.Company().id;
		if(dept.dgroup==null){
			//方便统一查询
			dept.dgroup=-1;
		}
		//分公司
		List<DeptGroup> depts = dao.listByParams(DeptGroup.class, "from DeptGroup where cid = ? and id<>?", cid,dept.dgroup);
		if(depts.isEmpty()){
			DeptGroup empty = new DeptGroup();
			empty.name="没找到";
			depts.add(empty);
		}
		Elements temp = doc.getElementsByClass("deptList");
		buildHtmlWithJsonArray(temp.first() , JSONHelper.toJSONArray(depts));
		temp.first().remove();
		
		String html = doc.html();
		html = html.replace("$${did}", String.valueOf(dept.id));
		return Jsoup.parse(html);
	}
}
