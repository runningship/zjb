package com.youwei.zjb.view;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.nodes.Element;

public class page {
	protected void buildHtmlWithJsonArray(Element temp, JSONArray arr) {
		for(int i=0;i<arr.size();i++){
			String child = buildHtmlWithJson(temp.toString() , arr.getJSONObject(i));
			temp.parent().append(child);
		}
	}

	protected String buildHtmlWithJson(String innerHtml, JSONObject json) {
		for(Object key : json.keySet()){
			innerHtml= innerHtml.replace("${"+key+"}", String.valueOf(json.get(key)));
		}
		return innerHtml;
	}
	
	
}
