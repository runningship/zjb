package com.youwei.zjb.task;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ThreadSession;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.KeyConstants;
import com.youwei.zjb.house.entity.House;
import com.youwei.zjb.job.PullDataHelper;

public class TaskExecutor extends Thread{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	private Task task;
	
	public TaskExecutor(Task task){
		this.task = task;
	}
	
	
	@Override
	public void run() {
		ThreadSession.setCityPY("hefei");
		task.status =KeyConstants.Task_Running;
		dao.saveOrUpdate(task);//设置任务状态为运行中
		try{
			execute();//execute方法会更新task.tatus
		}catch(Exception ex){
			task.status = KeyConstants.Task_Failed;
			task.lastError = ex.getMessage();
			ex.printStackTrace();
		}
		ThreadSession.setCityPY("hefei");
		//更新任务状态
		dao.saveOrUpdate(task);
	}


	public void execute(){
		if(StringUtils.isEmpty(task.siteUrl)){
			task.status = KeyConstants.Task_Failed;
			task.lastError = "siteUrl 不能为空";
			return;
		}
		String pageHtml = null;
		try {
			pageHtml = PullDataHelper.getHttpData(task.siteUrl, "", "utf8");
		} catch (IOException e) {
			task.status = KeyConstants.Task_Failed;
			task.lastError = "访问"+task.siteUrl+"失败";
			return;
		}
		if(StringUtils.isEmpty(task.cityPy)){
			task.status = KeyConstants.Task_Failed;
			task.lastError = "任务未设置城市";
			return;
		}
		if(pageHtml==null){
			task.status = KeyConstants.Task_Failed;
			task.lastError = "列表页面数据获取失败";
			return;
		}
		Document page = Jsoup.parse(pageHtml);
		Elements dataList = page.select(task.listSelector);
		if(dataList.isEmpty()){
			task.status = KeyConstants.Task_Failed;
			task.lastError = "列表没有找到数据,listSelector="+task.listSelector;
			return;
		}
		for(int i=dataList.size()-1;i>=0;i--){
			Element elem = dataList.get(i);
			if(isZhiDing(elem)){
				continue;
			}
			Elements link = elem.select(task.detailLink);
			if(link.isEmpty()){
				task.lastError = "列表没有找到详情页面链接,detailLinkSelector="+task.detailLink;
				continue;
			}
			String detailUrl = link.first().attr("href");
			try {
				if(task.interval>0){
					Thread.sleep(task.interval*1000);
				}
				processDetailPage(detailUrl);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | IOException e) {
				task.status = KeyConstants.Task_Failed;
				task.lastError = e.getMessage();
				LogUtil.log(Level.WARN, "任务运行失败，请检查程序", e);
				return;
			} catch(Exception ex){
				//单挑数据失败，继续
				task.lastError = ex.getMessage()+";"+detailUrl;
				LogUtil.log(Level.WARN, "抓取数据失败:"+detailUrl, ex);
			}
		}
		task.status = KeyConstants.Task_Stop;
	}
	
	private boolean isZhiDing(Element elem) {
		if(StringUtils.isEmpty(task.filterSelector)){
			return false;
		}
		for(String sel : task.filterSelector.split(";")){
			if(!elem.select(sel).isEmpty()){
				return true;
			}
		}
		return false;
	}
	private void processDetailPage(String detailUrl) throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ThreadSession.setCityPY(task.cityPy);
		if(StringUtils.isNotEmpty(task.detailPageUrlPrefix)){
			detailUrl = task.detailPageUrlPrefix+detailUrl;
		}
		House po = dao.getUniqueByKeyValue(House.class, "href", detailUrl);
		if(po!=null){
			return;
		}
		String pageHtml = PullDataHelper.getHttpData(detailUrl, "", "utf8");
//		System.out.println(pageHtml);
		Document page = Jsoup.parse(pageHtml);
		House house = new House();
		house.cid = 1;
		//信息中心
		house.did = 90;
//		house.lxing="";
		house.ztai = "4";
		house.sh = 0;
		house.seeFH = 1;
		house.seeGX = 1;
		house.seeHM = 1;
		house.dhao = "";
		house.site = task.site;
		house.href = detailUrl;
		
		String area = getDataBySelector(page , "area");
		house.area = TaskHelper.getAreaFromText(area);
		if(StringUtils.isEmpty(house.area.trim())){
			System.out.println(detailUrl);
		}
//		page.select("li:contains(小区)").first().ownText()
//		page.select("span:containsOwn(地区) :first-child")
		String quyu = getDataBySelector(page , "quyu");
		if(StringUtils.isNotEmpty(quyu)){
			quyu = quyu.replace("区", "").replace("县", "");
		}
		house.quyu = quyu;
		
