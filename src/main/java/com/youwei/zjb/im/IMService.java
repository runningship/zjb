package com.youwei.zjb.im;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.ThreadSession;
import com.youwei.zjb.im.entity.Contact;
import com.youwei.zjb.im.entity.Message;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.JSONHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Module(name="/im/")
public class IMService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView getHistory(Page<Message> page , Integer contactId) {
		ModelAndView mv = new ModelAndView();
		page.setPageSize(5);
		Integer myId = ThreadSession.getUser().id;
		page = dao.findPage(page ,"from Message where (senderId=? and receiverId=?) or (senderId=? and receiverId=?) order by sendtime desc", myId , contactId , contactId , myId);
		mv.data.put("history", JSONHelper.toJSONArray(page.getResult()));
		return mv;
	}
	public List<Map> countUnReadMessage(int userId){
		//需要过滤掉不在自己好友列表里面的
		String hql = "select count(*) as total, m.senderId as senderId from Message m,Contact c where c.contactId=m.senderId and c.ownerId=? "
				+ " and m.hasRead=0 and m.receiverId=? group by senderId";
		List<Map> list = dao.listAsMap(hql, userId , userId);
		return list;
	}
	
	@WebMethod
	public ModelAndView close(){
		ModelAndView mv = new ModelAndView();
		try {
			IMServer.forceStop();
		} catch (Exception e) {
			throw new RuntimeException("try to close IM Server fail.",e);
		}
		mv.data.put("msg","IM Server closed.");
		return mv;
	}
	@WebMethod
	public ModelAndView status(){
		ModelAndView mv = new ModelAndView();
		JSONArray arr = new JSONArray();
		String domian = ThreadSession.getDomain();
		for(Integer uid : IMServer.conns.get(domian).keySet()){
			User u = dao.get(User.class, uid);
			if(u!=null){
				arr.add(u.uname);
			}
		}
		mv.data.put("onlien users", arr);
		return mv;
	}
	@WebMethod
	public ModelAndView start(){
		ModelAndView mv = new ModelAndView();
		try {
			IMServer.startUp();
		} catch (Throwable e) {
			throw new RuntimeException("try to start IM Server fail.",e);
		}
		mv.data.put("msg","IM Server started.");
		return mv;
	}
	
	@WebMethod
	public ModelAndView allAvatars(){
		ModelAndView mv = new ModelAndView();
		JSONArray arr = new JSONArray();
		for(int i=1;i<90;i++){
			JSONObject jobj = new JSONObject();
			jobj.put("avatarId", i);
			arr.add(jobj);
		}
		mv.data.put("avatars",arr);
		return mv;
	}
	
	@WebMethod
	public ModelAndView setAvatar(int userId,int avatarId){
		ModelAndView mv = new ModelAndView();
		User user = dao.get(User.class, userId);
		user.avatar = avatarId;
		dao.saveOrUpdate(user);
		return mv;
	}
	
	
	@WebMethod
	public ModelAndView addContact(Contact contact){
		ModelAndView mv = new ModelAndView();
		Contact po = dao.getUniqueByParams(Contact.class, new String[]{"ownerId", "contactId"}, new Object[]{contact.ownerId,contact.contactId});
		if(po==null){
			dao.saveOrUpdate(contact);
		}
		
		Contact po2 = dao.getUniqueByParams(Contact.class, new String[]{"ownerId", "contactId"}, new Object[]{contact.contactId,contact.ownerId});
		if(po2==null){
			Contact contact2 = new Contact();
			contact2.ownerId = contact.contactId;
			contact2.contactId = contact.ownerId;
			dao.saveOrUpdate(contact2);
		}
		mv.data.put("result", new JSONObject());
		return mv;
	}
	
	@WebMethod
	public ModelAndView delContact(int ownerId , int contactId){
		ModelAndView mv = new ModelAndView();
		dao.execute("delete from Contact where ownerId=? and contactId=?", ownerId , contactId);
		mv.data.put("result", 0);
		return mv;
	}
	
	@WebMethod
	public ModelAndView setRead(int myId, int contactId){
		ModelAndView mv = new ModelAndView();
		String hql = "update Message set hasRead=1 where senderId=? and receiverId=? and hasRead=0";
		dao.execute(hql, contactId, myId);
		mv.data.put("result", 0);
		return mv;
	}
}
