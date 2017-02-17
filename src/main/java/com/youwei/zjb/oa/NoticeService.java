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
import org.bc.sdak.utils.JSONHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.PlatformExceptionType;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSessionHelper;
import com.youwei.zjb.oa.entity.Notice;
import com.youwei.zjb.oa.entity.NoticeReceiver;
import com.youwei.zjb.oa.entity.NoticeReply;
import com.youwei.zjb.oa.entity.Site;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.HTMLSpirit;

@Module(name="/oa")
public class NoticeService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView getUnReadStatistic(){
		ModelAndView mv = new ModelAndView();
		long piazzaCount = dao.countHql("select count(*) from Notice n , NoticeReceiver nr where n.id = nr.noticeId "
				+ "and nr.receiverId=? and  (isPublic=? or isPublic=?) and nr.hasRead=0" , ThreadSessionHelper.getUser().id , 2,3);
		
		long oaCount = dao.countHql("select count(*) from Notice n , NoticeReceiver nr where n.id = nr.noticeId "
				+ "and nr.receiverId=? and  (isPublic=? or isPublic=?) and nr.hasRead=0" , ThreadSessionHelper.getUser().id , 0,1);
		
		mv.data.put("piazzaCount", piazzaCount);
		mv.data.put("oaCount", oaCount);
		return mv;
	}
	
	@WebMethod
	public ModelAndView listNotice(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		mv.data.put("page", JSONHelper.toJSON(innerListNotice(page)));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listArticle(Page<Map> page){
		ModelAndView mv = new ModelAndView();
		mv.data.put("page", JSONHelper.toJSON(innerListArticle(page)));
		return mv;
	}
	
	private Page<Map> innerListNotice(Page<Map> page){
		page.setPageSize(6);
		page = dao.findPage(page, "select n.id as id, n.title as title, n.senderId as senderId, nr.hasRead as hasRead,n.addtime as addtime , SubString(n.conts,1,50) as conts ,n.reads as reads,n.replys as replys from Notice n ,"
				+ " NoticeReceiver nr where n.id=nr.noticeId and isPublic=0 and nr.receiverId=? order by n.addtime desc", true, new Object[]{ThreadSessionHelper.getUser().id});
		for(Map map : page.getResult()){
			String conts = (String)map.get("conts");
			if(StringUtils.isNotEmpty(conts)){
				conts = HTMLSpirit.delHTMLTag(conts);
				map.put("conts", conts);
			}
		}
		return page;
	}
	
	private Page innerListArticle(Page<Map> page){
		page.setPageSize(6);
		page = dao.findPage(page, "select n.id as id, n.title as title, n.senderId as senderId, u.uname as senderName, u.avatar as senderAvatar, nr.hasRead as hasRead,n.addtime as addtime ,SubString(n.conts,1,180) as conts "
				+ ",n.zans as zans ,nr.zan as zan ,n.reads as reads,n.replys as replys , nr.hasRead as hasRead from Notice n , NoticeReceiver nr , User u where n.id=nr.noticeId and u.id=n.senderId and isPublic=1 and nr.receiverId=? order by n.addtime desc", true, new Object[]{ThreadSessionHelper.getUser().id});
		for(Map map : page.getResult()){
			String conts = (String)map.get("conts");
			if(StringUtils.isNotEmpty(conts)){
				conts = HTMLSpirit.delHTMLTag(conts);
				map.put("conts", conts);
			}
		}
		return page;
	}

	@WebMethod
	public ModelAndView index(){
		ModelAndView mv = new ModelAndView();
		
		//公告
		Page<Map> page = new Page<Map>();
		page.setPageSize(6);
		page = innerListNotice(page);
		mv.jspData.put("noticeList", page.getResult());

		//文化墙
		page = innerListArticle(page);
		mv.jspData.put("articleList", page.getResult());
		
		
		List<Site> personalList = dao.listByParams(Site.class, "from Site where uid=?",ThreadSessionHelper.getUser().id);
		List<Site> shareList = dao.listByParams(Site.class, "from Site where uid is null");
		mv.jspData.put("personalList", JSONHelper.toJSONArray(personalList));
		mv.jspData.put("shareList", JSONHelper.toJSONArray(shareList));
		mv.jspData.put("myId", ThreadSessionHelper.getUser().id);
		
		User me = ThreadSessionHelper.getUser();
//		StringBuilder auths = new StringBuilder();
//		if(me.getRole()!=null){
//			for(RoleAuthority ra : me.getRole().Authorities()){
//				auths.append(ra.name).append(",");
//			}
//		}
//		mv.jspData.put("auths", auths);
		
		mv.jspData.put("addPublicSite", me.id==1);
		return mv;
	}
	
	@Transactional
	@WebMethod(name="notice/get")
	public ModelAndView get(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		if(po.reads==null){
			po.reads = 0;
		}
		po.reads++;
		dao.saveOrUpdate(po);
		NoticeReceiver nr = dao.getUniqueByParams(NoticeReceiver.class, new String[]{"noticeId" , "receiverId"}, new Object[]{id , ThreadSessionHelper.getUser().id});
		if(nr!=null){
			nr.hasRead=1;
			dao.saveOrUpdate(nr);
		}
		User sender = dao.get(User.class, po.senderId);
		po.senderName = sender.uname;
		po.senderAvatar = sender.avatar;
		mv.data.put("user",JSONHelper.toJSON(sender));
		mv.data.put("notice", JSONHelper.toJSON(po));
		mv.data.put("nr", JSONHelper.toJSON(nr));
		if(po.zanUids!=null && po.zanUids.contains(ThreadSessionHelper.getUser().id+"")){
			mv.data.put("zan", 1);
		}else{
			mv.data.put("zan", 0);
		}
		return mv;
	}
	
	@WebMethod(name="notice/reply/list")
	public ModelAndView listReply(Page<Map> page , int noticeId){
		ModelAndView mv = new ModelAndView();
		page = dao.findPage(page, "select u.uname as replyUname, u.avatar as replyAvatar , reply.conts as conts ,reply.addtime as replytime from NoticeReply reply ,User u where u.id=reply.replyUid and noticeId=?",true, new Object[]{noticeId});
		mv.data.put("page", JSONHelper.toJSON(page));
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
		NoticeReceiver nr = dao.getUniqueByParams(NoticeReceiver.class, new String[]{"noticeId" , "receiverId"}, new Object[]{ id , ThreadSessionHelper.getUser().id});
		if(nr.zan==0){
			nr.zan=1;
			po.zans++;
			mv.data.put("zan", 1);
		}else{
			nr.zan=0;
			po.zans--;
			mv.data.put("zan", -1);
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
			List<Map> uids = dao.listAsMap("select id as uid from User where cid=?", ThreadSessionHelper.getUser().cid);
			for(Map map : uids){
				receivers+=map.get("uid")+",";
			}
		}
//		Notice po = dao.getUniqueByKeyValue(Notice.class, "title", notice.title);
//		if(po!=null){
//			throw new GException(PlatformExceptionType.BusinessException,"该标题已存在");
//		}
		User user = ThreadSessionHelper.getUser();
		notice.senderId = user.id;
		notice.addtime = new Date();
		notice.zans=0;
		notice.reads = 0;
		notice.replys = 0;
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
		User user = ThreadSessionHelper.getUser();
		StringBuilder hql = new StringBuilder("select n.id as id, n.title as title, n.senderId as senderId, u.uname as senderName, n.reads as reads, n.replys as replys, nr.hasRead as hasRead,n.addtime as addtime from Notice n ,"
				+ " NoticeReceiver nr , User u where n.id=nr.noticeId and u.id=n.senderId and isPublic=? and nr.receiverId=? ");
		
		params.add(query.isPublic);
		params.add(user.id);
		page.orderBy = "n.addtime";
		page.setPageSize(20);
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
	
	@WebMethod(name="notice/reply/add")
	@Transactional
	public ModelAndView addReply(NoticeReply reply){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, reply.noticeId);
		po.replys++;
		reply.replyUid = ThreadSessionHelper.getUser().id;
		reply.addtime = new Date();
		dao.saveOrUpdate(reply);
		dao.saveOrUpdate(po);
		return mv;
	}
	
}
