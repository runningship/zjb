package com.youwei.zjb.job;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
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
import com.youwei.zjb.im.IMServer;

public class Pull58Rent extends AbstractJob implements HouseRentJob{

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	private static Pull58Rent instance = new Pull58Rent();
	private Pull58RentAction action = new Pull58RentAction();
	private final String listPageUrl= "http://hf.58.com/chuzu/0/";
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		HouseRent hr = PullDataHelper.pullDetail(instance.action , "http://hf.58.com/zufang/22143062947980x.shtml" ,  new Date(), RentType.整租 , null);
//		int a=0;
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
		String link ="";
		HouseRent hr = null;
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
			
			for(int i=list.size()-1;i>=0;i--){
				Element e = list.get(i);
				Elements duns = e.getElementsByClass("dun");
				if(duns!=null && duns.isEmpty()==false){
					continue;
				}
				Elements dings = e.getElementsByClass("ding");
				if(dings!=null && dings.isEmpty()==false){
					continue;
				}
				link = getLink(e);
				if(StringUtils.isEmpty(link)){
					LogUtil.warning("获取房源链接失败:"+e.html());
					continue;
				}
				if(link.contains("anjuke")){
					//过滤掉安居客的
					continue;
				}
				//过滤掉参数
				URL linkUrl;
				try {
					linkUrl = new URL(link);
					link = linkUrl.toExternalForm().replace("?"+linkUrl.getQuery(),"");
				} catch (MalformedURLException ex) {
					ex.printStackTrace();
				}
				HouseRent po = dao.getUniqueByKeyValue(HouseRent.class, "href", link);
				if(po!=null){
					continue;
				}
				Date pubTime = PullDataHelper.getPubTime(e);
				hr = PullDataHelper.pullDetail(action , link , pubTime ,getRentType(e) , null);
				if(hr!=null){
					dao.saveOrUpdate(hr);
					count++;
				}
				Thread.sleep(this.getDetailPageInterval());
			}
//			IMServer.sendMsgToUser(PullDataHelper.errorReportUserId, "本次共处"+action.getSiteName()+"理房源数:"+count);
		}catch(Exception ex){
			StackTraceElement stack = ex.getStackTrace()[0];
			String msg = action.getSiteName()+"扫网任务失败，href="+link+",at"+stack.getClassName()+" line "+stack.getLineNumber()+","+stack.getMethodName();
//			IMServer.sendMsgToUser(PullDataHelper.errorReportUserId, msg);
			LogUtil.log(Level.WARN, "58扫网任务失败", ex);
		}
	}
	
	private String getLink(Element elem){
		return elem.getElementsByClass("qj-rentd").first().getElementsByTag("a").first().attr("href");
	}
	
	private RentType getRentType(Element elem){
		String title =elem.getElementsByClass("qj-rentd").first().getElementsByTag("a").first().text();
		if(StringUtils.isEmpty(title)){
			return RentType.合租;
		}
		if(title.contains("整租")){
			return RentType.整租;
		}else{
			return RentType.合租;
		}
	}
	

	@Override
	public String getJobName() {
		return action.getSiteName();
	}
	
}
