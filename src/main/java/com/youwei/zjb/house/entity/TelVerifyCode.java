package com.youwei.zjb.house.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 手机验证码码
 */
@Entity
@Cacheable
public class TelVerifyCode {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String code;
	
	public String tel;
	
	public Date sendtime;
}
