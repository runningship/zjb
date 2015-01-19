package com.youwei.zjb.swq;

import java.util.Date;
import java.util.UUID;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

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
		SWQClient po2 = dao.getUniqueByParams(SWQClient.class, new String[] {"lic" , "createtimeInLong"}, new Object[]{swq.lic , swq.createtimeInLong});
		SWQClient po = dao.getUniqueByParams(SWQClient.class, new String[] {"lic"}, new Object[]{swq.lic});
		if(po==null){
			LogUtil.info("swq login fail with lic only,[lic="+swq.lic+",createTime=]"+swq.createtimeInLong);
			throw new GException(PlatformExceptionType.BusinessException, "请先注册");
		}else if(po.sh==null || po.sh==0){
			throw new GException(PlatformExceptionType.BusinessException, "等待审核");
		}
		if(po2==null){
			LogUtil.info("swq login with lic only,[lic="+swq.lic+",createTime=]"+swq.createtimeInLong);
		}
		ThreadSession.getHttpSession().setAttribute("swq", true);
		return mv;
	}
	
	@WebMethod
	public ModelAndView index_58(){
		ModelAndView mv = new ModelAndView();
		boolean swq = (Boolean)ThreadSession.getHttpSession().getAttribute("swq");
		if(swq){
			mv.jspData.put("${pass}", "1");
		}
		return mv;
	}
}
