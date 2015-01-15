package com.youwei.zjb.job;

import java.net.URL;
import java.net.URLConnection;

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
import com.youwei.zjb.im.IMServer;

public class PullAJKRent extends AbstractJob implements HouseRentJob{

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	private static PullAJKRent instance = new PullAJKRent();
	//默认60秒
	private PullAJKRentAction action = new PullAJKRentAction();
	private final String listPageUrl= "http://hf.zu.anjuke.com/fangyuan/l2/";
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		HouseRent hr = PullDataHelper.pullDetail(instance.action ,"http://hf.zu.anjuke.com/gfangyuan/37789311?from=Filter_16" , null ,RentType.整租,null);
//		int a=0;
//		dao.saveOrUpdate(hr);
		instance.work();
	}
	
	private Elements getRepeats(Document doc){
		Elements infoList = doc.getElementsByClass("main_content").select("dl");
		return infoList;
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
				String link = getLink(e);
				HouseRent po = dao.getUniqueByKeyValue(HouseRent.class, "href", link);
				if(po!=null){
					continue;
				}
				HouseRent hr = PullDataHelper.pullDetail(action , link , null ,getRentType(e),null);
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
		String href = elem.attr("link");
		return href;
	}
	
	private RentType getRentType(Element elem){
		String title = elem.getElementsByClass("p_tag").text();
		if(StringUtils.isEmpty(title)){
			return RentType.合租;
		}
		if(title.contains("合租")){
			return RentType.合租;
		}else{
			return RentType.整租;
		}
	}

	@Override
	public String getJobName() {
		return action.getSiteName();
	}

}
