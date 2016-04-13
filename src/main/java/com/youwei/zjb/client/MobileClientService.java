package com.youwei.zjb.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.client.entity.Client;
import com.youwei.zjb.util.DataHelper;

@Module(name="/mobile/client")
public class MobileClientService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView list(Integer uid , String tel , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select c.name as name,c.tels as tels "
				+ "from Client c, User u where c.uid=u.id and u.tel=? ");
		params.add(tel);
		page.orderBy = "c.addtime";
		page.order = Page.DESC;
		page.pageSize=25;
		page = dao.findPage(page, hql.toString(), true,params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.dateSdf.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView getClient(Integer cid){
		ModelAndView mv = new ModelAndView();
		Client client = dao.get(Client.class, cid);
		mv.data.put("client", JSONHelper.toJSON(client));
		return mv;
	}
}
