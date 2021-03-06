package com.youwei.zjb.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.client.entity.ClientGenJin;
import com.youwei.zjb.user.entity.User;

@Module(name="/client/gj")
public class ClientGenJinService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView list(ClientQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User u = ThreadSessionHelper.getUser();
		StringBuilder hql = new StringBuilder("select d.namea as dname, u.uname as uname, gj.conts as conts,gj.addtime as addtime from User u, "
				+ "Department d, ClientGenJin gj where u.did=d.id and u.id=gj.uid and gj.clientId=?");
		params.add(query.clientId);
		page.orderBy = "addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(ClientGenJin gj){
		ModelAndView mv = new ModelAndView();
		gj.addtime = new Date();
		User u = ThreadSessionHelper.getUser();
		gj.uid = u.id;
		gj.did = u.did;
		gj.cid = u.cid;
		dao.saveOrUpdate(gj);
		return mv;
	}
}
