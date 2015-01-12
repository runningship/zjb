package com.youwei.zjb.trial;

import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.user.entity.User;

@Module(name="/trial/")
public class TrialService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView listData(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(Trial trial){
		ModelAndView mv = new ModelAndView();
		dao.saveOrUpdate(trial);
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Trial trial){
		ModelAndView mv = new ModelAndView();
		Trial po = dao.get(Trial.class, trial.id);
		User me = ThreadSession.getUser();
		if(po!=null){
			po.clConts = trial.clConts;
			po.finish = trial.finish;
			po.clrId = me.id;
			po.clrName = me.uname;
			dao.saveOrUpdate(trial);
		}
		return mv;
	}
}
