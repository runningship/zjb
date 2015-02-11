package com.youwei.zjb.finace;

import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.StartUpListener;

public class StockTuiJian {

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
		List<Stock> list = dao.listByParams(Stock.class, "from Stock where 1=1 ");
		Page<StockPrice> page = new Page<StockPrice>();
		page.setPageSize(20);
		for(Stock stock : list){
			page = dao.findPage(page, "from StockPrice where code=? order by day ", stock.code);
			if(page.result.size()<20){
				continue;
			}
			for(int i=0;i<page.getResult().size();i++){
				StockPrice price = page.getResult().get(i);
				if("20150210".equals(price.day)){
					if(i<6){
						System.out.println(stock.name+":"+stock.code);
					}
				}
			}
		}
	}
}
