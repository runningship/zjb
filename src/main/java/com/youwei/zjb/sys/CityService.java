package com.youwei.zjb.sys;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.sys.entity.City;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/city/")
public class CityService {
	
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		List<City> list = dao.listByParams(City.class, "from City ", new Object[]{});
		mv.data.put("citys", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView test(){
		ModelAndView mv = new ModelAndView();
		mv.returnText="haha";
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(City city){
		ModelAndView mv = new ModelAndView();
		dao.saveOrUpdate(city);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(City city){
		ModelAndView mv = new ModelAndView();
		if(city.id==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择要更新的城市");
		}
		if(StringUtils.isEmpty(city.domain)){
			throw new GException(PlatformExceptionType.ParameterMissingError,"domain","域名不能为空");
		}
		dao.saveOrUpdate(city);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		City city = dao.get(City.class, id);
		if(city!=null){
			dao.delete(city);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		City city = dao.get(City.class, id);
		mv.data = JSONHelper.toJSON(city);
		return mv;
	}
}
