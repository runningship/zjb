package com.youwei.test.zjb.house;

import java.util.List;

import org.bc.sdak.utils.BeanUtil;
import org.junit.Before;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.house.FavoriteService;
import com.youwei.zjb.house.entity.House;

public class TestFavoriteService {

	FavoriteService service = new FavoriteService();
	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	
	private void printResult(List<House> list){
		for(House h : list){
			System.out.println(BeanUtil.toString(h));
		}
	}
}
