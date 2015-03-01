package com.youwei.zjb.finace;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class StockPrice implements Comparable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public Integer id;
	
	public String code;
	
	public String day;
	
	public Float price;
	
	public Float zhangdie;
	
	public Float cashIn;

	@Override
	public int compareTo(Object o) {
		StockPrice another = (StockPrice)o;
		if(another.price>price){
			return -1;
		}else{
			return 1;
		}
	}
	
}
