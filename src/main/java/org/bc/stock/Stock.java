package org.bc.stock;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Stock {

	@Id
	public String id;
	
	public String name;
	
	public String code;
	
	public String type;

	public Float cashIn;
	
	
}
