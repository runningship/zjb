package com.youwei.zjb;

import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.im.IMServer;
import com.youwei.zjb.user.entity.User;

@Module(name="/")
public class ZjbService {

CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@WebMethod
	public ModelAndView home(){
		ModelAndView mv = new ModelAndView();
		List<Map> users = dao.listAsMap("select u.avatar as avatar, u.uname as uname , d.namea as dname ,u.id as uid from User u, Department d where cid=? and u.did=d.id order by u.uname", ThreadSession.getUser().cid);
		//随机头像
		Random  r = new Random();
		User me = ThreadSession.getUser();
		Map mySelf = null;
		for(Map user : users){
			Integer uid = (Integer)user.get("uid");
			if(me.id.equals(uid)){
				mySelf = user;
				continue;
			}
			if(IMServer.isUserOnline(me.domain, uid)){
				user.put("online", true);
			}else{
				user.put("online", false);
			}
			if(user.get("avatar")==null){
				int avatar = r.nextInt(95)+1;
				user.put("avatar", avatar);
				User u = dao.get(User.class, uid);
				u.avatar = avatar;
				dao.saveOrUpdate(u);
			}
		}
		users.remove(mySelf);
		mv.jspData.put("role", me.getRole());
		mv.jspData.put("me", me);
		mv.jspData.put("cname", me.Company().namea);
		mv.jspData.put("dname", me.Department().namea);
		//按在线优先排序
		mv.jspData.put("contacts",users);
		return mv;
	}
}
