package com.youwei.zjb.oa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Site {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	//收藏人
	public Integer uid;
	
	public Integer cid;
	
	public String title;
	
	public String url;
	
	public String label;
}
