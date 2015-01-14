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

public class Pull58Rent extends AbstractJob implements HouseRentJob{

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	private static Pull58Rent instance = new Pull58Rent();
	private Pull58RentAction action = new Pull58RentAction();
	private final String listPageUrl= "http://hf.58.com/chuzu/0/";
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		job.pullDetail("http://hf.58.com/zufang/20614401441800x.shtml", new Date(), RentType.整租);
		instance.work();
	}

	private Elements getRepeats(Document doc){
		Element table = null;
		Element infoList = doc.getElementById("infolist");
		if(infoList==null){
			return null;
		}
		for(Element elem : infoList.children()){
			if("table".equals(elem.tagName())){
				table = elem;
				break;
			}
		}
		if(table!=null){
			Elements trs = table.getElementsByTag("tr");
			return trs;
		}
		return null;
	}
	
	public void work(){
		try{
			System.out.print(action.getSiteName()+"正在运行");
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
				Elements duns = e.getElementsByClass("dun");
				if(duns!=null && duns.isEmpty()==false){
					continue;
				}
				Elements dings = e.getElementsByClass("ding");
				if(dings!=null && dings.isEmpty()==false){
					continue;
				}
				String link = getLink(e);
				HouseRent po = dao.getUniqueByKeyValue(HouseRent.class, "href", link);
				if(po!=null){
					continue;
				}
				Date pubTime = getPubTime(e);
				HouseRent hr = PullDataHelper.pullDetail(action , link , pubTime ,getRentType(e));
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
	
	private String getLink(Element elem){
		return elem.getElementsByTag("h1").first().getElementsByTag("a").first().attr("href");
	}
	
	private RentType getRentType(Element elem){
		String title = elem.getElementsByTag("h1").first().getElementsByTag("a").first().text();
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
		try{
			String text = elem.getElementsByClass("qj-renaddr").first().ownText();
			if(StringUtils.isEmpty(text)){
				return null;
			}
			for(String str : text.split(" ")){
				if(str.contains("分钟") || str.contains("小时")){
					text = str;
					break;
				}
			}
			if(text.endsWith("分钟")){
				text = text.replace("分钟","");
				Calendar ca = Calendar.getInstance();
				ca.add(Calendar.MINUTE, Integer.valueOf(text));
				return ca.getTime();
			}else if (text.endsWith("小时")){
				text = text.replace("小时","");
				Calendar ca = Calendar.getInstance();
				ca.add(Calendar.HOUR_OF_DAY, Integer.valueOf(text));
				return ca.getTime();
			}
		}catch(Exception ex){
			LogUtil.warning("获取发布时间失败,"+ elem.outerHtml());
			return null;
		}
		
		return null;
	}

	@Override
	public String getJobName() {
		return action.getSiteName();
	}
	
}
