package com.youwei.zjb.phone;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

import com.youwei.zjb.sys.CityService;
import com.youwei.zjb.user.entity.Charge;
import com.youwei.zjb.util.DataHelper;

@Module(name="/mobile/")
public class ChargeService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	CityService cityService = TransactionalServiceHelper.getTransactionalService(CityService.class);
	private static final String masterSecret ="17bfb60aef56934949f1b231";
	private static final String appKey ="71f5687861a727f2827ba04a";
	
	@WebMethod
	public ModelAndView charge(Integer userId , Page<Map> page){
		ModelAndView mv = new ModelAndView();
		if(userId==null){
			mv.data.put("result", "1");
			mv.data.put("msg", "没找到相关信息");
			return mv;
		}
		StringBuilder hql = new StringBuilder("select c.id as id , c.tradeNo as tradeON , c.fee as fee , c.addtime as addtime"
				+ " from Charge c  where c.uid=?");
		page = dao.findPage(page, hql.toString(), true, new Object[]{userId});
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
}
