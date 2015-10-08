package com.youwei.zjb.trial;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.ThreadSession;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.im.IMServer;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.MailUtil;

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
		trial.finish=0;
		LogUtil.info("收到试用请求:city="+ThreadSession.getCityPY());
		ThreadSession.setCityPY("hefei");
		dao.saveOrUpdate(trial);
//		mv.returnText="<script>window.location=\"http://www.zhongjiebao.com/success.html\"</script>";
		mv.returnText="";
		StringBuilder msg = new StringBuilder("收到新的试用请求").append("<br/>");
		msg.append("城市: ").append(trial.city).append("<br/>");
		msg.append("公司名称: ").append(trial.cname).append("<br/>");
		msg.append("店面名称: ").append(trial.dname).append("<br/>");
		msg.append("姓名: ").append(trial.uname).append("<br/>");
		msg.append("手机号码: ").append(trial.tel).append("<br/>");
		msg.append("qq: ").append(trial.qq).append("<br/>");
		try{
			IMServer.sendMsgToGroup(1, msg.toString());
		}catch(Exception ex){
			LogUtil.warning(msg.toString());
		}
		try{
			List<String> toList = new ArrayList<String>();
			toList.add("2975066320@qq.com");//yulong
			toList.add("947813825@qq.com");//gujun
			toList.add("894350008@qq.com");
			toList.add("253187898@qq.com");
			toList.add("673508153@qq.com");//jinfei
			MailUtil.send_email(toList, "中介宝试用申请", msg.toString());
		}catch(Exception ex){
			LogUtil.warning(msg.toString());
		}
		ThreadSession.getHttpservletresponse().setHeader("Access-Control-Allow-Origin", "*");
		return mv;
	}
	
	@WebMethod
	public ModelAndView update(Trial trial){
		ModelAndView mv = new ModelAndView();
		Trial po = dao.get(Trial.class, trial.id);
		User me = ThreadSessionHelper.getUser();
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
