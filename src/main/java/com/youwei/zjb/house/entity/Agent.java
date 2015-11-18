package com.youwei.zjb.house.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 中介
 */
@Entity
public class Agent {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String tel;
	
	//标记此经纪人的用户
	public Integer labelUid;
	
	public Integer hid;
}
