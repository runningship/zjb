package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.youwei.zjb.SimpDaoTool;

/**
 * fid为0 表示公司，其他表示分公司
 */
@Entity
@Table(name="uc_comp")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Department {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	/**
	 * 公司id
	 */
	public Integer fid;
	
	@Column(nullable=false)
	public String namea;
	
	@Column(name="code_num")
	public String codeNum;
	
	@Column(name="auth_code")
	public String authCode;
	
	public String cnum;
	
	public Integer pcnum;
	
	public String lxr;
	
	public String tel;
	
	public String beizhu;
	
	public Date addtime;
	
	public Integer flag;
	
	public Integer sh;
	
	public Integer share;
	
	public void Group(){
		
	}
	
	public Department Company(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Department.class,fid);
	}
}
