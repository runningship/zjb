package com.youwei.zjb.finace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StockPrice {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	
	public String code;
	
	public String day;
	
	public Float price;
	
	public Float zhangdie;
	
	public Float cashIn;
	
}
