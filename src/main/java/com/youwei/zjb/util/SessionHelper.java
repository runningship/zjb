package com.youwei.zjb.util;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.bc.sdak.SimpDaoTool;
import org.bc.web.ThreadSession;

import com.youwei.zjb.entity.RoleAuthority;
import com.youwei.zjb.user.UserHelper;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.user.entity.UserSession;

public class SessionHelper {
	
	public static void initHttpSession(HttpSession session, User user ,UserSession us) {
		session.setAttribute("user", user);
//		if(us==null){
//			us = new UserSession();
//			us.addtime = new Date();
//			us.sessionId = session.getId();
//			us.userId = user.id;
//			us.ip = (String) session.getAttribute("ip");
//		}else{
//			us.sessionId = session.getId();
//			us.updatetime = new Date();
//			us.ip = (String) session.getAttribute("ip");
//		}
		
		us = new UserSession();
		us.addtime = new Date();
		us.sessionId = session.getId();
		us.userId = user.id;
		us.ip = (String) session.getAttribute("ip");
		
		SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(us);
		session.removeAttribute("relogin");
		
		ThreadSession.getHttpSession().setAttribute("authNames", UserHelper.getAuthorityNames(user));
		StringBuilder auths = new StringBuilder();
		if(user.getRole()!=null){
			for(RoleAuthority ra : user.getRole().Authorities()){
				auths.append(ra.name).append(",");
			}
		}
		ThreadSession.getHttpSession().setAttribute("auths", auths.toString());
	}
}
