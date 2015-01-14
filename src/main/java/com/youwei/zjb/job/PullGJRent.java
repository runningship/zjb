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

public class PullGJRent extends AbstractJob implements HouseRentJob{

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	private static PullGJRent instance = new PullGJRent();
	//默认60秒
	private PullGJRentAction action = new PullGJRentAction();
	private final String listPageUrl= "http://hf.ganji.com/fang1/";
	
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		HouseRent hr = PullDataHelper.pullDetail(job.action , "http://hf.ganji.com/fang1/1350251348x.htm" , null ,RentType.整租);
//		dao.saveOrUpdate(hr);
		instance.work();
	}
	
	private Elements getRepeats(Document doc){
		Elements infoList = doc.getElementsByClass("listBox").select("li");
		return infoList;
	}
	
	public void work(){
		try{
			System.out.print(action.getSiteName()+"正在运行");
//			CloseableHttpClient client = HttpClientBuilder.create().build(); 
//			RequestConfig requestConfig = RequestConfig.custom()  
//		    .setConnectionRequestTimeout(10000).setConnectTimeout(10000)
//		    .setSocketTimeout(10000).build();
//		    HttpGet request = new HttpGet(listPageUrl);
//		    request.setConfig(requestConfig);
//			CloseableHttpResponse response = client.execute(request);
//			String result = EntityUtils.toString(response.getEntity() , "utf-8");
//			response.close();
			
			URL url = new URL(listPageUrl);
			URLConnection conn = url.openConnection();
			conn.getHeaderField("Set-Cookie");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			Document doc = Jsoup.parse(result);
			if(result.contains("您的访问速度太快了")){
				IMServer.sendMsgToUser(PullDataHelper.errorReportUserId, "赶集又要输入验证码了。。");
				return;
			}
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
				Elements hots = e.getElementsByClass("ico-hot");
				if(hots!=null && hots.isEmpty()==false){
					continue;
				}
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
				if(count>5){
					break;
				}
				Thread.sleep(this.getDetailPageInterval());
			}
			System.out.println("共处理"+action.getSiteName()+"房源数:"+count);
		}catch(Exception ex){
			String msg = action.getSiteName()+"扫网任务失败，reason="+ex.getMessage();
			IMServer.sendMsgToUser(PullDataHelper.errorReportUserId, msg);
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

	@Override
	public String getJobName() {
		return action.getSiteName();
	}

}
