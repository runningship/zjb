package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 只用于手机版用户
 */
@Entity
@Table(name="phone_house_seeout")
public class Track {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer hid;
	
	public Integer uid;
	
	public Integer chuzu;
	
	//最后访问时间
	public Date viewTime;
	
	public Integer viewCount;
}
