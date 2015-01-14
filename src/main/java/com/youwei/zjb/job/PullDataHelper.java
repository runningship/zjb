package com.youwei.zjb.job;

import java.net.SocketTimeoutException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Element;

import com.youwei.zjb.house.FangXing;
import com.youwei.zjb.house.LouXing;
import com.youwei.zjb.house.RentState;
import com.youwei.zjb.house.RentType;
import com.youwei.zjb.house.entity.HouseRent;
import com.youwei.zjb.im.IMServer;

public class PullDataHelper {

	public final static int errorReportUserId= 36;
	public static HouseRent pullDetail(PullRentHouseAction action ,String hlink , Date pubTime , RentType rentType, String address){
		System.out.println("pulling "+hlink);
		Element sumary =null;
		try{
			sumary = action.getDetailSumary(hlink);
			HouseRent hr = new HouseRent();
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
			return hr;
		}catch(SocketTimeoutException ex){
			System.err.println("请求超时");
		}catch(TooFastException ex){
			throw ex;
		}catch(Exception ex){
			String msg = "扫网"+hlink+"失败，reason="+ex.getMessage();
			IMServer.sendMsgToUser(errorReportUserId, msg);
			ex.printStackTrace();
		}
		return null;
	}
}
