package com.youwei.test.zjb.house;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.bc.sdak.Page;
import org.bc.sdak.utils.BeanUtil;
import org.bc.web.ModelAndView;
import org.junit.Before;
import org.junit.Test;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.house.ChaoXiang;
import com.youwei.zjb.house.HouseAttribute;
import com.youwei.zjb.house.HouseQuery;
import com.youwei.zjb.house.HouseService;
import com.youwei.zjb.house.HouseType;
import com.youwei.zjb.house.entity.House;

public class TestHouseService {

	@Before
	public void init(){
		StartUpListener.initDataSource();
	}
	@Test
	public void testListByQuyu(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.quyus.add("蜀山区");
		ModelAndView mv = hs.listAll(hq , new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByXinzhi(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.xingzhi = HouseAttribute.公盘;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByFangXing(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByJiaoYi(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
//		hq.jiaoyis=JiaoYi.租售+","+JiaoYi.出售;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}

	@Test
	public void testListByLeibie(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.leibie = HouseType.商铺.toString();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBySJia(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByLouXing(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByLouCeng(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.lcengStart=5;
		hq.lcengEnd=6;
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByChaoXiang(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.chaoxiang= ChaoXiang.东.toString();
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydateweituo(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateStart = "2013-09-13";
		hq.dateEnd = "2013-09-14";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydategenjin(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateStart = "2013-09-13";
		hq.dateEnd = "2013-09-14";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydateadd(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateStart = "2013-03-13";
		hq.dateEnd = "2013-09-14";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListBydateyear(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.dateStart = "2013";
		hq.dateEnd = "2013";
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	@Test
	public void testListByXPath(){
		HouseService hs = new HouseService();
		HouseQuery hq = new HouseQuery();
		hq.xpath="185";
		hq.quyus.add("包河区");
		ModelAndView mv = hs.listAll(hq,new Page<House>());
		printResult(mv);
	}
	
	private void printResult(ModelAndView mv){
		List<House> houses = (List<House>) mv.data.get("houses");
		for(House house : houses){
			System.out.println(BeanUtil.toString(house));
		}
	}
	
	@Test
	public void testUserAgent() throws IOException{
		URL url = new URL("http://m.58.com/hf/ershoufang/22571195901470x.shtml?adtype=1&entinfo=22571195901470_0&psid=121857892188567320687039891&ClickID=2&PGTID=121857892188567320687039891&ad_id=152233426518080");
		URLConnection conn = url.openConnection();
		conn.addRequestProperty("User-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3");
		conn.setDefaultUseCaches(false);
		conn.setUseCaches(false);
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(10000);
		String result = IOUtils.toString(conn.getInputStream(),"utf8");
		System.out.println(result);
	}
}
