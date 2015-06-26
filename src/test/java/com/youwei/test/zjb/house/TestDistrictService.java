package com.youwei.test.zjb.house;

import org.bc.sdak.SimpDaoTool;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.house.DistrictService;
import com.youwei.zjb.house.entity.District;

public class TestDistrictService {

	DistrictService service = new DistrictService();
	
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	@Test
	public void testUpdate(){
		District dis = SimpDaoTool.getGlobalCommonDaoService().get(District.class, 367);
		dis.uid=-1;
		service.update(dis);
	}
	
	public static void main(String[] args){
		String str = " 158056 00755  18100515581  ";
		for(int i=0;i<str.length();i++){
			System.out.println(i+":"+str.codePointAt(i));
		}
	}
}
