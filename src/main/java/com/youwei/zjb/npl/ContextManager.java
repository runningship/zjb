package com.youwei.zjb.npl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

public class ContextManager {

	private List<JSONObject> context = new ArrayList<JSONObject>();
	
	private Thread watcher = new Thread(){

		@Override
		public void run() {
			while(true){
				try {
					Thread.sleep(1000);
					work();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		
		
	};
	public ContextManager(){
		watcher.start();
	}
	
	private void work() {
		// TODO Auto-generated method stub
		
	}
	
	public void addContextItem(JSONObject item){
		JSONObject jobj = new JSONObject();
		jobj.put("addtime", new Date());
		jobj.put("target", item);
		context.add(jobj);
	}
}
