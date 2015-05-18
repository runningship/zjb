package com.youwei.zjb.house.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 房主
 */
@Entity
@Cacheable
public class HouseOwner {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String name;
	
	public String tel;
	
	public String pwd;
	
	public Integer sh;
}
