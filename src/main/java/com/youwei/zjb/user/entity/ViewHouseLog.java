package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 用于访问记录统计，足迹使用Track类
 * @author Administrator
 *
 */
@Entity
public class ViewHouseLog {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer hid;
	
	public Integer uid;
	
	public Integer isMobile;
	
	public Date viewTime;
}
