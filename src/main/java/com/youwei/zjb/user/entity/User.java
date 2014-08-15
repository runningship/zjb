package com.youwei.zjb.user.entity;

import java.util.Date;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bc.web.WebParam;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.entity.Role;

/**
 * 终端用户
 */
@Entity
@Table(name="uc_user")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public Integer cid;
	
	/**
	 * 分店
	 */
	@Column(name="did")
	public Integer did;
	
	public String uname;
	
	/**
	 * 登录帐号
	 */
	public String lname;
	
	@Column(name="lpwds")
	public String pwd;
	
	public Date addtime;
	
	public Date lasttime;
	
	@Column(name="unid")
	public Integer roleId;
	
	@Column(name="llock")
	public Integer lock;
	
	public String tel;
	
	@Column(name="dizhi")
	public String address;
	
	/**
	 *TODO
	 */
	public Integer flag;
	
	/**
	 * TODO
	 */
	public Integer sh;
	
	@Column(name="sex")
	public Integer gender;
	
	public Integer share;
	
	public String sim;
	/**
	 * 部门架构中的层次
	 */
	public String orgpath;
	
	public Role getRole(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Role.class, roleId);
	}
	
	public Department Department(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Department.class, did);
	}
	public Department Company(){
		return SimpDaoTool.getGlobalCommonDaoService().get(Department.class, Department().fid);
	}
}
