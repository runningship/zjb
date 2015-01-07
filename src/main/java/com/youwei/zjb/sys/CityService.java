package com.youwei.zjb.sys;

import java.io.File;
import java.io.IOException;

import net.sf.json.JSONArray;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.sys.entity.City;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/city/")
public class CityService {
	
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		try {
			String path = ConfigCache.get("citys_file", "");
			String text = FileUtils.readFileToString(new File(path), "utf8");
			mv.data.put("citys", JSONArray.fromObject(text));
		} catch (IOException e) {
			LogUtil.log(Level.WARN, "load filter words failed ", e);
			mv.data.put("citys", new JSONArray());
		}
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
