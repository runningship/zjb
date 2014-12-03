package com.youwei.zjb.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.biz.entity.Leave;
import com.youwei.zjb.biz.entity.OutBiz;
import com.youwei.zjb.biz.entity.OutHouse;
import com.youwei.zjb.client.entity.Client;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;


@Module(name="/outHouse/")
public class OutHouseService {
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(OutHouse outHouse){
		ModelAndView mv = new ModelAndView();
		validate(outHouse);
		User u = dao.get(User.class, outHouse.uid);
		if(outHouse.clientId!=null){
			Client c = dao.get(Client.class, outHouse.clientId);
			outHouse.clientName = c.name;
			outHouse.ctels = c.tels;
		}
		outHouse.uid = u.id;
		outHouse.did = u.did;
		outHouse.cid  = u.cid;
		outHouse.dname = u.Department().namea;
		outHouse.status = "待返回";
		outHouse.createtime = new Date();
		dao.saveOrUpdate(outHouse);
		return mv;
	}
	
	private void validate(OutHouse outHouse){
		if(outHouse.clientId==null && outHouse.type==0){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择客源");
		}
		if(StringUtils.isEmpty(outHouse.houseIds) && StringUtils.isEmpty(outHouse.houseInfos)){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择房源");
		}
		if(outHouse.uid==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择业务员");
		}
	}
	@WebMethod
	public ModelAndView list(Page<Map> page, BizQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select u.uname as uname,oh.uid as uid, oh.dname as dname, oh.id as id, oh.outtime as outtime ,oh.backtime as backtime"
				+ " , oh.type as type,oh.status as status, oh.clientName as clientName, oh.ctels as ctels,oh.houseInfos as houseInfos from User u , OutHouse oh where oh.uid=u.id  and oh.cid=?");

//		StringBuilder hql = new StringBuilder("select c.dname as dname, oh.id as id, oh.outtime as outtime ,oh.backtime as backtime"
//				+ " , oh.type as type,c.name as clientName, c.tels as ctels,oh.houseInfos as houseInfos from OutHouse  oh left join client c on oh.clientId=c.id where oh.cid=? ");
//		
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSession.getUser().cid);
		if(query.uid!=null){
			hql.append(" and oh.uid=? ");
			params.add(query.uid);
		}
		if(query.did!=null){
			hql.append(" and oh.did=? ");
			params.add(query.did);
		}
		
		if(query.type!=null){
			hql.append(" and oh.type=? ");
			params.add(query.type);
		}
		if(StringUtils.isNotEmpty(query.clientName)){
			hql.append(" and oh.clientName like ?");
			params.add("%"+query.clientName +"%");
		}
		if(StringUtils.isNotEmpty(query.ctel)){
			hql.append(" and oh.ctels like ?");
			params.add("%"+query.ctel +"%");
		}
		if(query.area!=null){
			hql.append(" and oh.houseInfos like ? ");
			params.add("%"+query.area+"%");
		}
		if(StringUtils.isNotEmpty(query.status)){
			hql.append(" and oh.status=? ");
			params.add(query.status);
		}
		hql.append(HqlHelper.buildDateSegment("oh.outtime",query.outtimeStart,DateSeparator.After,params));
		hql.append(HqlHelper.buildDateSegment("oh.outtime",query.outtimeEnd,DateSeparator.Before,params));
		page.setPageSize(25);
		page.order = Page.DESC;
		page.orderBy = "oh.createtime";
		page = dao.findPage(page, hql.toString(),true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
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
	
	@WebMethod
	public ModelAndView shenpi(OutHouse oh){
		ModelAndView mv = new ModelAndView();
		OutHouse po = dao.get(OutHouse.class, oh.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "记录不存在或已被删除");
		}
		po.status = "已批阅";
		po.pyyj = oh.pyyj;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(OutHouse oh){
		OutHouse po = dao.get(OutHouse.class, oh.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "记录不存在或已被删除");
		}
		validate(oh);
		if(oh.clientId!=null){
			po.clientId = oh.clientId;
			Client c = dao.get(Client.class, oh.clientId);
			po.clientName = c.name;
			po.ctels = c.tels;
		}
		po.houseIds = oh.houseIds;
		po.houseInfos = oh.houseInfos;
		po.outtime = oh.outtime;
		po.backtime = oh.backtime;
		po.remarks = oh.remarks;
		if(StringUtils.isNotEmpty(oh.status)){
			po.status = oh.status;
		}
		dao.saveOrUpdate(po);
		return get(po.id);
	}
	
	@WebMethod
	public ModelAndView back(OutHouse oh){
		OutHouse po = dao.get(OutHouse.class, oh.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "记录不存在或已被删除");
		}
		po.backtime = oh.backtime;
		if(StringUtils.isNotEmpty(oh.status)){
			po.status = oh.status;
		}
		po.conts = oh.conts;
		dao.saveOrUpdate(po);
		return get(po.id);
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		OutHouse po = dao.get(OutHouse.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "记录不存在或已被删除");
		}
		dao.delete(po);
		return mv;
	}
}
