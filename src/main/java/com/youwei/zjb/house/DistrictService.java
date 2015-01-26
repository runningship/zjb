package com.youwei.zjb.house;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.util.DataHelper;

@Module(name="/areas/")
public class DistrictService {

	CommonDaoService service = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(District district){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(district.name)){
			throw new GException(PlatformExceptionType.BusinessException,"小区名不能为空");
		}
		District po = service.getUniqueByKeyValue(District.class, "name", district.name);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"小区名重复");
		}
		district.pinyin=DataHelper.toPinyin(district.name);
		district.pyShort=DataHelper.toPinyinShort(district.name);
		service.saveOrUpdate(district);
		mv.data.put("msg", "添加成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int areaId){
		ModelAndView mv = new ModelAndView();
		District area = service.get(District.class, areaId);
		mv.data.put("area", JSONHelper.toJSON(area));
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		District po = service.get(District.class, id);
		if(po!=null){
			service.delete(po);
		}
		return mv;
	}
	
	@WebMethod
	public ModelAndView getMapInfo(String area){
		ModelAndView mv = new ModelAndView();
		List<District> areas = service.listByParams(District.class, "from District where name like ?", "%"+area+"%");
		mv.data.put("areas", JSONHelper.toJSONArray(areas));
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(String search ,String leibie, Page<District> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("from District where 1=1 ");
		if("chongfu".equals(leibie)){
			hql = new StringBuilder("select name as name,quyu as quyu from District group by name,quyu having count(*)>1");
			List<Map> list = service.listAsMap(hql.toString());
			mv.data.put("list", JSONHelper.toJSONArray(list));
			mv.data.put("leibie", leibie);
			return mv;
		}else if("kong".equals(leibie)){
			hql = new StringBuilder("from District where (maplat is null or maplng is null)");
		}
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotEmpty(search)){
			search = "%"+search+"%";
			hql.append(" and (name like ?  or quyu like ? or pinyin like ? or pyShort like ?)");
			params.add(search);
			params.add(search);
			params.add(search);
			params.add(search);
		}
		
		page = service.findPage(page, hql.toString(), params.toArray());
		mv.data.put("result", 0);
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(District district){
		ModelAndView mv = new ModelAndView();
		if(district.id==null){
			throw new GException(PlatformExceptionType.BusinessException, "id不能为空");
		}
		if(StringUtils.isEmpty(district.name)){
			throw new GException(PlatformExceptionType.BusinessException, "小区名不能为空");
		}
		District po = service.get(District.class, district.id);
		po.address = district.address;
		po.name = district.name;
		po.quyu = district.quyu;
		po.maplat = district.maplat;
		po.maplng  = district.maplng;
		po.pinyin=DataHelper.toPinyin(district.name);
		po.pyShort=DataHelper.toPinyinShort(district.name);
		service.saveOrUpdate(po);
		mv.data.put("msg", "保存成功");
		return mv;
	}
	
	@WebMethod
	public ModelAndView prompt(Page<Map> page , String search){
		ModelAndView mv = new ModelAndView();
		String hql = "";
		hql = "select distinct(h.name) as area , h.address as address , h.quyu as quyu  from District h where h.name like ? or h.pinyin like ? or h.pyShort like ?";
		page.setPageSize(30);
		search = search+"%";
		page= service.findPage(page, hql, true, new Object[]{"%"+search , search , search});
		mv.data.put("houses",JSONHelper.toJSONArray(page.getResult()));
		return mv;
	}
}
