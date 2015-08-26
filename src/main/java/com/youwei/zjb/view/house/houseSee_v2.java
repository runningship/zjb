package com.youwei.zjb.view.house;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.utils.JSONHelper;
import org.jsoup.nodes.Document;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.house.SellState;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.user.entity.ViewHouseLog;


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
		json.put("city", ThreadSessionHelper.getCityPinyin());
		Department dept = dao.get(Department.class, h.did);
		User user = dao.get(User.class, h.uid);
		json.put("fbr", (user==null || user.uname==null) ? "":user.uname);
		json.put("ywy", h.forlxr==null ? "":h.forlxr);
		json.put("fortel", h.fortel==null ? "":h.fortel);
		json.put("dname", dept==null? "房主" : dept.namea);
		SellState ztai = SellState.parse(h.ztai);
		json.put("ztai", ztai==null ? "": ztai);
		String favStr = "@"+ThreadSessionHelper.getUser().id+"|";
		if(h.fav!=null && h.fav.contains(favStr)){
			json.put("fav", "1");
		}else{
			json.put("fav", "0");
		}
		ViewHouseLog vl = new ViewHouseLog();
		vl.hid = id;
		vl.uid = ThreadSessionHelper.getUser().id;
		vl.isMobile = 0;
		vl.viewTime = new Date();
		dao.saveOrUpdate(vl);
		return json;
	}

	@Override
	protected int getChuzu() {
		return 0;
	}

}
