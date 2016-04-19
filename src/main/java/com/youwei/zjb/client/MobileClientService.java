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

import com.youwei.zjb.client.entity.Client;
import com.youwei.zjb.client.entity.ClientGenJin;
import com.youwei.zjb.util.DataHelper;

@Module(name="/mobile/client")
public class MobileClientService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	@WebMethod
	public ModelAndView list(Integer uid , String tel , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("select c.id as cid, c.name as name,c.tels as tels "
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
		List<ClientGenJin> genjinList = dao.listByParams(ClientGenJin.class, "from ClientGenJin where clientId=? ", cid);
		mv.data.put("client", JSONHelper.toJSON(client));
		mv.data.put("genjinList", JSONHelper.toJSONArray(genjinList));
		return mv;
	}
	
	@WebMethod
	public ModelAndView addGenJin(String conts, String clientId ,Integer uid){
		ModelAndView mv = new ModelAndView();
		ClientGenJin gj = new ClientGenJin();
		gj.addtime = new Date();
		gj.clientId = Integer.valueOf(clientId);
		gj.uid=uid;
		gj.conts = conts;
		dao.saveOrUpdate(gj);
		return mv;
	}
	
	@WebMethod
	public ModelAndView deleteClient(Integer id){
		ModelAndView mv = new ModelAndView();
		Client gj = dao.get(Client.class, id);
		dao.delete(gj);
		return mv;
	}
}
