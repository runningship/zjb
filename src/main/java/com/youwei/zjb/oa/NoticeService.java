package com.youwei.zjb.oa;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.GException;
import org.bc.sdak.Page;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.PlatformExceptionType;
import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.oa.entity.Notice;
import com.youwei.zjb.oa.entity.NoticeReceiver;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/oa/notice")
public class NoticeService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView getUnReadStatistic(){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select nc.id as id,  nc.fenlei as fenlei,count(*) as total from Notice n, NoticeReceiver nr , NoticeClass nc"
				+ " where n.id=nr.noticeId and n.claid=nc.id  and nr.receiverId=? and nr.hasRead=0 group by nc.fenlei");
		List<Map> list = dao.listAsMap(hql.toString(), ThreadSession.getUser().id);
		mv.data.put("oaData", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		dao.execute("update NoticeReceiver set hasRead=1 where noticeId=? and receiverId=?", id,ThreadSession.getUser().id);
		mv.data.put("notice", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView update(Notice notice , String receivers){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, notice.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException,"公告已经不存在");
		}
		if(StringUtils.isEmpty(receivers)){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择接受人");
		}
		po.title = notice.title;
		po.conts = notice.conts;
		dao.saveOrUpdate(po);
		dao.execute("delete from NoticeReceiver where noticeId=? ", po.id);
		
		User user = ThreadSession.getUser();
		if(!receivers.contains(user.id.toString())){
			receivers +=","+user.id;
		}
		for(String receiver: receivers.split(",")){
			NoticeReceiver nr = new NoticeReceiver();
			nr.noticeId = notice.id;
			nr.hasRead = 0;
			nr.receiverId = Integer.valueOf(receiver);
			dao.saveOrUpdate(nr);
		}
		return mv;
	}
	@WebMethod
	public ModelAndView add(Notice notice , String receivers){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(receivers)){
			throw new GException(PlatformExceptionType.BusinessException, "请先选择接收人");
		}
		Notice po = dao.getUniqueByKeyValue(Notice.class, "title", notice.title);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"该标题已存在");
		}
		User user = ThreadSession.getUser();
		notice.senderId = user.id;
		notice.addtime = new Date();
		dao.saveOrUpdate(notice);
		if(!receivers.contains(user.id.toString())){
			receivers +=","+user.id;
		}
		for(String receiver: receivers.split(",")){
			NoticeReceiver nr = new NoticeReceiver();
			nr.noticeId = notice.id;
			nr.hasRead = 0;
			nr.receiverId = Integer.valueOf(receiver);
			dao.saveOrUpdate(nr);
		}
		mv.data.put("noticeId", notice.id);
		return mv;
	}
	
	@WebMethod
	public ModelAndView list(OAQuery query, Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User user = ThreadSession.getUser();
		StringBuilder hql = new StringBuilder("select nr.hasRead as hasRead,n.id as id, n.title as title, n.addtime as addtime, nc.fenlei as classTitle, u.uname as uname from Notice n, NoticeReceiver nr , NoticeClass nc , User u where n.id=nr.noticeId and n.claid=nc.id and u.id=n.userId and nr.receiverId=?");
		params.add(user.id);
		page.orderBy = "n.addtime";
		page.order = Page.DESC;
		page = dao.findPage(page , hql.toString() , true, params.toArray());
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod
	@Transactional
	public ModelAndView delete(int noticeId){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, noticeId);
		if(po!=null){
			dao.delete(po);
		}
		dao.execute("delete from NoticeReceiver where noticeId = ? ", noticeId);
//		dao.execute("delete from Attachment where bizType = 'oa' and recordId=? ", noticeId);
		mv.data.put("msg", "删除成功");
		return mv;
	}
}
