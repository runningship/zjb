package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class JifenRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	//房源图片编号
	public Integer hiid;
	
	public Integer uid;
	
	public String conts;
	
	public String beizhu;
	
	public Date addTime;
	
	//1赚取 ,2 消费
	public Integer type;
	
	public Integer offsetCount;
}
