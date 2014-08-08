package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 充值记录
 *
 */
@Entity
public class Charge {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String tradeNo;
	
	public String outTradeNo;
	
	public Float fee;
	
	public String feeStr;
	
	public Integer buyerId;
	
	public String buyerAccount;
	
	/**
	 * 1 支付宝,2 微信
	 */
	public Integer type;
	
	public Date addtime;
	
	public String beizhu;
	
	public String status;
}
