package com.youwei.zjb.oa.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class NoticeReply {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	//回复人
	public Integer replyUid;
	
	//发送人
	public Integer noticeId;
	
	public String conts;
	
	public Date addtime;
	
}
