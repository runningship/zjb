package com.youwei.zjb.house.entity;

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

/**
 * 都盘字典(小区表)
 */
@Entity
@Table(name="house_annex")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class District {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String quyu;
	
	/**
	 * 小区名称
	 */
	@Column(name="area")
	public String name;
	
	/**
	 * 小区首字母
	 */
	@Column(name="pin")
	public String pyShort;
	
	/**
	 * 小区全拼
	 */
	public String pinyin;
	
	/**
	 * 价格，均价
	 */
	public String jizhunjia;
	
	public String address;
	
	/**
	 * 维度
	 */
	public Float maplat;
	
	/**
	 * 经度
	 */
	public Float maplng;
	
	public Date addtime;
	
	/**
	 * 是否审核
	 * 1 已审核
	 * 0 未审核
	 */
	public Integer sh;
	
	public Integer uid;
	
}
