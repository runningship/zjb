package com.youwei.zjb.npl.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Thing {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public String id;
	
	//与word想对应
//	public String text;
	
	public String sense;
	
	public String value;
}
