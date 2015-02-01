package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.npl.entity.Oper;

//生成语义块
public class Parser {
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public Block buildBlock(List<String> words){
		int index = getNextOper(words);
		Block block = new Block();
		if(index<=-1){
			block.text.addAll(words);
			return block;
		}
		 block.text.add(words.get(index));
		 block.isOper = true;
		 if(index>0){
			 block.left = buildBlock(words.subList(0, index));
		 }
		 if(index<words.size()-1){
			 block.right = buildBlock(words.subList(index+1, words.size()));
		 }
		 return block;
	}
	
	public int getNextOper(List<String> words){
		int pos = -1;
		int priority = Integer.MAX_VALUE;
		for(int i=0;i<words.size();i++){
			Oper oper = dao.getUniqueByKeyValue(Oper.class, "text", words.get(i));
			if(oper!=null){
				if(oper.priority<=priority){
					priority = oper.priority;
					pos = i;
				}
			}
		}
		return pos;
	}
}
