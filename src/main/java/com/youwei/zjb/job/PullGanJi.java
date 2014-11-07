package com.youwei.zjb.job;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.utils.LogUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.house.QuYu;
import com.youwei.zjb.house.ZhuangXiu;
import com.youwei.zjb.house.entity.ExpHouse;
import com.youwei.zjb.util.DataHelper;

public class PullGanJi {

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		pull("http://hf.ganji.com/fang5/1199668148x.htm");
		start();
	}
	
	public static void startJob(){
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				start();
//				Calendar cal = Calendar.getInstance();
//				if(cal.get(Calendar.HOUR_OF_DAY)>=20 && cal.get(Calendar.HOUR_OF_DAY)<=7){
//					start();
//				}else{
//					LogUtil.info("58 pulling is on");
//				}
			}
			
		}, 1000*60,1000*60*5);
	}
	private static void start(){
		Random r = new Random();
		try{
			String urlStr = "http://hf.ganji.com/fang5/a1/";
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.getRequestProperties();
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			Document doc = Jsoup.parse(result);
			Elements list = doc.getElementsByClass("list-img");
			int count=0;
			for(Element e : list){
				//热
				Elements hot = e.getElementsByClass("ico-hot");
				if(!hot.isEmpty()){
					continue;
				}
				//安全
				Elements security = e.getElementsByClass("ico-security");
				if(!security.isEmpty()){
					continue;
				}
				//顶
				Elements ding = e.getElementsByClass("ico-stick-yellow");
				if(!ding.isEmpty()){
					continue;
				}
				
				Elements title = e.getElementsByClass("list-info-title");
				if(title.isEmpty()){
					continue;
				}
				String href = title.attr("href");
				Elements offset = e.getElementsMatchingOwnText("分钟前");
				if(offset==null || offset.isEmpty()){
					offset = e.getElementsMatchingOwnText("小时前");
				}
				String[] arr = offset.first().text().split("/");
				String offsetStr= arr[arr.length-1];
				ExpHouse po = dao.getUniqueByKeyValue(ExpHouse.class, "href", href);
				if(po!=null){
					continue;
				}
				pull("http://hf.ganji.com/"+href , offsetStr);
				Thread.sleep(1000*(10+r.nextInt(4)));
				count++;
			}
			System.out.println("共处理房源数:"+count);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void pull(String hlink, String timeOffset){
		System.out.println("pulling "+hlink);
		ExpHouse ehouse = new ExpHouse();
		ehouse.href = hlink;
		ehouse.timeOffset = timeOffset;
		ehouse.site="赶集";
		try{
			URL url = new URL(hlink);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			Document doc = Jsoup.parse(result);
//			Element titleInfo = doc.getElementsByClass("title-info-l").get(0);
//			Elements fbTime = titleInfo.getElementsMatchingOwnText("发布");
//			String fbTimeStr = "2014-"+fbTime.first().getElementsByTag("i").text()+":00";
//			ehouse.pubtime = DataHelper.sdf.parse(fbTimeStr);
			Elements list = doc.getElementsByClass("basic-info");
			Element basicInfo = list.get(0);
			Elements price = basicInfo.getElementsByClass("basic-info-price");
			if(price!=null && price.first()!=null){
				//总价
				ehouse.zjia = price.first().text();
				System.out.println("zjia="+price.first().text());
			}
			Elements gaik = basicInfo.getElementsMatchingOwnText("概况");
			if(gaik!=null && gaik.first()!=null){
				String str = gaik.first().parent().ownText();
			    String[] arr = str.split(" ");
			    for(int i=0;i<arr.length;i++){
			    	if(arr[i].contains("年房龄")){
			    		String ageStr = arr[i].replace("年房龄", "");
			    		Integer age = Integer.valueOf(ageStr);
			    		ehouse.dateyear = String.valueOf(2014-age);
			    		System.out.println("建筑年代="+ehouse.dateyear);
			    	}
			    }
			}
			Elements hxing = basicInfo.getElementsMatchingOwnText("户型");
			if(hxing!=null && hxing.first()!=null){
				String str=hxing.first().parent().ownText();
				String tmp = "";
				for(int i=0;i<str.length();i++){
					if(str.charAt(i)=='室'){
						ehouse.hxf = tmp;
						tmp="";
					}else if(str.charAt(i)=='厅'){
						ehouse.hxt = tmp;
						tmp="";
					}else if(str.charAt(i)=='卫'){
						ehouse.hxw = tmp;
						tmp="";
					}else{
						tmp +=str.charAt(i);
					}
				}
				System.out.println("户型="+ehouse.hxf+"-"+ehouse.hxt+"-"+ehouse.hxw);
			}
			Elements quyu = basicInfo.getElementsMatchingOwnText("位置");
			if(quyu!=null && quyu.first()!=null){
				String str=quyu.first().nextElementSibling().nextElementSibling().text();
				String[] arr = str.split("-");
				ehouse.quyu = arr[0]+"区";
				System.out.println("quyu="+ehouse.quyu);
			}
			Elements lceng = basicInfo.getElementsMatchingOwnText("楼层");
			if(lceng!=null && lceng.first()!=null){
				String str=lceng.first().parent().ownText();
				String[] arr = str.split("/");
				ehouse.lceng = arr[0];
				if(arr.length>1){
					ehouse.zceng = arr[1];
				}
			}
			Elements area = basicInfo.getElementsMatchingOwnText("小区");
			if(area!=null && area.first()!=null){
				String str=area.first().nextElementSibling().ownText();
				if(StringUtils.isEmpty(str)){
					str = area.first().parent().ownText();
				}
				ehouse.area = str;
				System.out.println("area="+ehouse.area);
			}
			Elements address = basicInfo.getElementsByClass("addr-area");
			if(address!=null && address.first()!=null){
				ehouse.address = address.first().attr("title");
				System.out.println("address="+ehouse.address);
			}
			Elements lxr = basicInfo.getElementsByClass("fc-4b");
			if(lxr!=null && lxr.first()!=null){
				ehouse.lxr = lxr.first().text();
				System.out.println("lxr="+ehouse.lxr);
			}
			Elements phone = basicInfo.getElementsByClass("contact-mobile");
			if(phone!=null && phone.first()!=null){
				ehouse.tel = phone.first().text().replace(" ", "");
				System.out.println("tel="+ehouse.tel);
			}
			
			Element summary = doc.getElementsByClass("fang-summary").get(0);
			Elements mji = summary.getElementsMatchingOwnText("建筑面积");
			String mjiStr = mji.first().parent().ownText();
			if(mjiStr!=null){
				ehouse.mji = mjiStr.replace("㎡", "");
				System.out.println("mji="+ehouse.mji);
			}
			Elements zxiu = summary.getElementsMatchingOwnText("装修程度");
			if(zxiu!=null && zxiu.first()!=null){
				ehouse.zxiu = zxiu.first().parent().ownText();
				System.out.println("zxiu="+ehouse.zxiu);
			}
			
			ehouse.finish=0;
//			save(ehouse);
		}catch(SocketTimeoutException ex){
			System.err.println("请求超时");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	private static void save(ExpHouse ehouse) {
		ehouse.code=ehouse.area+"-"+ehouse.zceng+"-"+ehouse.lceng+"-"+ehouse.hxf+"-"+ehouse.hxt+"-"+ehouse.hxw;
		ExpHouse po = dao.getUniqueByKeyValue(ExpHouse.class, "code", ehouse.code);
		if(po==null){
			ehouse.repeat=0;
		}else{
			ehouse.repeat=1;
		}
		
		if("简单装修".equals(ehouse.zxiu)){
			ehouse.zxiu = ZhuangXiu.简装.toString();
		}else if("中等装修".equals(ehouse.zxiu)){
			ehouse.zxiu = ZhuangXiu.中装.toString();
		}else if("精装修".equals(ehouse.zxiu)){
			ehouse.zxiu = ZhuangXiu.精装.toString();
		}else if("豪华装修".equals(ehouse.zxiu)){
			ehouse.zxiu = ZhuangXiu.豪装.toString();
		}else if("毛坯".equals(ehouse.zxiu)){
			ehouse.zxiu = ZhuangXiu.毛坯.toString();
		}
		ehouse.addtime = new Date();
		dao.saveOrUpdate(ehouse);
	}
}
