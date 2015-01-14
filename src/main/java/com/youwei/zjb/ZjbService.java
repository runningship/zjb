package com.youwei.zjb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ModelAndView;
import org.bc.web.Module;
import org.bc.web.WebMethod;

import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.im.IMServer;
import com.youwei.zjb.user.entity.Department;
import com.youwei.zjb.user.entity.User;

@Module(name="/")
public class ZjbService {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public static final int AssistantUid=-999;
	public static final int AssistantAvatar=116;
	public static final String AssistantName="小助手";
	@WebMethod
	public ModelAndView home(){
		ModelAndView mv = new ModelAndView();
		List<Map> users = dao.listAsMap("select u.avatar as avatar, u.uname as uname , d.namea as dname ,u.id as uid from User u, Department d where u.cid=? and u.lock=1 and u.did=d.id order by u.uname", ThreadSession.getUser().cid);
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
				int avatar = r.nextInt(168)+1;
				user.put("avatar", avatar);
				User u = dao.get(User.class, uid);
				u.avatar = avatar;
				dao.saveOrUpdate(u);
			}
		}
		users.remove(mySelf);
		if(me.cid==1){
			// 小助手
			Map<String,Object> ass = new HashMap<String,Object>();
			ass.put("avatar", AssistantAvatar);
			ass.put("uname", AssistantName);
			ass.put("dname", "系统服务中心");
			ass.put("uid", AssistantUid);
			ass.put("online", true);
			users.add(ass);
		}
		
		mv.jspData.put("role", me.getRole());
		mv.jspData.put("me", me);
		mv.jspData.put("cname", me.Company().namea);
		mv.jspData.put("dname", me.Department().namea);
		//按在线优先排序
		mv.jspData.put("contacts",users);
		
		Page<Map> page = new Page<Map>();
		page.setPageSize(4);
		//群组(分店)
		//加载生成群组头像
		List<Map> depts = dao.listAsMap("select d.id as did ,d.namea as dname from Department d , User u where u.did=d.id and u.id=?", me.id);
		for(Map dept : depts){
			Object did = dept.get("did");
			page = dao.findPage(page, "select avatar as avatar from User where did=?", true, new Object[]{did});
			dept.put("users", page.getResult());
			
			//群组人数统计
			long count = dao.countHql("select count(*) from User where did=? and lock=1", did);
			dept.put("totalUsers", count);
			dept.put("type", "部门");
		}
		//群组(全公司)
		Department com = me.Company();
		Map<String,Object> comp = new HashMap<String,Object>();
		comp.put("dname", com.namea);
		comp.put("did", com.id);
		comp.put("type", "公司");
		long compUsers = dao.countHql("select count(*) from User where cid=?", me.cid);
		comp.put("totalUsers", compUsers);
		page = dao.findPage(page, "select avatar as avatar from User where cid=?", true, new Object[]{me.cid});
		comp.put("users", page.getResult());
		depts.add(0,comp);
		mv.jspData.put("depts",depts);
		
		mv.jspData.put("domainName", ConfigCache.get("domainName" , "www.zhongjiebao.com"));
		mv.jspData.put("use_im", me.Company().useIm);
		
		StringBuilder auths = new StringBuilder();
		if(me.getRole()!=null){
			for(RoleAuthority ra : me.getRole().Authorities()){
				auths.append(ra.name).append(",");
			}
		}
		mv.jspData.put("auths", auths);
//		auths.toString().indexOf("cw_on");
		return mv;
	}
}
