package com.youwei.zjb.biz.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OutHouse {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	//业务员id
	public Integer uid;
	
	public int did;
	
	public int cid;
	
	/**
	 * 0 带看，1 陪看，2 自看
	 */
	public int type;
	
	public Integer clientId;
	
	public String clientName;
	
	public String ctels;
	
	public String dname;
	
	public String houseIds;
	
	public String houseInfos;
	
	public Date outtime;
	
	public Date backtime;
	
	public Date createtime;
	
	public String status;
	//补充说明
	public String remarks;
	
	//看房总结
	public String conts;
}
