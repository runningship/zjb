package com.youwei.zjb.swq;

import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.biz.entity.Leave;
import com.youwei.zjb.swq.entity.SWQClient;

@Module(name="/swq/")
public class SWQService {

CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView add(SWQClient swq){
		ModelAndView mv = new ModelAndView();
		SWQClient po = dao.getUniqueByParams(SWQClient.class, new String[] {"lic"}, new Object[]{swq.lic});
		if(po!=null){
			if(po.sh==1){
				throw new GException(PlatformExceptionType.BusinessException, "已成功注册,可以登录了");
			}
			mv.data.put("lic", po.lic);
			return mv;
		}
		swq.lic = UUID.randomUUID().toString();
		swq.createtime = new Date(swq.createtimeInLong);
		swq.sh = 0;
		dao.saveOrUpdate(swq);
		mv.data.put("lic", swq.lic);
		return mv;
	}
	
	@WebMethod
	public ModelAndView login(SWQClient swq){
		ModelAndView mv = new ModelAndView();
		SWQClient po = dao.getUniqueByParams(SWQClient.class, new String[] {"lic" , "createtimeInLong"}, new Object[]{swq.lic , swq.createtimeInLong});
		if(po==null){
			LogUtil.log(Level.ERROR, "swq login fail[lic="+swq.lic+",createTime=]"+swq.createtimeInLong , null);
			throw new GException(PlatformExceptionType.BusinessException, "请先注册");
		}else if(po.sh==null || po.sh==0){
			throw new GException(PlatformExceptionType.BusinessException, "等待审核");
		}
		ThreadSession.getHttpSession().setAttribute("swq", true);
		return mv;
	}
}
