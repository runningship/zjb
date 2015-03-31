package com.youwei.zjb.finace;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.io.IOUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.Page;
import org.bc.sdak.SimpDaoTool;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.job.PullDataHelper;

public class StockTuiJian2 {

	static CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();

	public static void main(String[] args) throws BiffException, IOException {
		// StartUpListener.initDataSource();
		// //所有非创业板股票
		// List<Stock> list = dao.listByParams(Stock.class,
		// "from Stock where code not like '3%' ");
		//http://table.finance.yahoo.com/table.csv?s=000061.ss
		//http://table.finance.yahoo.com/table.csv?s=000061.sz
		
		cacuMoveAverage("000061");
	}

	private static void cacuMoveAverage(String code) throws BiffException, IOException {
		
		CsvListReader reader = null;
		InputStream in = null;
		try{
			URL url = new URL("http://table.finance.yahoo.com/table.csv?s="+code+".sz");
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			String result = IOUtils.toString(conn.getInputStream());
			in = conn.getInputStream();
			reader = new CsvListReader(new InputStreamReader(in), CsvPreference.EXCEL_PREFERENCE);
		}catch(Exception ex){
			if(in!=null){
				in.close();
			}
			URL url = new URL("http://table.finance.yahoo.com/table.csv?s="+code+".sz");
			URLConnection conn = url.openConnection();
			in = conn.getInputStream();
			reader = new CsvListReader(new InputStreamReader(in), CsvPreference.EXCEL_PREFERENCE);
		}
		
		List<String[]> content = new ArrayList<String[]>();
		List<String> line = new ArrayList<String>();
		int row = -1;
		float average5 = 0;
		float average10 = 0;
		float average15 = 0;
		float average20 = 0;
		float average30 = 0;
		float total = 0;
		while ((line = reader.read()) != null) {
			row++;
			if (row == 0) {
				continue;
			}
			if (row > 60) {
				break;
			}
			String[] data = line.toArray(new String[] {});
			String close = data[4];
			Float price = Float.valueOf(close);
			total += price;
			if (row == 5) {
				average5 = total / 5;
			}
			if (row == 10) {
				average10 = total / 10;
			}
			if (row == 20) {
				average20 = total / 20;
			}
			if (row == 30) {
				average30 = total / 30;
				break;
			}
		}
		System.out.println("五日均价:" + average5 + ";十日均价为:" + average10 + ";二十日均价为:" + average20 + ";三十日均价为:" + average30);
		System.out.println("方差为:"+cacuVariance2(average5 , average10 ,average20, average30));
		in.close();
	}

	
	private static float cacuVariance(Float... nums){
		float total = 0;
		//平方总值
		float pingfang = 0;
		for(float  num : nums){
			total+=num;
			pingfang+= num*num;
		}
		float average = total/nums.length;
		//平方的均值-均值的平方
		return pingfang/nums.length - average*average;
	}
	
	private static float cacuVariance2(Float... nums){
		float total = 0;
		float fangcha = 0;
		for(float  num : nums){
			total+=num;
		}
		float average = total/nums.length;
		for(float  num : nums){
			fangcha += (num-average)*(num-average);
		}
		//平方的均值-均值的平方
		return fangcha/nums.length;
	}
	
	private static void findByNewLowPrice(Stock stock, List<StockPrice> list) {
		String day = list.get(0).day;
		Collections.sort(list);
		if (day.equals(list.get(0).day)) {
			// System.out.println(stock.code);
		} else if (day.equals(list.get(1).day)) {
			System.out.println(stock.code);
		} else if (day.equals(list.get(2).day)) {
			// System.out.println(stock.code);
		}
	}
}
