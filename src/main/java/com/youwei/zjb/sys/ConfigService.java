package com.youwei.zjb.sys;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.biz.LeaveStatus;
import com.youwei.zjb.biz.LeaveType;
import com.youwei.zjb.house.ChaoXiang;
import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.LouXing;
import com.youwei.zjb.house.QuYu;
import com.youwei.zjb.house.RentState;
import com.youwei.zjb.house.RentType;
import com.youwei.zjb.house.SellState;
import com.youwei.zjb.house.ShenHe;
import com.youwei.zjb.house.WuhuQuYu;
import com.youwei.zjb.house.ZhuangXiu;

@Module(name="/config/")
public class ConfigService {

	CityService cityService = new CityService();
	@WebMethod
	public ModelAndView getQueryOptions(){
		ModelAndView mv = new ModelAndView();
		mv.data.put("chaoxiang", ChaoXiang.toJsonArray());
//		mv.data.put("datetype", DateType.toJsonArray());
		mv.data.put("fxing", FangXing.toJsonArray());
//		mv.data.put("xingzhi", HouseAttribute.toJsonArray());
//		mv.data.put("leibie", HouseType.toJsonArray());
		mv.data.put("lxing", LouXing.toJsonArray());
		String domain=ThreadSessionHelper.getDomain();
		//此处可以优化
		JSONArray citys = cityService.getCitys();
		for(int i=0;i<citys.size();i++){
			JSONObject city = citys.getJSONObject(i);
			if(city.getString("py").equals(domain)){
				mv.data.put("quyu", city.getJSONArray("quyu"));
			}
		}
//		if("wuhu".equals(domain)){
//			mv.data.put("quyu", WuhuQuYu.toJsonArray());
//		}else{
//			mv.data.put("quyu", QuYu.toJsonArray());
//		}
		
		mv.data.put("ztai_sell", SellState.toJsonArray());	
		mv.data.put("ztai_rent", RentState.toJsonArray());
		mv.data.put("shenhe", ShenHe.toJsonArray());
		mv.data.put("zhuangxiu", ZhuangXiu.toJsonArray());
		mv.data.put("rent_type", RentType.toJsonArray());
		mv.data.put("leaveType", LeaveType.toJsonArray());
		mv.data.put("leaveStatus", LeaveStatus.toJsonArray());
		return mv;
	}
}
