package com.youwei.zjb.sys;

import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

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
import com.youwei.zjb.house.ZhuangXiu;

@Module(name="/config/")
public class ConfigService {

	@WebMethod
	public ModelAndView getQueryOptions(){
		ModelAndView mv = new ModelAndView();
		mv.data.put("chaoxiang", ChaoXiang.toJsonArray());
//		mv.data.put("datetype", DateType.toJsonArray());
		mv.data.put("fxing", FangXing.toJsonArray());
//		mv.data.put("xingzhi", HouseAttribute.toJsonArray());
//		mv.data.put("leibie", HouseType.toJsonArray());
		mv.data.put("lxing", LouXing.toJsonArray());
		mv.data.put("quyu", QuYu.toJsonArray());
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
