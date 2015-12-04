package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class HouseImage {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;

	//上传人
	public Integer uid;
	
	public Integer hid;
	
	public Integer chuzu;
	
	public String wxMediaId;
	
	public String path;
	
	public String thumb;
	
	public Date addtime;
	
	public Integer isPrivate;
	
	public Integer sh;
	
	public Integer zanCount;
	
	public Integer shitCount;
}
