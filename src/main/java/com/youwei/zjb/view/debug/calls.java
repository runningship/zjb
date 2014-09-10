package com.youwei.zjb.view.debug;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.SessionFilter;
import com.youwei.zjb.view.page;

public class calls extends page{

	public Document initPage(Document doc , HttpServletRequest req){
		JSONArray arr = new JSONArray();
		Map<String, Integer> map = SessionFilter.calls;
		int total=0;
		for(String key : map.keySet()){
			JSONObject obj = new JSONObject();
			total+=map.get(key);
			obj.put("path", key);
			obj.put("count", map.get(key));
			arr.add(obj);
		}
		Elements temp = doc.getElementsByClass("id_list");
		buildHtmlWithJsonArray(temp.first(),arr);
		temp.remove();
		Element totalElem = doc.getElementById("total");
		totalElem.html("总访问量: "+total);
		return doc;
	}
}
