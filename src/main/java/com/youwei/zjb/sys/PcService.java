package com.youwei.zjb.sys;

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

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.sys.entity.PC;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.util.SecurityHelper;

@Module(name="/pc/")
public class PcService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(PC pc){
		ModelAndView mv = new ModelAndView();
		if(pc==null){
			return mv;
		}
		
		if(pc.did==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写店面信息");
		}
		
		if(StringUtils.isEmpty(pc.mac) && StringUtils.isEmpty(pc.disk)){
			throw new GException(PlatformExceptionType.BusinessException, "机器码为空，不能授权，可能是由于您安装的是精简版的操作系统.");
		}
		Department comp =  dao.getUniqueByKeyValue(Department.class, "authCode", pc.authCode);
		if(comp==null){
			throw new GException(PlatformExceptionType.BusinessException, "授权码不正确,请联系系统管理员");
		}
		long pcCount = dao.countHql("select count(*) from PC where cid=?", comp.id);
		if(pcCount>=comp.pcnum){
			throw new GException(PlatformExceptionType.BusinessException, "已经授权的机器数量超过预定额度");
		}
		if(pc.pcname!=null){
			pc.pcname = pc.pcname.replace("-", "").toLowerCase();
		}
		if(pc.disk!=null){
			pc.disk = pc.disk.toLowerCase();
		}
		if(pc.cpu!=null){
			pc.cpu = pc.cpu.toLowerCase();
		}
		pc.uuid = SecurityHelper.Md5(pc.cpu+pc.disk)+SecurityHelper.Md5(pc.mac);
		PC po = dao.getUniqueByParams(PC.class, new String[]{"did","uuid"},	new Object[]{pc.did , pc.uuid});
		if(po==null){
			pc.addtime = new Date();
			pc.lock=0;
			pc.cid = comp.id;
			pc.lastip = ThreadSession.getIp();
			dao.saveOrUpdate(pc);
		}else{
			throw new GException(PlatformExceptionType.BusinessException, "您已经申请过授权，无需重复申请");
		}
		
		mv.data.put("result", "0");
		mv.data.put("msg", "申请授权成功，等待审核");
		return mv;
	}
	
	@WebMethod
	public ModelAndView unAuthList(Page<PC> page){
		ModelAndView mv = new ModelAndView();
		page = SimpDaoTool.getGlobalCommonDaoService().findPage(page, "from PC where lock=0 or lock is null");
		mv.data.put("page",JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(Page<Map> page,PCQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select pc.id as id, pc.pcname as pcname, pc.addtime as addtime, pc.lasttime as lasttime, pc.uuid as uuid,pc.lastip as ip, "
				+ " pc.lock as lock, pc.beizhu as beizhu,d.namea as dname  from PC pc,Department d where d.id=pc.did");
		List<Object> params = new ArrayList<Object>();
		if(query.lock!=null){
			hql.append(" and pc.lock=? ");
			params.add(query.lock);
		}
		if(query.did!=null){
			hql.append(" and pc.did=? ");
			params.add(query.did);
		}
		if(query.cid!=null){
			hql.append(" and pc.cid=? ");
			params.add(query.cid);
		}
		
		page.orderBy = "pc.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView shenhe(Integer id){
		ModelAndView mv = new ModelAndView();
		PC pc = dao.get(PC.class, id);
		if(pc!=null){
			if(pc.lock==1){
				pc.lock=0;
			}else{
				pc.lock = 1;
			}
			dao.saveOrUpdate(pc);
		}
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(Integer id){
		ModelAndView mv = new ModelAndView();
		PC pc = dao.get(PC.class, id);
		if(pc!=null){
			dao.delete(pc);
		}
		return mv;
	}
}
