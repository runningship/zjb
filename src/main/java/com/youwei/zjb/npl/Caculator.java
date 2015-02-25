package com.youwei.zjb.npl;

import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.npl.entity.Aggregation;

import net.sf.json.JSONObject;

public class Caculator {

	public void run(JSONObject jobj){
		if("是".equals(jobj.get("oper"))){
			execIs(jobj);
		}
	}

	private void execIs(JSONObject jobj) {
		JSONObject left = jobj.getJSONObject("left");
		String leftVal = getValue(left);
		if(jobj.containsKey("set")){
			Aggregation po = SimpDaoTool.getGlobalCommonDaoService().getUniqueByParams(Aggregation.class, new String[]{"elem" , "sets"}, 
					new Object[]{leftVal , jobj.getString("set")});
			if(po==null){
				po = new Aggregation();
				po.elem = leftVal;
				po.sets = jobj.getString("set");
				SimpDaoTool.getGlobalCommonDaoService().saveOrUpdate(po);
			}
		}else{
			
		}
	}
	
	private String getValue(JSONObject jobj){
		String result  = new String();
		for(Object val : jobj.values()){
			result += val;
		}
		return result;
	}
}
