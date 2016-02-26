package com.youwei.zjb.swq.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class SWQClient {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String lic;
	
	public Date createtime;
	
	public Long createtimeInLong;
	
	public Integer sh;
}
