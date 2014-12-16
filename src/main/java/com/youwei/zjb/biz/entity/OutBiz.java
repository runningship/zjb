package com.youwei.zjb.biz.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OutBiz {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer uid;
	
	public Integer pyUid;
	
	public int did;
	
	public int cid;
	
	public String reason;
	
	public Date outtime;
	
	public Date backtime;
	
	public Date createtime;
	
	public String status;
	
	//总结
	public String conts;
	
	//批阅意见
	public String pyyj;
}
