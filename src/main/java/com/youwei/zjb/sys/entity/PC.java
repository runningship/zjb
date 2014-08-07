package com.youwei.zjb.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 授权的电脑
 */
@Entity
@Table(name="uc_pc")
public class PC {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 公司
	 */
	public Integer cid;
	
	/**
	 * 店面
	 */
	@Column(name="did")
	public Integer deptId;
	
	@Column(name="lname")
	public String pcname;
	
	public Date addtime;
	
	public Date lasttime;
	
	public String lastip;
	
	@Column(name="code_ma")
	public String mac="";
	
	@Column(name="code_pc")
	public String board="";
	
	/**
	 * 加密过的机器码
	 */
	@Column(name="code_cp")
	public String uuid="";
	
	@Column(name="regnumber")
	public String authCode;
	
	@Column(name="llock")
	public Integer lock;
	
	public String beizhu;
}
