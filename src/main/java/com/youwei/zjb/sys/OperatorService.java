package com.youwei.zjb.sys;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.DateSeparator;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.sys.entity.OperRecord;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.HqlHelper;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/oper/")
public class OperatorService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void add(OperatorType operType , String conts){
		OperRecord oper = new OperRecord();
		oper.addtime = new Date();
		User user = ThreadSession.getUser();
		oper.uid = user.id;
		oper.type = operType.getCode();
		oper.ip = ThreadSession.getIp();
		oper.did = user.did;
		oper.cid = user.cid;
		oper.uname = user.uname;
		oper.conts = conts;
		try{
			dao.saveOrUpdate(oper);
		}catch(Exception ex){
			LogUtil.log(Level.WARN, "记录日志失败", ex);
		}
	}
	
	@WebMethod
	public ModelAndView list(Page<Map> page , OperatorQuery query, int operType){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		params.add(operType);
		StringBuilder hql = new StringBuilder("select r.conts as conts , u.uname as uname , d.namea as deptName ,r.addtime as addtime, r.ip as ip from OperRecord r ,User u , Department d where r.uid=u.id and u.did=d.id and r.type=?");
		hql.append(HqlHelper.buildDateSegment("r.addtime", query.addtimeStart, DateSeparator.After, params));
		hql.append(HqlHelper.buildDateSegment("r.addtime", query.addtimeEnd, DateSeparator.Before, params));
		if(StringUtils.isNotEmpty(query.xpath)){
			hql.append(" and u.orgpath like ? ");
			params.add("%"+query.xpath+"%");
		}
		page.orderBy="r.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
