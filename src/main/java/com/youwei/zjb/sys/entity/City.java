package com.youwei.zjb.sys.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class City {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String name;
	
	public String py;
	
	public String province;
	
	//服务器地址
	//TODO 改名为host
	public String domain;
}
