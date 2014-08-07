package com.youwei.zjb.user.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="uc_comp_group")
public class DeptGroup {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String name;
	
	/**
	 * 上级分组
	 */
	public Integer pid;
	
	/**
	 * 房产公司
	 */
	public Integer cid;
}
