package com.youwei.zjb.im.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class GroupMessageStatus {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Date readtime;
	
	public Integer receiverId;
	
	public Integer groupMsgId;
	
	public Integer hasRead;
}
