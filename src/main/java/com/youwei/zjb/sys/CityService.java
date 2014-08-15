package com.youwei.zjb.sys;

import java.util.ArrayList;
import java.util.List;

import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.sys.entity.City;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/city/")
public class CityService {
	
	@WebMethod
	public ModelAndView list(){
		ModelAndView mv = new ModelAndView();
		List<City> list = new ArrayList<City>();
		City hf = new City();
		hf.id=1;
		hf.name="合肥";
		hf.domain = "hf";
		City nj = new City();
		nj.id=2;
		nj.name = "南京";
		nj.domain = "nj";
		list.add(hf);
		list.add(nj);
		mv.data.put("citys", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView test(){
		ModelAndView mv = new ModelAndView();
		mv.returnText="haha";
		return mv;
	}
}
