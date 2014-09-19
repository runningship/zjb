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
	
	public String hxings;
	
	public String zxius;
	
	public Integer mjiFrom;
	
	public Integer mjiTo;
	
	/**
	 * 总价
	 */
	public Integer jiageFrom;
	
	public Integer jiageTo;
	
	
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
	
	public String quyus;
	
	public String source;
	
	public Date gjtime;
}
