package com.youwei.zjb.feedback.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Reply {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String conts;
	
	public Date addtime;
	
	public Integer uid;
	
	public Integer uname;
	
	public Integer fbId;
}
