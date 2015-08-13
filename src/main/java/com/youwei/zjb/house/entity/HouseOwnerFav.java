package com.youwei.zjb.house.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 房主搜藏
 */
@Entity
public class HouseOwnerFav {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer hid;
	
	public Integer hoid;
	
}
