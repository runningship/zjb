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
	
	//3 广场公告, 4 二手信息
	public int isPublic;
	
	//点赞数量
	public Integer zans;
	
	//阅读数量
	public Integer reads;
	
	public Integer replys;
	
	public Integer orderx;
	
	//点赞人的id ;号隔开,用于"广场"功能
	public String zanUids;
	
	public transient String senderName;
	public transient Integer senderAvatar;
}
