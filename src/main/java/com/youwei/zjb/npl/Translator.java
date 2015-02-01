package com.youwei.zjb.npl;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.npl.entity.Thing;

import net.sf.json.JSONObject;

public class Translator {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public JSONObject visit(Block block){
		if(block.isOper){
			visitOper(block);
		}else{
			visitLeaf(block);
		}
		return null;
	}

	private JSONObject visitLeaf(Block block) {
		JSONObject jobj = new JSONObject();
		for(String str : block.text){
			//find something about str
			jobj.put("text", str);
			Thing thing = dao.getUniqueByKeyValue(Thing.class, "value", str);
			if(thing.sense!=null){
				jobj.put(thing.sense, str);
			}
		}
		return jobj;
	}

	private void visitOper(Block block) {
		if("的".equals(block.text.get(0))){
			visitOf(block);
		}else if("有".equals(block.text.get(0))){
			
		}
	}

	private void visitOf(Block block) {
		JSONObject left = visit(block.left);
		JSONObject right = visit(block.right);
		
	}
}
