package org.bc.stock;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DailyPrice {
	
	@Id
	public String id;

	public String name;
	
	public String code;
	
	//停盘价格
	public float price;
	
	public String day;
}
