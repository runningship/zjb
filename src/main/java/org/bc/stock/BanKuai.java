package org.bc.stock;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BanKuai {

	@Id
	public Integer id;
	
	public String name;
}
