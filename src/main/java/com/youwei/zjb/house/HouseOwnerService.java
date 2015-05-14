package com.youwei.zjb.house;

import java.util.Date;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.house.entity.HouseOwner;
import com.youwei.zjb.house.entity.TelVerifyCode;

@Module(name="/houseOwner")
public class HouseOwnerService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView sendVerifyCode(String tel){
		ModelAndView mv = new ModelAndView();
		TelVerifyCode tvc = new TelVerifyCode();
		tvc.tel = tel;
		//send code to tel
		tvc.sendtime =  new Date();
		dao.saveOrUpdate(tvc);
		return mv;
	}
	
	@WebMethod
	public ModelAndView verifyCode(String tel , String code){
		ModelAndView mv = new ModelAndView();
		TelVerifyCode tvc = dao.getUniqueByParams(TelVerifyCode.class, new String[]{"tel","code" },  new Object[]{tel , code});
		if(tvc==null){
			//验证码不正确
		}
		if(System.currentTimeMillis() - tvc.sendtime.getTime()>300*1000){
			//验证码已经过期
		}
		HouseOwner owner  = new HouseOwner();
		owner.tel = tel;
		return mv;
	}
}
