package com.youwei.zjb.view.settings;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.youwei.zjb.view.page;

public class uc_index_c_add extends page{
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public Document initPage(Document doc, HttpServletRequest req) {
		String html = doc.html();
		List<Map> list = dao.listAsMap("select max(cnum) as maxCnum from Department where fid=0 and cnum<>'888'");
		int count = 0;
		if(!list.isEmpty()){
			count = Integer.valueOf(list.get(0).get("maxCnum").toString());
		}
		count++;
		String pattern="000";
		 java.text.DecimalFormat df = new java.text.DecimalFormat(pattern);
		String cnum = df.format(count);
		html = html.replace("$${cnum}", cnum);
		html = html.replace("$${authCode}", String.valueOf(System.currentTimeMillis()));
		return Jsoup.parse(html);
	}
}
