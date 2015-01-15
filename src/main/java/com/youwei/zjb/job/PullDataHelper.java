package com.youwei.zjb.job;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.utils.LogUtil;
import org.jsoup.nodes.Element;

import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.LouXing;
import com.youwei.zjb.house.RentState;
import com.youwei.zjb.house.RentType;
import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.im.IMServer;

public class PullDataHelper {

	private static Map<String,String> cookies = new HashMap<String, String>();
	public final static int errorReportUserId= 36;
	public static HouseRent pullDetail(PullRentHouseAction action ,String hlink , Date pubTime , RentType rentType, String address){
		System.out.println("pulling "+hlink);
		Element sumary =null;
		HouseRent hr = null;
		try{
			sumary = action.getDetailSumary(hlink);
			hr = new HouseRent();
			hr.beizhu="";
			hr.site = action.getSiteName();
			hr.href = hlink;
			if(address==null){
				hr.address = action.getAddress(sumary);
			}else{
				hr.address = address;
			}
			
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
			action.getTel(hr,sumary);
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
				if(hr.zceng<=7){
					hr.lxing = LouXing.多层.name();
				}else if(hr.zceng<=18){
					hr.lxing = LouXing.小高层.name();
				}else if(hr.zceng<=35){
					hr.lxing = LouXing.高层.name();
				}
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
			if(StringUtils.isNotEmpty(hr.tel)){
				HouseRent po = SimpDaoTool.getGlobalCommonDaoService().getUniqueByKeyValue(HouseRent.class, "tel", hr.tel);
				if(po!=null){
					LogUtil.info("房源"+hlink+"重复");
					return null;
				}
			}
			return hr;
		}catch(SocketTimeoutException ex){
			System.err.println("请求超时");
		}catch(TooFastException ex){
			throw ex;
		}catch(Exception ex){
			StackTraceElement stack = ex.getStackTrace()[0];
			String msg = "扫网"+hlink+"失败，href="+hlink+",at"+stack.getClassName()+" line "+stack.getLineNumber()+","+stack.getMethodName();
			IMServer.sendMsgToUser(errorReportUserId, msg);
			ex.printStackTrace();
		}
		return null;
	}
	
	public static String getHttpData(String urlStr ,String site , String encode) throws IOException{
		URL url = new URL(urlStr);
		URLConnection conn = url.openConnection();
//		if(cookies.containsKey(site)){
//			conn.setRequestProperty("Cookie", cookies.get(site));
//		}
		conn.setDefaultUseCaches(false);
		conn.setUseCaches(false);
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		String result = IOUtils.toString(conn.getInputStream(),encode);
		
		//获取cookie  
//        Map<String,List<String>> map=conn.getHeaderFields();
//        Set<String> set=map.keySet();  
//        for (Iterator iterator = set.iterator(); iterator.hasNext();) {
//            String key = (String) iterator.next();
//            if(key==null){
//            	continue;
//            }
//            if (key.equals("Set-Cookie")) {  
//                List<String> list = map.get(key);  
//                StringBuilder builder = new StringBuilder();  
//                for (String str : list) {  
//                    builder.append(str).toString();  
//                }  
//                String firstCookie = builder.toString();
//                cookies.put(site, firstCookie);
//            }  
//        }
		
		return result;
	}
}
