package com.youwei.zjb.swq;

import java.util.Date;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.biz.entity.Leave;
import com.youwei.zjb.swq.entity.SWQClient;

@Module(name="/swq/")
public class SWQService {

CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(SWQClient swq){
		ModelAndView mv = new ModelAndView();
		SWQClient po = dao.getUniqueByParams(SWQClient.class, new String[] {"uuid"}, new Object[]{swq.uuid});
		if(po!=null){
			
		}
		swq.createtime = new Date(swq.createtimeInLong);
		dao.saveOrUpdate(swq);
		return mv;
	}
	
	@WebMethod
	public ModelAndView login(SWQClient swq){
		ModelAndView mv = new ModelAndView();
		SWQClient po = dao.getUniqueByParams(SWQClient.class, new String[] {"uuid" , "createtimeInLong"}, new Object[]{swq.uuid , swq.createtimeInLong});
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先注册");
		}else if(po.sh==null || po.sh==0){
			throw new GException(PlatformExceptionType.BusinessException, "等待审核");
		}
		return mv;
	}
}
