package com.youwei.zjb.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.converters.StringArrayConverter;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.DateSeparator;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.biz.entity.Leave;
import com.youwei.zjb.biz.entity.OutBiz;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;


@Module(name="/leave/")
public class LeaveService {
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(Leave leave){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(leave.reason)){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写请假原因");
		}
		if(leave.uid==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择业务员");
		}
		User u = dao.get(User.class, leave.uid);
		leave.uid = u.id;
		leave.did = u.did;
		leave.cid  = u.cid;
		leave.sh = 0;
		leave.status = "未审批";
		leave.createtime = new Date();
		dao.saveOrUpdate(leave);
		
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<Map> page, BizQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select u.uname as uname, d.namea as dname, lea.id as id, SubString(lea.reason,1,50) as reason,lea.starttime as starttime ,lea.endtime as endtime"
				+ ",lea.hours as hours, lea.type as type,lea.sh as sh,lea.uid as uid from User u ,Department d, Leave lea where lea.uid=u.id and lea.did=d.id and lea.cid=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSession.getUser().cid);
		if(query.uid!=null){
			hql.append(" and lea.uid=? ");
			params.add(query.uid);
		}
		if(query.did!=null){
			hql.append(" and lea.did=? ");
			params.add(query.did);
		}
		
		if(query.sh!=null){
			hql.append(" and lea.sh=?");
			params.add(query.sh);
		}
		
		if(query.leaveType!=null){
			hql.append(" and lea.type=? ");
			params.add(query.leaveType);
		}
		
		hql.append(HqlHelper.buildDateSegment("lea.starttime",query.starttimeStart,DateSeparator.After,params));
		hql.append(HqlHelper.buildDateSegment("lea.starttime",query.starttimeEnd,DateSeparator.Before,params));
		
		hql.append(HqlHelper.buildDateSegment("lea.endtime",query.endtimeStart,DateSeparator.After,params));
		hql.append(HqlHelper.buildDateSegment("lea.endtime",query.endtimeEnd,DateSeparator.Before,params));
		page.setPageSize(25);
		page.order = Page.DESC;
		page.orderBy = "lea.createtime";
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.sdf3.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView shenpi(Leave leave){
		ModelAndView mv = new ModelAndView();
		Leave po = dao.get(Leave.class, leave.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "请假记录不存在或已被删除");
		}
		po.status = leave.status;
		po.conts = leave.conts;
		po.sh=leave.sh;
		po.pyUid = ThreadSession.getUser().id;
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		Leave po = dao.get(Leave.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "请假记录不存在或已被删除");
		}
		dao.delete(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Leave po = dao.get(Leave.class, id);
		User u = dao.get(User.class, po.uid);
		mv.data = JSONHelper.toJSON(po,DataHelper.sdf3.toPattern());
		mv.data.put("uname", u.uname);
		mv.data.put("dname", u.Department().namea);
		mv.data.put("tel", u.tel);
		User py = dao.get(User.class, po.pyUid);
		if(py!=null){
			mv.data.put("pyr", py.uname);
		}else{
			mv.data.put("pyr", ThreadSession.getUser().uname);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Leave leave){
		Leave po = dao.get(Leave.class, leave.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "请假记录不存在或已被删除");
		}
		po.reason = leave.reason;
		po.starttime = leave.starttime;
		po.endtime = leave.endtime;
		po.hours = leave.hours;
		po.type= leave.type;
		po.sh = leave.sh;
		po.conts = leave.conts;
		dao.saveOrUpdate(po);
		return get(po.id);
	}
}
