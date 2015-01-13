package com.youwei.zjb.job;

import java.io.IOException;
import java.util.Date;

import org.jsoup.nodes.Element;

public interface PullRentHouseAction {

	/**
	 * 获取楼盘名称
	 */
	public String getArea(Element elem);
	
	/**
	 * 获取面积
	 */
	public String getMji(Element elem);
	
	/**
	 * 获取租房方式
	 */
//	public String getRentType(Element elem);
	
	/**
	 * 获取租金
	 */
	public String getZujin(Element elem);
	
	/**
	 * 获取楼层
	 */
	public String getLceng(Element elem);
	
	/**
	 * 获取总层
	 */
	public String getZceng(Element elem);
	
	/**
	 * 获取楼型,如(多层,高层)
	 */
	public String getLxing(Element elem);
	
	/**
	 * 获取户型，如(1房2厅1卫)
	 */
	public String getHxing(Element elem);
	
	/**
	 * 获取装修，如(毛坯)
	 */
	public String getZxiu(Element elem);
	
	/**
	 * 获取建筑年代, 必须为四位数字
	 */
	public String getYear(Element elem);
	
	/**
	 * 获取区域 , 必须符合规范 {@code Quyu} {@link com.youwei.zjb.house.Quyu}
	 */
	public String getQuyu(Element elem);
	
	/**
	 * 获取地址
	 */
	public String getAddress(Element elem);
	
	/**
	 * 获取联系人
	 */
	public String getLxr(Element elem);
	
	/**
	 * 获取电话号码图片地址
	 */
	public String getTelImg(Element elem);
	
	/**
	 * 主卧，次卧..
	 */
	public String getWo(Element elem);
	
	/**
	 * 限女生，那女不限
	 */
	public String getXianZhi(Element elem);
	
	/**
	 * 配置，设施
	 */
	public String getPeiZhi(Element elem);
	
	/**
	 * 配置，设施
	 */
	public String getTitle(Element doc);
	
	public Element getDetailSumary(String url) throws IOException;
	
	/**
	 * 发布时间
	 */
	public Date getPubTime();
}