		String address = getDataBySelector(page , "address");
		house.address = address;
		
		String lceng = getDataBySelector(page , "lceng");
		house.lceng = TaskHelper.getLcengFromText(lceng);
		
		String zceng = getDataBySelector(page , "zceng");
		house.zceng = TaskHelper.getZcengFromText(zceng);
		
		String hxf = getDataBySelector(page , "hxf");
		house.hxf = TaskHelper.getHxsFromText(hxf);
		
		String hxt = getDataBySelector(page , "hxt");
		house.hxt = TaskHelper.getHxtFromText(hxt);
		 
		String hxw = getDataBySelector(page , "hxw");
		house.hxw = TaskHelper.getHxwFromText(hxw);
		
		String zxiu = getDataBySelector(page , "zxiu");
		zxiu = TaskHelper.getZxiuFromText(zxiu);
		//装修情况：毛坯房
		house.zxiu = zxiu;
		
		String mji = getDataBySelector(page , "mji");
		house.mji = TaskHelper.getMjiFromText(mji);
		
		String zjia = getDataBySelector(page , "zjia");
		zjia = TaskHelper.getZjiaFromText(zjia);
		//价格：48万元
		if(StringUtils.isEmpty(zjia)){
			house.zjia = 0f;
		}else{
			house.zjia = Float.valueOf(zjia);
		}
		
		
//		house.djia = Float.valueOf(djia);
		if(house.mji!=null && house.mji!=0f){
			house.djia = house.zjia*10000/house.mji;
			house.djia = house.djia.intValue()*1.0f;
		}
		
		String lxr = getDataBySelector(page , "lxr");
		house.lxr = lxr;
		
		String tel = getDataBySelector(page , "tel");
		tel  = TaskHelper.getTelFromText(tel);
		if(tel.contains("http:")){
			house.telImg = tel;
		}else{
			house.tel = tel;
		}
		
		String dateyear = getDataBySelector(page , "dateyear");
		house.dateyear = TaskHelper.getYearFromText(dateyear);
		
		String beizhu = getDataBySelector(page , "beizhu");
		house.beizhu = beizhu;
		
//		house.dateadd = new Date();
		String pubtime = getDataBySelector(page , "pubtime");
		house.dateadd = TaskHelper.getPubtimeFromText(pubtime);
		
		dao.saveOrUpdate(house);
	}

	private String getDataBySelector(Document page ,String selectorField) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		Field field = Task.class.getField(selectorField);
		String selectors = (String)field.get(task);
		if(StringUtils.isEmpty(selectors)){
			return "";
		}
		for(String sel : selectors.split(";")){
			Elements elems = page.select(sel);
//			page.select("li:contains(位置)").first().ownText(); //楼盘 p:containsOwn(小区名称) :first-child
//			page.select("li:contains(位置) a").first().ownText();//区域
//			page.select("li:containsOwn(楼层) + li"); //楼层
//			page.select("p:containsOwn(小区名称) :first-child"); //地址
//			page.select("li:containsOwn(装修程度) + li"); //装修
//			page.select("li:containsOwn(建造年代) + li"); //年代
//			page.select("div:containsOwn(户型) + .su_con"); //户型 面积
//			page.select(".bigpri");//售价
//			page.select(".liv0 a"); //联系人
//			page.select("#t_phone script");//电话
//			page.select(".description_con :first-child");//备注
			//page.select(".mainTitle.time");//发布时间
			if(elems.isEmpty()){
				continue;
			}
			String text = elems.first().ownText();
			if(StringUtils.isEmpty(text)){
				text = elems.first().text();
			}
			if(StringUtils.isEmpty(text)){
				text = elems.first().html();
			}
			//过滤点无用字符
			text = text.replace("-", "");
			String tmp = text.replace(" ", "").replace(String.valueOf((char)160),"");
			if(StringUtils.isEmpty(tmp)){
				continue;
			}
			return text;
		}
		return "";
	}
	
	public static void main(String[] args) throws Exception{
		Task task  = new Task();
		task.cityPy = "fuyang";
		TaskExecutor te = new TaskExecutor(task);
		task.area= "p:containsOwn(小区名称) :first-child";
		te.processDetailPage("http://fy.58.com/ershoufang/21407468341007x.shtml");
	}
}
