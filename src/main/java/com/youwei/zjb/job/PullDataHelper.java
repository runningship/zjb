package com.youwei.zjb.job;

import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.RentState;
import com.youwei.zjb.house.RentType;
import com.youwei.zjb.house.entity.HouseRent;

public class PullDataHelper {

	public static HouseRent pullDetail(PullRentHouseAction action ,String hlink , Date pubTime , RentType rentType){
		System.out.println("pulling "+hlink);
		try{
			URL url = new URL(hlink);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream(),"utf-8");
			Document doc = Jsoup.parse(result);
			if(doc.getElementsMatchingOwnText("页面可能被删除").isEmpty()==false){
				return null;
			}
			Element sumary = doc.getElementsByClass("col_sub sumary").first();
			if(sumary==null){
				sumary = doc.getElementsByClass("detailPrimary").first();
			}
			HouseRent hr = new HouseRent();
			hr.beizhu="";
			hr.site = "58";
			hr.href = hlink;
			hr.address = action.getAddress(sumary);
			hr.area = action.getArea(sumary);
			//中介宝
			hr.cid = 1;
			//信息中心
			hr.did = 90;
			if(pubTime==null){
				hr.dateadd = new Date();
			}else{
				hr.dateadd = pubTime;
			}
			hr.dateyear = action.getYear(sumary);
			hr.dhao = "";
			hr.fhao="";
			String zujin = action.getZujin(sumary);
			try{
				hr.zjia = Float.valueOf(zujin);
			}catch(NumberFormatException ex){
				hr.beizhu += " 租金："+zujin+"";
			}
			hr.fangshi = rentType.getCode();
			hr.lxr = action.getLxr(sumary);
			hr.telImg = action.getTelImg(sumary);
			String hx = action.getHxing(sumary);
			try{
				FangXing hxEnum = FangXing.parse(hx);
				hr.hxf = hxEnum.getHxf();
				hr.hxt = hxEnum.getHxt();
				hr.hxw = hxEnum.getHxw();
			}catch(Exception ex){
				hr.beizhu += " 户型："+hx+"";
			}
			String lceng = action.getLceng(sumary);
			String zceng = action.getZceng(sumary);
			try{
				hr.lceng = Integer.valueOf(lceng);
			}catch(NumberFormatException ex){
				if(StringUtils.isNotEmpty(lceng)){
					hr.beizhu += " 楼层："+lceng+"";
				}
				
			}
			try{
				hr.zceng = Integer.valueOf(zceng);
			}catch(NumberFormatException ex){
				if(StringUtils.isNotEmpty(zceng)){
					hr.beizhu += " 总层："+zceng+"";
				}
			}
//			hr.lxr = "lxr";
			String mji = action.getMji(sumary);
			try{
				hr.mji = Float.valueOf(mji);
			}catch(NumberFormatException ex){
				hr.beizhu += " 面积："+mji+"";
			}
			hr.quyu = action.getQuyu(sumary);
			hr.seeFH = 1;
			hr.seeGX = 1;
			hr.seeHM = 1;
			hr.ztai = String.valueOf(RentState.在租.getCode());
			hr.zxiu = action.getZxiu(sumary);
			
			hr.wo = action.getWo(sumary);
			hr.xianzhi = action.getXianZhi(sumary);
			hr.peizhi = action.getPeiZhi(sumary);
			hr.title = action.getTitle(sumary);
			hr.isdel = 0;
			return hr;
		}catch(SocketTimeoutException ex){
			System.err.println("请求超时");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
}
