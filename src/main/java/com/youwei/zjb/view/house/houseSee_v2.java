package com.youwei.zjb.view.house;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.house.SellState;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.JSONHelper;


public class houseSee_v2 extends AbstractSee {

	@Override
	public Document initPage(Document doc, HttpServletRequest req) {
		return super.initPage(doc, req);
	}

	@Override
	protected JSONObject getData(int id) {
		
		CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
		House h = dao.get(House.class, id);
		if(h==null){
			return null;
		}
		JSONObject json = JSONHelper.toJSON(h);
		Department dept = dao.get(Department.class, h.did);
		User user = dao.get(User.class, h.uid);
		json.put("fbr", (user==null || user.uname==null) ? "":user.uname);
		json.put("ywy", h.forlxr==null ? "":h.forlxr);
		json.put("fortel", h.fortel==null ? "":h.fortel);
		json.put("dname", dept==null? "" : dept.namea);
		SellState ztai = SellState.parse(h.ztai);
		json.put("ztai", ztai==null ? "": ztai);
		String favStr = "@"+ThreadSession.getUser().id+"|";
		if(h.fav!=null && h.fav.contains(favStr)){
			json.put("fav", "1");
		}else{
			json.put("fav", "0");
		}
		return json;
	}

	@Override
	protected int getChuzu() {
		return 0;
	}

}
