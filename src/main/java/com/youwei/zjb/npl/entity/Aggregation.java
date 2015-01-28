package com.youwei.zjb.npl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Aggregation {

	@Id
	public Integer id;
	
	public String elem;
	
	public String sets;
}
