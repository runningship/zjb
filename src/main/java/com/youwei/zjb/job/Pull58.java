package com.youwei.zjb.job;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Pull58 {

	public static void main(String[] args){
//		pull("http://hf.58.com/ershoufang/19254087071361x.shtml?PGTID=14104274032260.3141201017424464&ClickID=7");
		start();
	}
	
	private static void start(){
		try{
			String urlStr = "http://hf.58.com/ershoufang/0/h1/?ClickID=1";
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			Document doc = Jsoup.parse(result);
			Elements list = doc.getElementsByTag("td");
			for(Element e : list){
				if(!"t".equals(e.attr("class"))){
					continue;
				}
				Elements jjr = e.getElementsByClass("qj-listjjr");
				if(jjr==null){
					continue;
				}
				if(jjr.html().contains("个人")){
					Elements ding = e.getElementsByClass("ding");
					if(ding!=null && ding.size()>0){
						continue;
					}
					Elements h1 = e.getElementsByTag("h1");
//					System.out.println(e.getElementsByClass("qj-listleft").text());
					Elements link = h1.first().getElementsByTag("a");
//					System.out.println(link.first().attr("href"));
					pull(link.first().attr("href"));
				}
			}
			System.out.println("共处理房源数:"+list.size());
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	public static void pull(String hlink){
		System.out.println("pulling "+hlink);
		try{
			URL url = new URL(hlink);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			Document doc = Jsoup.parse(result);
			Elements list = doc.getElementsByClass("suUl");
			Element ul = null;
			for(Element e : list){
				if(!e.tagName().equals("ul")){
					continue;
				}
				ul = e;
				break;
			}
			Elements price = ul.getElementsByClass("bigpri");
			if(price!=null && price.first()!=null){
				//总价
				System.out.println("zjia="+price.first().text());
			}
			Elements gaik = ul.getElementsMatchingOwnText("概况");
			if(gaik!=null && gaik.first()!=null){
				String str = gaik.first().nextElementSibling().text();
			    String[] arr = str.split(" ");
			    for(int i=0;i<arr.length;i++){
			    	if(arr[i].contains("年建")){
			    		String tmp = arr[i].replace("年建", "");
			    		tmp = tmp.replace(" ", "");
			    		String nbsp = String.valueOf((char)160);
			    		tmp = tmp.replace(nbsp, "");
			    		System.out.println("dateyear="+tmp);
			    		break;
			    	}
			    }
				
			}
			Elements hxing = ul.getElementsMatchingOwnText("户型");
			if(hxing!=null && hxing.first()!=null){
				String str=hxing.first().nextElementSibling().text();
				String[] arr = str.split(" ");
				for(int i=0;i<arr.length;i++){
					//加一个空格确保每次分割数组长度至少为2
					String[] tarr = (arr[i]+" ").split("室");
					if(tarr.length>1){
						System.out.println("hxf="+tarr[0]);
						continue;
					}
					tarr = (arr[i]+" ").split("厅");
					if(tarr.length>1){
						System.out.println("hxt="+tarr[0]);
						continue;
					}
					tarr = (arr[i]+" ").split("卫");
					if(tarr.length>1){
						System.out.println("hxw="+tarr[0]);
						continue;
					}
					tarr = (arr[i]+" ").split("㎡");
					if(tarr.length>1){
						System.out.println("mji="+tarr[0]);
						break;
					}
				}
			}
			Elements zxiu = ul.getElementsMatchingOwnText("装修");
			if(zxiu!=null && zxiu.first()!=null){
				String str = zxiu.first().nextElementSibling().text();
				String[] arr = str.split(" ");
				for(int i=0;i<arr.length;i++){
					if(arr[i].contains("朝")){
						System.out.println("chaoxiang="+arr[i]);
					}else if(arr[i].contains("层")){
						str = arr[i].replace("/", "");
						str = str.replace("层", "");
						System.out.println("zceng="+str);
					}else if(arr[i].contains("装修") || arr[i].contains("毛坯")){
						System.out.println("zxiu="+arr[i]);
					}else{
						System.out.println("lceng="+arr[i]);
					}
				}
			}
			Elements weizhi = ul.getElementsMatchingOwnText("位置");
			if(weizhi!=null && weizhi.first()!=null){
				Element li = weizhi.first().parent();
				String str = li.text();
				str = str.replace("位置：", "");
				String[] arr = str.split("-");
				if(arr.length>0){
					System.out.println("quyu="+arr[0]);
				}
				if(arr.length>2){
					String tmp = arr[2];
					tmp = tmp.replace(String.valueOf((char)160), "");
					arr = tmp.split("（");
					System.out.println("area="+arr[0]);
				}
			}
			Elements address = ul.getElementsMatchingOwnText("地址");
			if(address!=null && address.first()!=null){
				System.out.println("address="+address.first().nextElementSibling().ownText());
			}
			Elements lxr = ul.getElementsMatchingOwnText("联系人");
			if(lxr!=null && lxr.first()!=null){
				String str = lxr.first().nextElementSibling().text();
			    str = str.replace("（个人）", "");
				System.out.println("lxr="+str);
			}
			Element phone = ul.getElementById("t_phone");
			if(phone!=null){
				Elements img = phone.getElementsByTag("script");
				if(img!=null && img.first()!=null){
					String str = img.first().html();
					String[] arr = str.split("src=");
					if(arr.length>1){
						str = arr[1];
						arr = str.split("'");
						if(arr.length>2){
							str = arr[1];
							System.out.println("tel="+str);
						}
					}
				}
			}
		}catch(SocketTimeoutException ex){
			System.err.println("请求超时");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
