package com.youwei.zjb.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.feedback.entity.FeedBack;
import com.youwei.zjb.feedback.entity.FeedbackQuery;
import com.youwei.zjb.feedback.entity.Reply;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/reply/")
public class ReplyService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView list(Page<Reply> page , FeedbackQuery query){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder(" from Reply  " + " where fbId=?");
		List<Object> params = new ArrayList<Object>();
		params.add(query.fbId);
		if(query.hasRead!=null){
			hql.append(" and hasRead=?");
			params.add(query.hasRead);
		}
		page.orderBy = "addtime";
		page.order = Page.DESC;
		page = dao.findPage(page, hql.toString(),  params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	public ModelAndView add(Reply reply){
		if(reply.conts==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写回复内容");
		}
		reply.addtime = new Date();
		reply.uid = ThreadSession.getUser().id;
		reply.uname = ThreadSession.getUser().uname;
		reply.hasRead =0;
		dao.saveOrUpdate(reply);
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
