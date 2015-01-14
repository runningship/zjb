package com.youwei.zjb.job;

import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
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
import com.youwei.zjb.util.DataHelper;

public class Pull365Rent extends AbstractJob implements HouseRentJob{

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	private static Pull365Rent instance = new Pull365Rent();
	private Pull365RentAction action = new Pull365RentAction();
	private final String listPageUrl= "http://hf.rent.house365.com/district_i1/";
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		HouseRent hr = PullDataHelper.pullDetail(instance.action , "http://hf.rent.house365.com/r_859381.html" , null ,RentType.整租);
		instance.work();
	}

	private Elements getRepeats(Document doc){
		Elements infoList = doc.getElementsByClass("listPagBox").select("dd");
		return infoList;
	}
	
	public void work(){
		try{
			System.out.print(action.getSiteName()+"正在运行");
			URL url = new URL(listPageUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"GBK");
			Document doc = Jsoup.parse(result);
			Elements list = getRepeats(doc);
			if(list==null){
				LogUtil.warning("获取房源列表信息失败，等待下次重试.");
				return;
			}
			int count=0;
			for(Element e : list){
				String link = getLink(e);
				HouseRent po = dao.getUniqueByKeyValue(HouseRent.class, "href", link);
				if(po!=null){
					continue;
				}
				Date pubTime = getPubTime(e);
				HouseRent hr = PullDataHelper.pullDetail(action , link , pubTime ,getRentType(e) ,getAddress(e));
				if(hr!=null){
					dao.saveOrUpdate(hr);
				}
				count++;
			}
			System.out.println("共处理房源数:"+count);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public String getAddress(Element elem) {
		String address = elem.getElementsByClass("info").first().child(1).ownText();
		if(elem.getElementsByClass("info").first().child(1).children().isEmpty()){
			return "";
		}else {
			return address.trim();
		}
	}

	private String getLink(Element elem){
		return elem.getElementsByClass("info").first().child(0).child(0).attr("href");
	}
	
	private RentType getRentType(Element elem){
		String title = elem.getElementsByClass("info").first().child(2).ownText();
		if(StringUtils.isEmpty(title)){
			return RentType.合租;
		}
		if(title.contains("整租")){
			return RentType.整租;
		}else{
			return RentType.合租;
		}
	}
	
	private Date getPubTime(Element elem){
		String time = elem.getElementsByClass("info").first().child(3).child(0).text();
		String text = time.replace("更新", "").trim();
		text ="2015-"+text;
		try {
			return DataHelper.sdf3.parse(text);
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}

	@Override
	public String getJobName() {
		return action.getSiteName();
	}
	
}
