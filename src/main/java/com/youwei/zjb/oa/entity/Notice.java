package com.youwei.zjb.oa.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Notice {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	//发送人
	public Integer senderId;
	
	@Column(nullable=false)
	public String title;
	
	public String conts;
	
	public Date addtime;
	
	public int isPublic;
}
