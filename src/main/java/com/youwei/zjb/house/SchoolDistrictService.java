package com.youwei.zjb.house;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.entity.SchoolDistrict;

@Module(name="/schoolDistrict/")
public class SchoolDistrictService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(SchoolDistrict district){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(district.name)){
			throw new GException(PlatformExceptionType.BusinessException,"小区名不能为空");
		}
		service.saveOrUpdate(district);
		mv.data.put("msg", "添加成功");
		mv.data.put("id", district.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		SchoolDistrict po = service.get(SchoolDistrict.class, id);
		if(po!=null){
			service.delete(po);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(SchoolDistrict district){
		ModelAndView mv = new ModelAndView();
		if(district.id==null){
			throw new GException(PlatformExceptionType.BusinessException, "id不能为空");
		}
		if(StringUtils.isEmpty(district.name)){
			throw new GException(PlatformExceptionType.BusinessException, "学区名不能为空");
		}
		SchoolDistrict po = service.get(SchoolDistrict.class, district.id);
		po.name = district.name;
		po.offsetX = district.offsetX;
		po.offsetY = district.offsetY;
		service.saveOrUpdate(po);
		mv.data.put("msg", "保存成功");
		mv.data.put("district", JSONHelper.toJSON(po));
		return mv;
	}
}
