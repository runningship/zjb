package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class House {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;

	/**
	 * 房产公司
	 */
	public Integer cid;
	
	/**
	 * 店面编号
	 */
	@Column(name = "did")
	public Integer deptId;

	@Column(name = "uid")
	public Integer userId;

	@Column(nullable=false)
	public String quyu;

	/**
	 * 楼盘名称
	 */
	@Column(nullable=false)
	public String area;

	/**
	 * 楼栋号
	 */
	@Column(nullable=false)
	public String dhao;

	/**
	 * 房号
	 */
	public String fhao;

	public String luduan;

	/**
	 * 楼层
	 */
	@Column(nullable=false)
	public Integer lceng;

	/**
	 * 总层
	 */
	public Integer zceng;

	/**
	 * 楼型
	 */
	public String lxing;

	/**
	 * 房型(房数)
	 */
	public Integer hxf;

	/**
	 * 房型(厅数)
	 */
	public Integer hxt;

	/**
	 * 房型(卫数)
	 */
	public Integer hxw;


	/**
	 * 装修
	 */
	@Column(name="zxiu")
	public Integer zhuangxiu;

	@Column(name="mji", nullable=false)
	public Float mianji;

	/**
	 * 总价(单位万)
	 */
	public Float zjia;

	public Float djia;

	/**
	 * 联系人,房主
	 */
	public String lxr;

	/**
	 * 房主电话，/ 分隔
	 */
	public String tel;

	/**
	 * 发布人
	 */
	public String forlxr;

	public String fortel;

	public String beizhu;

	 /**
	 * 状态：4 在售，6 已售，7 停售
	 */
	public String ztai;

	public Integer dateyear;

	public Date dateadds;

	public Date dategenjin;

	/**
	 * 收藏此房源的人
	 */
	public String fav;

	/**
	 * 1,已删除 0 或空未删除
	 */
	public Integer isdel;

	public Integer sh;
	
	public Integer seeGX;
	
	public Integer seeHM;
	
	public Integer seeFH;

}
