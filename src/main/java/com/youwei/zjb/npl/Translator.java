package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.npl.entity.Aggregation;

public class Translator {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public JSONObject visit(Block block){
		if(block.isOper){
			return visitOper(block);
		}else{
			return visitLeaf(block);
		}
	}

	private JSONObject visitLeaf(Block block) {
		JSONObject jobj = new JSONObject();
		for(int i=0;i<block.text.size();i++){
			String str = block.text.get(i);
			//find something about str
//			jobj.put("text", str);
			String aggr = BasicConcept.findRootAggregation(str);
			if(aggr==null){
				aggr = BasicConcept.findSet(str);
				if(aggr!=null){
					jobj.put("set", aggr);
				}else{
					jobj.put(i+"", str);
				}
			}else{
				//set elem
				jobj.put(aggr, str);
			}
			
		}
		return jobj;
	}

	private JSONObject visitOper(Block block) {
		if("的".equals(block.text.get(0))){
			return visitOf(block);
		}else if("有".equals(block.text.get(0))){
			return visitHas(block);
		}else if("是".equals(block.text.get(0))){
			return visitIs(block);
		}else{
			return null;
		}
	}

	private JSONObject visitIs(Block block) {
		JSONObject left = visit(block.left);
		JSONObject right = visit(block.right);
		right.put("left", left);
		right.put("oper", "是");
		return right;
	}

	private JSONObject visitOf(Block block) {
		JSONObject left = visit(block.left);
		JSONObject right = visit(block.right);
		if(left==null){
			System.out.println("的字前应该要有内容");
			return null;
		}
		if(right==null){
			System.out.println("的字后面没有内容改怎么处理呢?");
		}
		for(Object key : left.keySet()){
			//找到对应的条件类别，设置到right对象上
			if("指代".equals(key)){
				right.put("whos", left.get(key));
			}else{
				right.put(key, left.get(key));
			}
		}
		//小明的爸爸
		//一部新的手机
		return right;
	}
	
	private JSONObject visitHas(Block block){
		JSONObject left = visit(block.left);
		JSONObject right = visit(block.right);
		if(left==null){
			System.out.println("有字前应该要有内容");
			return null;
		}
		if(right==null){
			System.out.println("有字后应该要有内容");
			return null;
		}
		if(!left.has("有")){
			left.put("有", new ArrayList());
		}
		List list =(List) left.get("有");
		list.add(right);
		return left;
	}
}
