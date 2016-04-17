package com.youwei.zjb.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.HqlHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.DateSeparator;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.biz.entity.OutHouse;
import com.youwei.zjb.client.entity.Client;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;


@Module(name="/mobile/outHouse/")
public class MobileOutHouseService {
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	
	@WebMethod
	public ModelAndView list(Page<Map> page, BizQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select u.uname as uname,oh.uid as uid, oh.dname as dname, oh.id as id, oh.outtime as outtime ,oh.backtime as backtime"
				+ " , oh.type as type,oh.status as status, oh.clientName as clientName, oh.ctels as ctels,oh.houseInfos as houseInfos from User u , OutHouse oh where oh.uid=u.id");

		List<Object> params = new ArrayList<Object>();
		if(query.uid!=null){
			Integer anotherUid = UserHelper.getAnotherUser(query.uid);
			if(anotherUid==null){
				hql.append(" and oh.uid=? ");
				params.add(query.uid);
			}else{
				hql.append(" and (oh.uid=? or oh.uid=? ) ");
				params.add(query.uid);
				params.add(anotherUid);
			}
		}
		
		page.setPageSize(25);
		page.order = Page.DESC;
		page.orderBy = "oh.createtime";
		page = dao.findPage(page, hql.toString(),true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.sdf7.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		OutHouse po = dao.get(OutHouse.class, id);
		User u = dao.get(User.class, po.uid);
		Client client = dao.get(Client.class, po.clientId);
		mv.data = JSONHelper.toJSON(po);
		mv.data.put("uname", u.uname);
		mv.data.put("dname", u.Department().namea);
		mv.data.put("tel", u.tel);
		if(client!=null){
			mv.data.put("clientName",client.name);
		}
		return mv;
	}
}
