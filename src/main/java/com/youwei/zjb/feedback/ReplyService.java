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
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.feedback.entity.FeedBack;
import com.youwei.zjb.feedback.entity.FeedbackQuery;
import com.youwei.zjb.feedback.entity.Reply;

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
	public ModelAndView getUnReadReply(){
		StringBuilder hql = new StringBuilder("select fbId as fbId from Reply where threadUid=? and hasRead=0 and uid<>? group by fbId");
		List<Object> params = new ArrayList<Object>();
		params.add(ThreadSessionHelper.getUser().id);
		params.add(ThreadSessionHelper.getUser().id);
		List<Map> list = dao.listAsMap(hql.toString(), params.toArray());
		ModelAndView mv = new ModelAndView();
		int id=-1;
		if(!list.isEmpty()){
			id = (int) list.get(0).get("fbId");
		}
		mv.data.put("unReadFbId", id);
		return mv;
	}
	@WebMethod
	public ModelAndView add(Reply reply){
		if(reply.conts==null){
			throw new GException(PlatformExceptionType.BusinessException, "请先填写回复内容");
		}
		reply.addtime = new Date();
		reply.uid = ThreadSessionHelper.getUser().id;
		reply.uname = ThreadSessionHelper.getUser().uname;
		reply.hasRead =0;
		FeedBack fb = dao.get(FeedBack.class, reply.fbId);
		reply.threadUid = fb.userId;
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
	
	@WebMethod
	public ModelAndView setRead(int fbId){
		ModelAndView mv = new ModelAndView();
		dao.execute("update Reply set hasRead=1 where fbId=?", fbId);
		return mv;
	}
	
	@WebMethod
	public ModelAndView delete(int id){
		ModelAndView mv = new ModelAndView();
		Reply po = dao.get(Reply.class, id);
		if(po!=null){
			dao.delete(po);
		}
		return mv;
	}
}
