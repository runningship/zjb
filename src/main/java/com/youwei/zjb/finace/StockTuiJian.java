package com.youwei.zjb.finace;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.StartUpListener;

public class StockTuiJian {

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
		//所有非创业板股票
		List<Stock> list = dao.listByParams(Stock.class, "from Stock where code not like '3%' ");
		Page<StockPrice> page = new Page<StockPrice>();
		page.setPageSize(10);
		for(Stock stock : list){
			page = dao.findPage(page, "from StockPrice where code=? order by day desc", stock.code);
			if(page.result.size()<10){
				continue;
			}
			
//			int daysOfDie = 0;
//			float zhangdie = 0 ;
			findByNewLowPrice(stock,page.getResult());
//				zhangdie+=price.zhangdie;
//				if(i+1<page.getResult().size()){
//					if(price.price<page.getResult().get(i+1).price){
//						daysOfDie++;
//					}
//				}
//			}
//			if(zhangdie<=0.3){
//				System.out.println(stock.code);
//			}
		}
	}
	
	private static void findByNewLowPrice(Stock stock,List<StockPrice> list){
		String day = list.get(0).day;
		Collections.sort(list);
		if(day.equals(list.get(0).day)){
//			System.out.println(stock.code);
		}else if(day.equals(list.get(1).day)){
			System.out.println(stock.code);
		}else if(day.equals(list.get(2).day)){
//			System.out.println(stock.code);
		}
	}
}
