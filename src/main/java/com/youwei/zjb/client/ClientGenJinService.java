package com.youwei.zjb.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.client.entity.Client;
import com.youwei.zjb.client.entity.ClientGenJin;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/client/gj")
public class ClientGenJinService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView list(ClientQuery query ,Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User u = ThreadSession.getUser();
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
		User u = ThreadSession.getUser();
		gj.uid = u.id;
		gj.did = u.did;
		gj.cid = u.cid;
		dao.saveOrUpdate(gj);
		return mv;
	}
}
