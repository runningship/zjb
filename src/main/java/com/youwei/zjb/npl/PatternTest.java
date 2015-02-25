package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.youwei.zjb.StartUpListener;

public class PatternTest {

	public static void main(String[] args){
		StartUpListener.initDataSource();
//		String str = "小米手机被黄牛抬价了很多";
//		String str = "小明的爸爸有一部新的手机";
		String str = "三是数量";
		Lexer p = new Lexer();
		Expr expr = p.run(str);
		if(expr==null){
			return;
		}
		List<String> lexerResults = p.getResult(expr);
		for(String r : lexerResults){
			System.out.println(r);
		}
		//暂取词法分析结果的第一个
		if(lexerResults.isEmpty()){
			return;
		}
		Parser parser = new Parser();
		String[] temp = lexerResults.get(0).split(Lexer.ExprSeprator);
		List<String> words = new ArrayList<String>();
		for(int i=0;i<temp.length;i++){
			if(StringUtils.isNotEmpty(temp[i])){
				words.add(temp[i]);
			}
		}
//		List<OperMatch> opers = parser.matchOper(words.toArray(new String[]{}));
		Block block = parser.buildBlock(words);
		System.out.println(block.toString());
		Translator trans = new Translator();
		JSONObject jobj = trans.visit(block);
		System.out.println(jobj);
		Caculator caculator = new Caculator();
		caculator.run(jobj);
	}
}
