package com.youwei.zjb.finace;

import java.io.IOException;
import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.job.PullDataHelper;

//http://data.10jqka.com.cn/interface/funds/ggzjl/je/desc/2/1/
//http://stockpage.10jqka.com.cn/000050/funds/
public class StockPriceJob {

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	public static void main(String[] args) throws IOException{
		StartUpListener.initDataSource();
		List<Stock> list = dao.listByParams(Stock.class, "from Stock where 1=1 ");
		for(Stock stock : list){
			System.out.println("开始处理"+stock.name+"("+stock.code+")历史数据");
			try{
				getPriceHistory(stock.code);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public static void getPriceHistory(String code) throws IOException{
		//先判断上个交易日是否已经获取，已经获取则无需重复抓取
		
		String url = "http://stockpage.10jqka.com.cn/"+code+"/funds/";
		String result = PullDataHelper.getHttpData(url, "" ,"utf8");
		Document doc = Jsoup.parse(result);
		Elements trs = doc.select(".m_table_3").select("tr");
		for(Element tr : trs){
			if(tr.select("td").size()<=0){
				continue;
			}
			try{
				StockPrice price = new StockPrice();
				price.code = code;
				price.day = tr.child(0).text();
				price.price = Float.valueOf(tr.child(1).text());
				price.zhangdie = Float.valueOf(tr.child(2).text().replace("%", ""));
				price.cashIn = Float.valueOf(tr.child(3).text());
				StockPrice po = dao.getUniqueByParams(StockPrice.class, new String[]{"code","day" },  new Object[]{price.code , price.day});
				if(po==null){
					dao.saveOrUpdate(price);
				}
			}catch(Exception ex){
				System.out.println(code+"数据异常,日期:"+tr.child(0).text());
			}
		}
	}
}
