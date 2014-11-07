package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ExpHouse {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;

	public String quyu;

	/**
	 * 楼盘名称
	 */
	public String area;

	@Column(name="luduan")
	public String address;

	/**
	 * 楼层
	 */
	public String lceng;

	/**
	 * 总层
	 */
	public String zceng;

	/**
	 * 房型(房数)
	 */
	public String hxf;

	/**
	 * 房型(厅数)
	 */
	public String hxt;

	/**
	 * 房型(卫数)
	 */
	public String hxw;


	/**
	 * 装修
	 */
	public String zxiu;

	public String mji;

	/**
	 * 总价(单位万)
	 */
	public String zjia;

	public String chaoxiang;
	/**
	 * 联系人,房主
	 */
	public String lxr;

	/**
	 * 房主电话，/ 分隔
	 */
	public String tel;

	public String telImg;
	
	public String dateyear;

	public Date addtime;
	
	public Date pubtime;
	
	//房源地址
	public String href;
	
	//来自网站
	public String site;
	
	//quyu+zceng+lceng+hxf+hxt+hxw
	public String code;
	
	public Integer repeat;
	
	//是否已经通过审核
	//0 未审核，1审核通过，2审核不通过
	public Integer finish;
	
	public String timeOffset;
	
	/**
	 * 当前处理人
	 */
	public String shr;
	
	public Integer hid;
	
	public String beizhu;
}
