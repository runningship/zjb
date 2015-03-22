package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.npl.entity.Word;

//分词器
public class Lexer {

	public static final String ExprSeprator = "-";
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		String str = "小米手机被黄牛抬价了很多";
		String str = "我有一部新手机";
		Lexer p = new Lexer();
		Expr expr = p.run(str);
		if(expr==null){
			return;
		}
		List<String> results = p.getResult(expr);
		for(String r : results){
			System.out.println(r);
		}
	}
	
	public List<String> getResult(Expr head){
		List<String> results = new ArrayList<String>();
		if(head.next.isEmpty()){
			results.add(ExprSeprator+head.text);
		}else{
			for(Expr next : head.next){
				for(String str : getResult(next)){
					results.add(ExprSeprator+head.text+str);
				}
			}
		}
		return results;
	}
	public Expr run(String str){
		if(StringUtils.isEmpty(str)){
			return null;
		}
		Expr head = new Expr();
		head.text = "";
		head.next = nextExpr(str);
		return head;
	}

	private List<Word> getWordStartWith(String head) {
		return SimpDaoTool.getGlobalCommonDaoService().listByParams(Word.class, "from Word where text like ? order by len(text) desc", 
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
				break;
			}
		}
		if(exprs.isEmpty()){
			Expr expr = new Expr();
			expr.text = String.valueOf(str.charAt(0));
			exprs.add(expr);
		}
		for(Expr expr : exprs){
			expr.next = nextExpr(str.substring(expr.text.length()));
		}
		return exprs;
	}
	
}
