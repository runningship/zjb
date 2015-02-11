package com.youwei.zjb.finace;

import java.io.IOException;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.job.PullDataHelper;

public class StockListJob {

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	public static void main(String[] args) throws IOException{
		StartUpListener.initDataSource();
		for(int i=0;i<=53;i++){
			getPage(i);
		}
	}
	
	public static void getPage(int page) throws IOException{
	String url = "http://data.10jqka.com.cn/interface/funds/ggzjl/code/desc/"+page+"/1/";
	String result = PullDataHelper.getHttpData(url, "" ,"utf8");
	JSONObject json = JSONObject.fromObject(result);
	
	Document table = Jsoup.parse("<table>"+json.getString("data")+"</table>");
	Element tbody = table.getElementsByTag("tbody").first();
	Elements trs = tbody.getElementsByTag("tr");
	for(Element tr : trs){
		Stock stock = new Stock();
		stock.code = tr.child(1).text();
		stock.name = tr.child(2).text();
		StockPrice po = dao.getUniqueByParams(StockPrice.class, new String[]{"code" },  new Object[]{stock.code});
		if(po==null){
			dao.saveOrUpdate(stock);
			System.out.println("新增股票,代码:"+tr.child(1).text() +",名称:"+tr.child(2).text()+",价格:"+tr.child(3).text()+",涨跌幅:"+tr.child(4).text()+",资金:"+tr.child(8).text());
		}
	}
}
}
