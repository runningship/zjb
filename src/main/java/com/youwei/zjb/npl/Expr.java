package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.List;

//分词后形成的表达式树，树种的每一条路径为一种语义可能性
//Lexer负责生成Expr,Parser负责进一步处理Expr
public class Expr {

	public String text;
	
	//next可以命名为children,这样更容易理解成树结构
	public List<Expr> next = new ArrayList<Expr>();
}
