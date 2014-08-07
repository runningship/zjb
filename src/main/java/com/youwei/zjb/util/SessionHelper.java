package com.youwei.zjb.util;

import javax.servlet.http.HttpSession;

import com.youwei.zjb.user.entity.User;

public class SessionHelper {
	
	public static void initHttpSession(HttpSession session, User user) {
		session.setAttribute("user", user);
	}
}
