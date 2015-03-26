package com.youwei.zjb.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

@Module(name="/task/")
public class TaskService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView listData(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("from Task where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		dao.findPage(page, hql.toString(), params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView save(Task task){
		ModelAndView mv = new ModelAndView();
		Task po = dao.getUniqueByKeyValue(Task.class, "name", task.name);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException, "重复的任务");
		}
		task.status=0;
		task.taskOn = 0;
		dao.saveOrUpdate(task);
		return mv;
	}
	
	@WebMethod
	public ModelAndView toggleTaskOn(Integer id ){
		ModelAndView mv = new ModelAndView();
		Task po = dao.get(Task.class, id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "任务不存在");
		}
		if(po.taskOn==0){
			po.taskOn =1;
		}else{
			po.taskOn = 0;
		}
		dao.saveOrUpdate(po);
		mv.data.put("task", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	public ModelAndView edit(Integer id){
		ModelAndView mv = new ModelAndView();
		Task po = dao.get(Task.class, id);
		mv.jspData.put("task", po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Task task){
		ModelAndView mv = new ModelAndView();
		Task po = dao.get(Task.class, task.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "任务不存在");
		}
		po.name = task.name;
		po.siteUrl = task.siteUrl;
		po.cityPy = task.cityPy;
		po.area = task.area;
		po.lceng = task.lceng;
		po.zceng = task.zceng;
		po.hxw = task.hxw;
		po.hxf = task.hxf;
		po.hxt = task.hxt;
		po.zxiu = task.zxiu;
		po.mji = task.mji;
		po.zjia = task.zjia;
		po.djia = task.djia;
		po.dateyear = task.dateyear;
		po.lxr = task.lxr;
		po.tel = task.tel;
		po.beizhu = task.beizhu;
		po.quyu = task.quyu;
		po.detailLink = task.detailLink;
		po.listSelector = task.listSelector;
		po.address = task.address;
		po.filterSelector = task.filterSelector;
		po.pubtime = task.pubtime;
		po.detailPageUrlPrefix = task.detailPageUrlPrefix;
		po.site = task.site;
		po.interval = task.interval;
		dao.saveOrUpdate(po);
		return mv;
	}
}
