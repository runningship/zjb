package org.bc.stock;

import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//http://data.10jqka.com.cn/funds/ggzjl/#refCountId=db_50754c2e_294
public class GetBanKuaiList {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	public void pullStockList() throws Exception{
		URL url = new URL("http://bbs.10jqka.com.cn/codelist.html");
		URLConnection conn = url.openConnection();
		String html = IOUtils.toString(conn.getInputStream(),"gbk");
		Document doc = Jsoup.parse(html);
		Elements elements = doc.getElementsByAttributeValue("class", "bbsilst_wei3");
		
		for(int i=0;i<elements.size();i++){
			Element elem = elements.get(i);
			String type = elem.child(0).attributes().get("id");
			Elements stocks = elem.getElementsByTag("li");
			for(int j=0;j<stocks.size();j++){
				String value = stocks.get(j).child(0).text();
				int index = value.lastIndexOf(' ');
				String name = value.substring(0, index);
				String code = value.substring(index+1, value.length());
				System.out.println(name+"-"+code);
				//String[] arr = value.split(" ");
				BanKuai bk = new BanKuai();
				bk.name = "";
				if(isExsits(bk)==false){
					dao.saveOrUpdate(bk);
				}else{
					System.out.println(name + " is already exist");
				}
			}
		}
		
		System.out.println(elements.size());
	}

	private boolean isExsits(BanKuai bankuai) {
		Stock old = dao.getUniqueByParams(Stock.class,  new String[]{"name"}, new Object[]{bankuai.name});
		return old==null ? false:true;
	}
}
