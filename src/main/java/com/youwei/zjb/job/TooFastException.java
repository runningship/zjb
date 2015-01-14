package com.youwei.zjb.job;

public class TooFastException extends RuntimeException{
	private static final long serialVersionUID = 1206737618878285522L;

	public TooFastException(String msg){
		super(msg);
	}
}
