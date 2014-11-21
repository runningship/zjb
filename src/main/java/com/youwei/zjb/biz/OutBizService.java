package com.youwei.zjb.biz;

import java.util.ArrayList;
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
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;


@Module(name="/outBiz/")
public class OutBizService {
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(OutBiz biz){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(biz.reason)){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写外出原因");
		}
		if(biz.uid==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择业务员");
		}
		User u = dao.get(User.class, biz.uid);
		biz.uid = u.id;
		biz.did = u.did;
		biz.cid  = u.cid;
		biz.status = "待返回";
		biz.createtime = new Date();
		dao.saveOrUpdate(biz);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<Map> page, BizQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select u.uname as uname, d.namea as dname, biz.id as id, biz.reason as reason,biz.outtime as outtime ,biz.backtime as backtime"
				+ ", biz.status as ztai , biz.uid as uid from User u ,Department d, OutBiz biz where biz.uid=u.id and biz.did=d.id and biz.cid=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSession.getUser().cid);
		if(query.uid!=null){
			hql.append(" and biz.uid=? ");
			params.add(query.uid);
		}
		if(query.did!=null){
			hql.append(" and biz.did=? ");
			params.add(query.did);
		}
		if(StringUtils.isNotEmpty(query.status)){
			hql.append(" and biz.status=? ");
			params.add(query.status);
		}
		
		hql.append(HqlHelper.buildDateSegment("biz.outtime",query.outtimeStart,DateSeparator.After,params));
		hql.append(HqlHelper.buildDateSegment("biz.outtime",query.outtimeEnd,DateSeparator.Before,params));
		page.setPageSize(25);
		page.order = Page.DESC;
		page.orderBy = "biz.createtime";
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		OutBiz po = dao.get(OutBiz.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "请假记录不存在或已被删除");
		}
		dao.delete(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		OutBiz po = dao.get(OutBiz.class, id);
		User u = dao.get(User.class, po.uid);
		mv.data = JSONHelper.toJSON(po , DataHelper.sdf3.toPattern());
		mv.data.put("uname", u.uname);
		mv.data.put("dname", u.Department().namea);
		mv.data.put("tel", u.tel);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(OutBiz biz){
		ModelAndView mv = new ModelAndView();
		OutBiz po = dao.get(OutBiz.class, biz.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "请假记录不存在或已被删除");
		}
		po.reason = biz.reason;
		po.outtime = biz.outtime;
		po.backtime = biz.backtime;
		po.conts = biz.conts;
		po.status = biz.status;
		dao.saveOrUpdate(po);
		return mv;
	}
}
