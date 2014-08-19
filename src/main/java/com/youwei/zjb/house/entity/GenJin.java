package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="house_gj")
public class GenJin {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer cid;
	
	public Integer did;
	
	/**
	 * 用户id
	 */
	public Integer uid;
	
	/**
	 * 房源id
	 */
	@Column(nullable=false)
	public Integer hid;
	
	/**
	 * 跟进信息
	 */
	public String conts;
	
	public Date addtime;
	
	public Integer sh;
	
	/**
	 * 1 出租，0出售
	 */
	public Integer chuzu;
	
	//与House.ztai对应
	public Integer flag;
	
	//状态改变记录
	public String ztai;
}
