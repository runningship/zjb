package com.youwei.zjb.npl.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Word {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public String text;
}
