package com.youwei.zjb.view.settings;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.youwei.zjb.entity.Role;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.view.page;

public class uc_index_u_add extends page{
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public Document initPage(Document doc, HttpServletRequest req) {
		
		String id = req.getParameter("id");
		Department comp = null;
		if(StringUtils.isNotEmpty(id)){
			comp = dao.get(Department.class, Integer.valueOf(id));
		}
		String cnum="";
		Integer cid=null;
		if(comp.fid==0){
			cid = comp.id;
			cnum = comp.cnum;
		}else{
			cid = comp.fid;
			cnum = dao.get(Department.class, comp.fid).cnum;
		}
		int count=0;
		List<Map> list = dao.listAsMap("select max(lname) as maxLName from User where cid=?", cid);
		if(!list.isEmpty()){
			Object mlname = list.get(0).get("maxLName");
			if(mlname!=null && !"null".equals(mlname)){
				count = Integer.valueOf(mlname.toString());
			}
		}
		String pattern="0000000";
		String lname="";
		 java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
		 if(count==0){
			 lname=cnum+"0001";
		 }else{
			 lname=df.format(count+1);
		 }
		
		
		
		//分公司
		List<Department> depts = dao.listByParams(Department.class, "from Department where fid = ?", cid);
		
		Elements temp = doc.getElementsByClass("deptList");
		buildHtmlWithJsonArray(temp.first() , JSONHelper.toJSONArray(depts));
		temp.first().remove();
		
		List<Role> roles = dao.listByParams(Role.class, "from Role where cid=?", cid);
		Role r = new Role();
		r.title="请选择";
		roles.add(0,r);
		temp = doc.getElementsByClass("roleList");
		buildHtmlWithJsonArray(temp.first() , JSONHelper.toJSONArray(roles));
		temp.first().remove();
		
		String html = doc.html();
		html = html.replace("$${lname}", lname);
		return Jsoup.parse(html);
	}
}
