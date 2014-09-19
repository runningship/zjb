package com.youwei.zjb.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.house.entity.ExpHouse;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/house/exp/")
public class ExpHouseService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(Page<ExpHouse> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		page.orderBy="addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, "from ExpHouse where finish is null or finish=0" , params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		ExpHouse po = dao.get(ExpHouse.class, id);
		mv.data = JSONHelper.toJSON(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView start(int id){
		ModelAndView mv = new ModelAndView();
		ExpHouse po = dao.get(ExpHouse.class, id);
		if(StringUtils.isNotEmpty(po.shr)){
			po.shr = ThreadSession.getUser().uname;
		}else{
			throw new GException(PlatformExceptionType.BusinessException,"该房源正由"+po.shr+"在审核");
		}
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView end(int id){
		ModelAndView mv = new ModelAndView();
		ExpHouse po = dao.get(ExpHouse.class, id);
		if(ThreadSession.getUser().uname.equals(po.shr)){
			po.shr="";
		}else{
			throw new GException(PlatformExceptionType.BusinessException,"该房源不是由您在审核，所以您不能关闭流程");
		}
		dao.saveOrUpdate(po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView publish(House house ,String hxing, int id){
		ModelAndView mv = new ModelAndView();
		ExpHouse po = dao.get(ExpHouse.class, id);
		po.finish = 1;
		
		return mv;
	}
}
