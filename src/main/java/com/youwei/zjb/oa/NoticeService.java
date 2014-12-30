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
import com.youwei.zjb.feedback.entity.Reply;
import com.youwei.zjb.oa.entity.Notice;
import com.youwei.zjb.oa.entity.NoticeReceiver;
import com.youwei.zjb.oa.entity.NoticeReply;
import com.youwei.zjb.oa.entity.Site;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.JSONHelper;

@Module(name="/oa")
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
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView();
		//文化墙
		List<Map> articleList = dao.listAsMap("select n.id as id, n.title as title, n.senderId as senderId, nr.hasRead as hasRead,n.addtime as addtime "
				+ ",n.zans as zans ,nr.zan as zan from Notice n , NoticeReceiver nr where n.id=nr.noticeId and isPublic=1 and nr.receiverId=? order by n.addtime desc" , ThreadSession.getUser().id);
		//公告
		List<Map> noticeList = dao.listAsMap("select n.id as id, n.title as title, n.senderId as senderId, nr.hasRead as hasRead,n.addtime as addtime from Notice n ,"
				+ " NoticeReceiver nr where n.id=nr.noticeId and isPublic=0 and nr.receiverId=? order by n.addtime desc" , ThreadSession.getUser().id);
		mv.jspData.put("articleList", articleList);
		mv.jspData.put("noticeList", noticeList);
		
		List<Site> personalList = dao.listByParams(Site.class, "from Site where uid=?",ThreadSession.getUser().id);
		List<Site> shareList = dao.listByParams(Site.class, "from Site where uid is null");
		mv.jspData.put("personalList", JSONHelper.toJSONArray(personalList));
		mv.jspData.put("shareList", JSONHelper.toJSONArray(shareList));
		return mv;
	}
	
	@Transactional
	@WebMethod
	public ModelAndView view(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		po.reads++;
		dao.saveOrUpdate(po);
		NoticeReceiver nr = dao.getUniqueByParams(NoticeReceiver.class, new String[]{"noticeId" , "receiverId"}, new Object[]{id , ThreadSession.getUser().id});
		if(nr!=null){
			nr.hasRead=1;
		}
		mv.jspData.put("notice", JSONHelper.toJSON(po));
		return mv;
	}
	
	@WebMethod(name="notice/edit")
	public ModelAndView editNotice(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		mv.jspData.put("notice", po);
		return mv;
	}
	
	@WebMethod(name="article/edit")
	public ModelAndView editArticle(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		mv.jspData.put("article", po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView toggleZan(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		NoticeReceiver nr = dao.getUniqueByParams(NoticeReceiver.class, new String[]{"noticeId" , "receiverId"}, new Object[]{ id , ThreadSession.getUser().id});
		if(nr.zan==0){
			nr.zan=1;
			po.zans++;
		}else{
			nr.zan=0;
			po.zans--;
		}
		dao.saveOrUpdate(po);
		dao.saveOrUpdate(nr);
		return mv;
	}
	
	@WebMethod(name="notice/update")
	@Transactional
	public ModelAndView update(Notice notice){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, notice.id);
		if(po==null){
			throw new GException(PlatformExceptionType.BusinessException,"公告已经不存在");
		}
		po.title = notice.title;
		po.conts = notice.conts;
		po.orderx = notice.orderx;
		dao.saveOrUpdate(po);
		
		return mv;
	}
	@WebMethod(name="notice/save")
	@Transactional
	public ModelAndView save(Notice notice , String receivers){
		ModelAndView mv = new ModelAndView();
		if(StringUtils.isEmpty(receivers)){
			receivers = "";
//			throw new GException(PlatformExceptionType.BusinessException, "请先选择接收人");
			//先发给所有人
			List<Map> uids = dao.listAsMap("select id as uid from User where cid=?", ThreadSession.getUser().cid);
			for(Map map : uids){
				receivers+=map.get("uid")+",";
			}
		}
		Notice po = dao.getUniqueByKeyValue(Notice.class, "title", notice.title);
		if(po!=null){
			throw new GException(PlatformExceptionType.BusinessException,"该标题已存在");
		}
		User user = ThreadSession.getUser();
		notice.senderId = user.id;
		notice.addtime = new Date();
		notice.zans=0;
		dao.saveOrUpdate(notice);
		if(!receivers.contains(user.id.toString())){
			receivers +=","+user.id;
		}
		for(String receiver: receivers.split(",")){
			NoticeReceiver nr = new NoticeReceiver();
			nr.noticeId = notice.id;
			nr.hasRead = 0;
			nr.receiverId = Integer.valueOf(receiver);
			nr.zan=0;
			dao.saveOrUpdate(nr);
		}
		mv.data.put("noticeId", notice.id);
		return mv;
	}
	
	@WebMethod(name="notice/list")
	public ModelAndView list(OAQuery query, Page<Map> page){
		ModelAndView mv = new ModelAndView();
		List<Object> params = new ArrayList<Object>();
		User user = ThreadSession.getUser();
		StringBuilder hql = new StringBuilder("select n.id as id, n.title as title, n.senderId as senderId, u.uname as senderName, n.reads as reads, n.replys as replys, nr.hasRead as hasRead,n.addtime as addtime from Notice n ,"
				+ " NoticeReceiver nr , User u where n.id=nr.noticeId and u.id=n.senderId and isPublic=? and nr.receiverId=? ");
		
		params.add(query.isPublic);
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
	
	@WebMethod
	@Transactional
	public ModelAndView addReply(NoticeReply reply){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, reply.noticeId);
		po.replys++;
		dao.saveOrUpdate(reply);
		dao.saveOrUpdate(po);
		return mv;
	}
	
}
