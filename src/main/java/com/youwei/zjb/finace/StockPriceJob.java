package com.youwei.zjb.finace;

import java.io.IOException;
import java.util.Date;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import net.sf.json.JSONObject;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.job.PullDataHelper;
import com.youwei.zjb.util.DataHelper;

//http://data.10jqka.com.cn/interface/funds/ggzjl/je/desc/2/1/
//http://stockpage.10jqka.com.cn/600698/funds/
public class StockPriceJob {

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	public static void main(String[] args) throws IOException{
		StartUpListener.initDataSource();
		String code = "600698";
		String url = "http://stockpage.10jqka.com.cn/"+code+"/funds/";
		String result = PullDataHelper.getHttpData(url, "" ,"utf8");
		Document doc = Jsoup.parse(result);
		Elements trs = doc.select(".m_table_3").select("tr");
		for(Element tr : trs){
			if(tr.select("td").size()<=0){
				continue;
			}
			StockPrice stock = new StockPrice();
			stock.code = code;
			stock.day = tr.child(0).text();
			stock.price = Float.valueOf(tr.child(1).text());
			stock.zhangdie = Float.valueOf(tr.child(2).text().replace("%", ""));
			stock.cashIn = Float.valueOf(tr.child(3).text());
			StockPrice po = dao.getUniqueByParams(StockPrice.class, new String[]{"code","day" },  new Object[]{stock.code , stock.day});
			if(po==null){
				dao.saveOrUpdate(stock);
			}
		}
	}
	
//	public static void getPage(int page) throws IOException{
//		String url = "http://data.10jqka.com.cn/interface/funds/ggzjl/code/desc/"+page+"/1/";
//		String result = PullDataHelper.getHttpData(url, "" ,"utf8");
//		JSONObject json = JSONObject.fromObject(result);
//		
//		Document table = Jsoup.parse("<table>"+json.getString("data")+"</table>");
//		Element tbody = table.getElementsByTag("tbody").first();
//		Elements trs = tbody.getElementsByTag("tr");
//		for(Element tr : trs){
//			System.out.println("代码:"+tr.child(1).text() +",名称:"+tr.child(2).text()+",价格:"+tr.child(3).text()+",涨跌幅:"+tr.child(4).text()+",资金:"+tr.child(8).text());
//			StockPrice stock = new StockPrice();
//			stock.code = tr.child(1).text();
//			stock.price = Float.valueOf(tr.child(3).text());
//			stock.cashIn = Float.valueOf(tr.child(8).text());
//			stock.zhangdie = Float.valueOf(tr.child(4).text().replace("%", ""));
//			stock.day = DataHelper.sdf6.format(new Date());
//			StockPrice po = dao.getUniqueByParams(StockPrice.class, new String[]{"code","day" },  new Object[]{stock.code , stock.day});
//			if(po!=null){
//				dao.saveOrUpdate(stock);
//			}
//		}
//	}
}
