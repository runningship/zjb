package com.youwei.zjb.npl;


//语义块,一个语义块可以是一个句子，也可以是一个短语
//理论上一个句子最终转换成一个语义块，歧义(双关)句子可以转换成多个语义块
public class Block {
	
	public String text;
	
	public Block next;
	
	public Block pre;
}
