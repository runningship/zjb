package com.youwei.zjb.client;

import java.util.ArrayList;
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
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/client")
public class ClientService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(ClientQuery query ,Page<Client> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User u = ThreadSession.getUser();
		StringBuilder hql = new StringBuilder("from Client where 1=1 ");
		page.orderBy = "addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(Client client){
		ModelAndView mv = new ModelAndView();
		User ywy = dao.get(User.class, client.uid);
		client.uid = ywy.id;
		client.uname = ywy.uname;
		client.did = ywy.Department().id;
		client.dname = ywy.Department().namea;
		client.cid = ywy.Company().id;
		client.cname = ywy.Company().namea;
		User u = ThreadSession.getUser();
		client.djrId = u.id;
		client.djrName = u.uname;
		dao.saveOrUpdate(client);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Client po = dao.get(Client.class, id);
		mv.data.put("client", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Client client){
		ModelAndView mv = new ModelAndView();
		Client po = dao.get(Client.class, client.id);
		po.name = client.name;
		po.beizhu = client.beizhu;
		po.areas = client.areas;
		po.hxings = client.hxings;
		po.lxings = client.lxings;
		po.lcengFrom = client.lcengFrom;
		po.lcengTo = client.lcengTo;
		po.luduans = client.luduans;
		po.mjiFrom = client.mjiFrom;
		po.mjiTo = client.mjiTo;
		po.quyus = client.quyus;
		po.source = client.source;
		po.tels = client.tels;
		po.zxius = client.zxius;
		if(client.chuzu==1){
			po.zujinFrom = client.zujinFrom;
			po.zujinTo = client.zujinTo;
		}else{
			po.jiageFrom = client.jiageFrom;
			po.jiageTo = client.jiageTo;
		}
		if(po.uid.equals(client.uid)){
			//修改业务员
			User ywy = dao.get(User.class, client.uid);
			po.uid = client.uid;
			po.uname = ywy.uname;
			po.did = ywy.Department().id;
			po.dname = ywy.Department().namea;
			po.cid = ywy.Company().id;
			po.cname = ywy.Company().namea;
		}
		dao.saveOrUpdate(client);
		return mv;
	}
}
