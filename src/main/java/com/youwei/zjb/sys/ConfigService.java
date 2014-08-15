package com.youwei.zjb.sys;

import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.ChaoXiang;
import com.youwei.zjb.house.DateType;
import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.HouseAttribute;
import com.youwei.zjb.house.HouseType;
import com.youwei.zjb.house.JiaoYi;
import com.youwei.zjb.house.LouXing;
import com.youwei.zjb.house.QuYu;
import com.youwei.zjb.house.ShenHe;
import com.youwei.zjb.house.State;
import com.youwei.zjb.house.ZhuangXiu;

@Module(name="/config/")
public class ConfigService {

	@WebMethod
	public ModelAndView getQueryOptions(){
		ModelAndView mv = new ModelAndView();
		mv.data.put("chaoxiang", ChaoXiang.toJsonArray());
		mv.data.put("datetype", DateType.toJsonArray());
		mv.data.put("fangxing", FangXing.toJsonArray());
		mv.data.put("xingzhi", HouseAttribute.toJsonArray());
		mv.data.put("leibie", HouseType.toJsonArray());
		mv.data.put("jiaoyi", JiaoYi.toJsonArray());
		mv.data.put("louxing", LouXing.toJsonArray());
		mv.data.put("quyu", QuYu.toJsonArray());
		mv.data.put("zhuangtai", State.toJsonArray());
		mv.data.put("shenhe", ShenHe.toJsonArray());
		mv.data.put("zhuangxiu", ZhuangXiu.toJsonArray());
		return mv;
	}
}
