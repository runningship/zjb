package com.youwei.zjb.npl;

import java.util.List;

import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.npl.entity.Word;

public class Parser {

	public void run(String str){
		String head ="";
		for(int i=0;i<str.length();i++){
			head+=str.charAt(i);
			List<Word> list = getWordStartWith(head);
		}
	}

	private List<Word> getWordStartWith(String head) {
		return SimpDaoTool.getGlobalCommonDaoService().listByParams(Word.class, "from Word where name like ?", 
				new Object[]{head+"%"});
	}
}
