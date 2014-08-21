package com.youwei.test.zjb.user;

import java.util.List;
import java.util.Map;

import org.bc.sdak.Page;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.user.UserQuery;
import com.youwei.zjb.user.UserService;
import com.youwei.zjb.util.DebugHelper;

public class TestUserService {

	UserService us = new UserService();
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void TestGetUserTree(){
	}
	
	@Test
	public void testUserList(){
		UserQuery query = new UserQuery();
//		query.name = "你";
		query.gender = 0;
		query.ageStart=25;
		query.ageEnd = 25;
		ModelAndView mv = us.list(query, new Page<Map>());
		DebugHelper.printResult((List<?>) mv.data.get("users"));
	}
}
