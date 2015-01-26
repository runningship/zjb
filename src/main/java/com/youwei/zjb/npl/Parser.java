package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.npl.entity.Word;

public class Parser {

	public static void main(String[] args){
		
	}
	
	public void run(String str){
		if(StringUtils.isEmpty(str)){
			return;
		}
		Expr head = new Expr();
		head.next = nextExpr(str);
		
	}

	private List<Word> getWordStartWith(String head) {
		return SimpDaoTool.getGlobalCommonDaoService().listByParams(Word.class, "from Word where name like ?", 
				new Object[]{head+"%"});
	}
	
	private List<Expr> nextExpr(String str){
		List<Expr> exprs = new ArrayList<Expr>();
		if(StringUtils.isEmpty(str)){
			return exprs;
		}
		
		List<Word> list = getWordStartWith(String.valueOf(str.charAt(0)));
		for(Word w : list){
			if(str.startsWith(w.text)){
				Expr expr = new Expr();
				expr.text = w.text;
				exprs.add(expr);
			}
		}
		
		for(Expr expr : exprs){
			expr.next = nextExpr(str.substring(expr.text.length()-1));
		}
		return exprs;
	}
}
