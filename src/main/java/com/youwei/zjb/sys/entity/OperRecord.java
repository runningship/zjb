package com.youwei.zjb.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 操作记录表
 */
@Entity
@Table(name="sys_record")
public class OperRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer did;
	
	public Integer cid;
	
	public Integer uid;
	
	public String uname;
	
	public String dname;
	
	public String cname;
	
	public Date addtime;
	
	public String ip;
	
	public String pcma;
	
	public Integer type;
	
	public String fenlei;
	
	/**
	 * 操作内容
	 */
	public String conts;
	
	public String beizhu;
	
	public Integer yesno;
}
