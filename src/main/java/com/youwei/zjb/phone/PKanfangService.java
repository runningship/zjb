package com.youwei.zjb.phone;

import java.util.Date;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import cn.jpush.api.utils.StringUtils;

import com.youwei.zjb.biz.entity.OutHouse;
import com.youwei.zjb.util.DataHelper;

@Module(name="/mobile/kanfang/")
public class PKanfangService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(Page<OutHouse> page , Integer uid , Integer isChuzu){
		ModelAndView mv = new ModelAndView();
		page = dao.findPage(page, "from OutHouse where 1=1 and uid=? and isChuzu=? order by createtime desc", uid , isChuzu);
		mv.data.put("page", JSONHelper.toJSON(page , DataHelper.sdf7.toPattern()));
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(Integer id){
		ModelAndView mv = new ModelAndView();
		OutHouse po = dao.get(OutHouse.class, id);
		if(po!=null){
			mv.data.put("kanfang", JSONHelper.toJSON(po));
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(OutHouse daikan){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(daikan.houseInfos)){
			throw new GException(PlatformExceptionType.ParameterMissingError,"houseInfos","");
		}
		if(StringUtils.isEmpty(daikan.conts)){
			throw new GException(PlatformExceptionType.ParameterMissingError,"conts","");
		}
		daikan.createtime = new Date();
		daikan.type =0;
		daikan.cid=0;
		daikan.did=0;
		dao.saveOrUpdate(daikan);
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(OutHouse daikan){
		ModelAndView mv = new ModelAndView();
		OutHouse po = dao.get(OutHouse.class, daikan.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException,"记录不存在或已删除!");
		}
		po.houseInfos = daikan.houseInfos;
		po.conts = daikan.conts;
		po.ctels = daikan.ctels;
		po.clientName = daikan.clientName;
		po.outtime = daikan.outtime;
		po.backtime = daikan.backtime;
		dao.saveOrUpdate(po);
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(OutHouse daikan){
		ModelAndView mv = new ModelAndView();
		OutHouse po = dao.get(OutHouse.class, daikan.id);
		if(po!=null){
			dao.delete(po);
		}
		mv.data.put("result", 0);
		return mv;
	}
}
