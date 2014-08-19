package com.youwei.test.zjb.house;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.bc.sdak.Page;
import org.bc.sdak.utils.BeanUtil;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.house.GenJinQuery;
import com.youwei.zjb.house.GenJinService;
import com.youwei.zjb.house.entity.GenJin;
import com.youwei.zjb.util.DebugHelper;

public class TestGenJinService {

	@Before
	public void init(){
		StartUpListener.initDataSource();
	}

	@Test
	public void testQueryByDate(){
		GenJinQuery query = new GenJinQuery();
		query.addtimeStart = "2013-08-21";
		query.addtimeEnd = "2013-08-23";
		GenJinService service = new GenJinService();
	}
	
	@Test
	public void testAdd(){
		GenJinService service = new GenJinService();
		GenJin gj = new GenJin();
		gj.conts="测试";
		gj.addtime = new Date();
		service.add(gj);
		
	}
	
	private void printResult(List<GenJin> list){
		for(GenJin gj : list){
			System.out.println(BeanUtil.toString(gj));
		}
	}
}
