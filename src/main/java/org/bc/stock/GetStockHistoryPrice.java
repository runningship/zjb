package org.bc.stock;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Transactional;
import org.bc.sdak.TransactionalServiceHelper;


//http://hq.sinajs.cn/list=sh601006
//http://www.2cto.com/kf/201212/178371.html
public class GetStockHistoryPrice {

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	
	@Transactional
	private void pullCurrentPrice(String stockCode) throws Exception{
		URL url = new URL("http://qt.gtimg.cn/q="+stockCode);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		if(conn.getResponseCode()>400){
			System.out.println(IOUtils.toString(conn.getErrorStream()));
			return;
		}
		String result = IOUtils.toString(conn.getInputStream(),"gbk");
		String[] data = result.split("~");
		System.out.println(result);
		for(int i=0;i<data.length;i++){
			//System.out.println(i+" : "+data[i]);
		}
		DailyPrice price = new DailyPrice();
		price.name = data[1];
		price.code = data[2];
		price.price = Float.valueOf(data[3]);
		
		price.id = (UUID.randomUUID().toString());
		DailyPrice oldPrice = dao.getUniqueByParams(DailyPrice.class, new String[]{"code","day"}, new Object[]{price.code ,price.day});
		if(oldPrice==null){
			dao.saveOrUpdate(price);
		}
	}

	public void execute(){
		List<Stock> list = dao.listByParams(Stock.class, "from Stock where type <> 'fund' ", null, null);
		for(Stock stock : list){
			String code = stock.type+stock.code;
			try {
				pullCurrentPrice(code);
			} catch (Exception e) {
				System.out.println("error on : "+ code);
				e.printStackTrace();
			}
		}
	}
}
