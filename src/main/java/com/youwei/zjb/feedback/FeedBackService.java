package com.youwei.zjb.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.JSONHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.feedback.entity.ErrorReport;
import com.youwei.zjb.feedback.entity.FeedBack;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.MailUtil;

@Module(name="/feedback/")
public class FeedBackService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select fb.id as id, SubString(fb.conts,1,30) as conts ,fb.addtime as addtime , u.uname as uname, d.namea as deptName from FeedBack fb, User u, "
				+ "Department d where fb.userId=u.id and u.did=d.id");
		List<Object> params = new ArrayList<Object>();
		page.orderBy = "fb.addtime";
		page.order = Page.DESC;
		page.pageSize=25;
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listMy(Page<Map> page , String search){
		ModelAndView mv = new ModelAndView();
		//id>117表示是5.0以后的反馈
		StringBuilder hql = new StringBuilder("select fb.id as id, SubString(fb.conts,1,30) as conts ,fb.addtime as addtime  from FeedBack fb where userId=? and fb.conts like ? ");
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSessionHelper.getUser().id);
		params.add("%"+search+"%");
		page.orderBy = "fb.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(), true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(FeedBack fb){
		if(fb.conts==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写反馈内容");
		}
		fb.addtime = new Date();
		fb.userId = ThreadSessionHelper.getUser().id;
		dao.saveOrUpdate(fb);
		try{
			List<String> toList = new ArrayList<String>();
			toList.add("253187898@qq.com");
			MailUtil.send_email(toList, "中介宝意见", fb.conts);
		}catch(Exception ex){
			LogUtil.warning("---");
		}
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView reportError(String host , String stack){
		System.out.println(stack);
		User u = ThreadSessionHelper.getUser();
		ErrorReport err = new ErrorReport();
		err.stack = stack;
		err.host = host;
		if(u!=null){
			err.uid=u.id;
			err.did = u.did;
			err.cid = u.cid;
			err.addtime = new Date();
		}
		dao.saveOrUpdate(err);
		return new ModelAndView();
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		FeedBack po = dao.get(FeedBack.class, id);
		mv.data.put("feedback", JSONHelper.toJSON(po));
		return mv;
	}
}
