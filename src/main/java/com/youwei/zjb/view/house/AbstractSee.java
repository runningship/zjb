package com.youwei.zjb.view.house;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.util.JSONHelper;

public abstract class AbstractSee {
	public Document initPage(Document doc , HttpServletRequest req){
		String html = doc.html();
		String id = req.getParameter("id");
		CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
		JSONObject json = getData(Integer.valueOf(id));
		if(json==null){
			html="数据不存在";
			return Jsoup.parse(html);
		}else{
			Elements nrlist = doc.getElementsByClass("neirong");
			for(Element elem : nrlist){
				String innerHtml = elem.html();
				for(Object key : json.keySet()){
					innerHtml= innerHtml.replace("${"+key+"}", String.valueOf(json.get(key)));
				}
				elem.html(innerHtml);
			}
			String hql = "select gj.conts as conts , d.namea as dname , u.uname as uname , gj.addtime as addtime from GenJin gj , User u , "
					+ " Department d where gj.hid=? and gj.uid=u.id and u.did=d.id and gj.chuzu=?";
			List<Map> gjList = dao.listAsMap(hql, Integer.valueOf(id) , getChuzu());
			Elements temp = doc.getElementsByClass("list");
			temp.first().parent();
			buildHtmlWithJsonArray(temp.first() , JSONHelper.toJSONArray(gjList));
			temp.remove();
		}
		return doc;
	}

	protected abstract JSONObject getData(int id);
	
	protected abstract int getChuzu();
	
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
