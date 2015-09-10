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
	
	public Integer uid;
	
	public Integer did;
	
	public Integer cid;
	
	public String uname;
	
	public String dname;
	
	public String cname;
	public String tradeNo;
	
	public String outTradeNo;
	
	public Float fee;
	
	public String feeStr;
	
	public Integer buyerId;
	
	public String buyerAccount;
	
	/**
	 * 1 支付宝,2 微信
	 */
	public Integer payType;
	
	//pc,phone
	public String clientType;
	
	public Date addtime;
	
	public String beizhu;
	
	public String status;
	
	//1,0
	public Integer finish;
	
	public Integer monthAdd;
}
