package com.youwei.zjb.client.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Client {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 当前负责的业务员
	 */
	public Integer uid;
	
	public Integer did;
	
	public Integer cid;
	
	/**
	 * 业务员姓名
	 */
	public String uname;
	
	public String dname;
	
	public String cname;
	
	public Integer djrDid;
	/**
	 * 记录的最初登记人
	 */
	public Integer djrId;
	
	public String djrName;
	
	/**
	 * 客户姓名
	 */
	@Column(nullable=false)
	public String name;
	
	/**
	 * ,隔开
	 */
	public String tels;
	
	/**
	 * 期望楼盘 ","隔开
	 */
	public String areas;
	
	/**
	 * 期望路段 ","隔开
	 */
	public String luduans;
	
	public String lxings;
	
	/**
	 * 户型，房型
	 */
	public String hxings;
	
	public String zxius;
	
	//要查询面积期望落在区间D(d1到d2)的客户，算法为：d1<=mjiFrom<=d2 || d1<=mjiTo<=d2
	//用if(mjiFrom==null) mjiFrom=0; if(mjiTo==null) mjiTo=100000;来保证区间D的完整性
	public Float mjiFrom;
	
	public Float mjiTo;
	
	/**
	 * 总价
	 */
	public Float jiageFrom;
	
	public Float jiageTo;
	
	/**
	 * 购房单价
	 */
	public Float djia;
	
	public Integer zujinFrom;
	public Integer zujinTo;
	
	public String beizhu;
	
	public Date addtime;
	
	/**
	 * 0 出售，1 出租
	 */
	public int chuzu=0;
	
	public Integer lcengFrom;
	
	public Integer lcengTo;
	
	public Integer yearFrom;
	public Integer yearTo;
	
	public String quyus;
	
	public String source;
	
	public Date gjtime;
	
	public Integer isdel=0;
}
