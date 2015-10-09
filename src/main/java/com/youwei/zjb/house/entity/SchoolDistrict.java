package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 学区
 */
@Entity
public class SchoolDistrict {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 小区名称
	 */
	public String name;
	
	public Date addtime;
	
	public String path;
	
	public String center;
	
	public Integer offsetX;
	
	public Integer offsetY;
}
