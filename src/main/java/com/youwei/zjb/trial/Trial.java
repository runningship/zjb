package com.youwei.zjb.trial;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Trial {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String uname;
	
	public String cname;
	
	public String dname;
	
	public String tel;
	
	public String qq;
	
	public String city;
	
	public String conts;
	
	public Integer finish;
	
	//处理人
	public String clrName;
	
	public Integer clrId;
	
	//处理情况
	public String clConts;
}
