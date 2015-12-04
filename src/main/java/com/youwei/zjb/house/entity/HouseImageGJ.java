package com.youwei.zjb.house.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HouseImageGJ {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;

	//HouseImage id
	public Integer hiid;
	
	public Integer uid;
	
	public Integer zan;
	
	public Integer shit;
	
	public Integer hid;
}
