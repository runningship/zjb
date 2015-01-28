package com.youwei.zjb.npl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Thing {

	@Id
	public String id;
	
	//与word想对应
	public String text;
	
	public String sense;
	
	public String value;
}
