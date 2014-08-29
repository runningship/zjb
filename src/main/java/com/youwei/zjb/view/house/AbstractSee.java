package com.youwei.zjb.view.house;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.bc.sdak.CommonDaoService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.youwei.zjb.SimpDaoTool;
import com.youwei.zjb.util.JSONHelper;
import com.youwei.zjb.view.page;

public abstract class AbstractSee extends page{
	public Document initPage(Document doc , HttpServletRequest req){
		String html = doc.html();
		String id = req.getParameter("id");
		CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
		JSONObject json = getData(Integer.valueOf(id));
		if(json==null){
			html="404";
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
					+ " Department d where gj.hid=? and gj.uid=u.id and u.did=d.id and gj.chuzu=? order by addtime desc";
			List<Map> gjList = dao.listAsMap(hql, Integer.valueOf(id) , getChuzu());
			Elements temp = doc.getElementsByClass("list");
			buildHtmlWithJsonArray(temp.first() , JSONHelper.toJSONArray(gjList));
			temp.remove();
		}
		Elements fav = doc.getElementsByAttributeValue("fav", json.getString("fav"));
		fav.remove();
		html = doc.html();
		Object seeFH = json.get("seeFH");
		if(Integer.valueOf(1).equals(seeFH)){
			html = html.replace("${fdhao}", json.getString("dhao")+"-"+ json.getString("fhao"));
		}else{
			html = html.replace("${fdhao}","");
		}
		html = html.replace("$${lxr}", json.getString("lxr"));
		html = html.replace("$${tel}", json.getString("tel"));
		return Jsoup.parse(html);
	}

	protected abstract JSONObject getData(int id);
	
	protected abstract int getChuzu();
	
}
