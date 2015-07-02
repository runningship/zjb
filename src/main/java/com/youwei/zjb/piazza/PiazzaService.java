package com.youwei.zjb.piazza;

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

@Module(name="/piazza")
public class PiazzaService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView getUnReadStatistic(){
		ModelAndView mv = new ModelAndView();
		StringBuilder hql = new StringBuilder("select nc.id as id,  nc.fenlei as fenlei,count(*) as total from Notice n, NoticeReceiver nr , NoticeClass nc"
				+ " where n.id=nr.noticeId and n.claid=nc.id  and nr.receiverId=? and nr.hasRead=0 group by nc.fenlei");
		List<Map> list = dao.listAsMap(hql.toString(), ThreadSessionHelper.getUser().id);
		mv.data.put("oaData", JSONHelper.toJSONArray(list));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listKnowledge(Page<Map> page , String title){
		ModelAndView mv = new ModelAndView();
		mv.data.put("page", JSONHelper.toJSON(innerListKnowledge(page , title)));
		return mv;
	}
	
	@WebMethod
	public ModelAndView listSail(Page<Map> page,String title){
		ModelAndView mv = new ModelAndView();
		mv.data.put("page", JSONHelper.toJSON(innerListSale(page,title)));
		return mv;
	}
	
	private Page<Map> innerListKnowledge(Page<Map> page , String title){
		page.setPageSize(6);
		page = dao.findPage(page, "select n.id as id, n.title as title,n.zanUids as zanUids, n.senderId as senderId, u.uname as senderName, u.avatar as senderAvatar, n.addtime as addtime ,SubString(n.conts,1,180) as conts "
				+ ",n.zans as zans ,n.reads as reads,n.replys as replys from Notice n ,User u where u.id=n.senderId and isPublic=2 and n.title like ? order by n.addtime desc", true, new Object[]{"%"+title+"%"});
		for(Map map : page.getResult()){
			String conts = (String)map.get("conts");
			if(StringUtils.isNotEmpty(conts)){
				conts = HTMLSpirit.delHTMLTag(conts);
				map.put("conts", conts);
			}
		}
		return page;
	}
	
	private Page innerListSale(Page<Map> page, String title){
		page.setPageSize(15);
		page = dao.findPage(page, "select n.id as id, n.title as title, n.senderId as senderId, n.addtime as addtime ,u.uname as senderName, SubString(n.conts,1,50) as conts ,n.reads as reads,n.replys as replys from Notice n ,User u where u.id=n.senderId "
				+ " and isPublic=3 and n.title like ? order by n.addtime desc", true, new Object[]{"%"+title+"%"});
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
		page = innerListKnowledge(page , "");
		mv.jspData.put("KnowledgeList", page.getResult());

		Page<Map> page2 = new Page<Map>();
		page2 = innerListSale(page2,"");
		mv.jspData.put("SailList", page2.getResult());
		
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
		po.reads++;
		dao.saveOrUpdate(po);
		NoticeReceiver nr = dao.getUniqueByParams(NoticeReceiver.class, new String[]{"noticeId" , "receiverId"}, new Object[]{id , ThreadSessionHelper.getUser().id});
		if(nr!=null){
			nr.hasRead=1;
		}
		User sender = dao.get(User.class, po.senderId);
		po.senderName = sender.uname;
		po.senderAvatar = sender.avatar;
		mv.data.put("notice", JSONHelper.toJSON(po));
		mv.data.put("nr", JSONHelper.toJSON(nr));
		return mv;
	}
	
	@WebMethod(name="notice/reply/list")
	public ModelAndView listReply(Page<Map> page , int noticeId){
		ModelAndView mv = new ModelAndView();
		page = dao.findPage(page, "select u.uname as replyUname, u.avatar as replyAvatar , reply.conts as conts ,reply.addtime as replytime from NoticeReply reply ,User u where u.id=reply.replyUid and noticeId=?",true, new Object[]{noticeId});
		mv.data.put("page", JSONHelper.toJSON(page));
		return mv;
	}
	
	@WebMethod(name="knowledge/edit")
	public ModelAndView editKnowledge(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		mv.jspData.put("knowledge", po);
		return mv;
	}
	
	@WebMethod(name="sale/edit")
	public ModelAndView editArticle(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		mv.jspData.put("sale", po);
		return mv;
	}
	
	@WebMethod
	public ModelAndView toggleZan(int id){
		ModelAndView mv = new ModelAndView();
		Notice po = dao.get(Notice.class, id);
		int uid = ThreadSessionHelper.getUser().id;
		if(StringUtils.isEmpty(po.zanUids)){
			po.zanUids=uid+";";
			po.zans++;
		}else{
			if(po.zanUids.contains(String.valueOf(uid)+";")){
				po.zanUids = po.zanUids.replace(uid+";", "");
				po.zans--;
			}else{
				po.zanUids +=uid+";";
				po.zans++;
			}
		}
		dao.saveOrUpdate(po);
		mv.data.put("zan", po.zans);
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
	public ModelAndView list(PiazzaQuery query, Page<Map> page){
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
