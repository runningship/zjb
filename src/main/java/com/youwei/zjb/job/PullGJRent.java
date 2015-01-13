package com.youwei.zjb.job;

import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.utils.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.house.RentType;
import com.youwei.zjb.house.entity.HouseRent;

public class PullGJRent{

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	
	private PullGJRentAction action = new PullGJRentAction();
	private final String listPageUrl= "http://hf.ganji.com/fang1/a1/";
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
		PullGJRent job = new PullGJRent();
		HouseRent hr = PullDataHelper.pullDetail(job.action , "http://hf.ganji.com/fang1/1321622679x.htm" , null ,RentType.整租);
//		job.start();
		dao.saveOrUpdate(hr);
		int a=0;
	}
	
	private Elements getRepeats(Document doc){
		Elements infoList = doc.getElementsByClass("listBox").select("li");
		return infoList;
	}
	private void start(){
		try{
			URL url = new URL(listPageUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			Document doc = Jsoup.parse(result);
			Elements list = getRepeats(doc);
			if(list==null){
				LogUtil.warning("获取房源列表信息失败，等待下次重试.");
				return;
			}
			int count=0;
			for(Element e : list){
				Elements duns = e.getElementsByClass("ico-security");
				if(duns!=null && duns.isEmpty()==false){
					continue;
				}
				Elements dings = e.getElementsByClass("ico-stick-yellow");
				if(dings!=null && dings.isEmpty()==false){
					continue;
				}
				String link = getLink(e);
				HouseRent po = dao.getUniqueByKeyValue(HouseRent.class, "href", link);
				if(po!=null){
					continue;
				}
				HouseRent hr = PullDataHelper.pullDetail(action , link , null ,getRentType(e));
//				if(hr!=null){
//					dao.saveOrUpdate(hr);
//				}
//				count++;
			}
			System.out.println("共处理房源数:"+count);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private String getLink(Element elem){
		String href = elem.getElementsByClass("list-info-title").attr("href");
		return "http://hf.ganji.com"+href;
	}
	
	private RentType getRentType(Element elem){
		String title = elem.getElementsByClass("list-info-title").text();
		if(StringUtils.isEmpty(title)){
			return RentType.合租;
		}
		if(title.contains("合租")){
			return RentType.合租;
		}else{
			return RentType.整租;
		}
	}
	
}
