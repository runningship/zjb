package org.bc.stock;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BanKuaiPrice {

	@Id
	public Integer id;
	
	public Integer bkId;
	
	public Float point;
	
	public Float cashIn;
	
	//涨跌幅
	public Float zhangdie;
}
