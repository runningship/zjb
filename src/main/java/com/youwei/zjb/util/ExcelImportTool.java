package com.youwei.zjb.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExcelImportTool {

	private static Map<String,Integer> nums = new HashMap<String , Integer>();
	static{
		nums.put("一", 1);
		nums.put("二", 2);
		nums.put("三", 3);
		nums.put("四", 4);
		nums.put("五", 5);
		nums.put("六", 6);
		nums.put("七", 7);
		nums.put("八", 8);
		nums.put("九", 9);
	}
	
	public static void main(String[] args){
		try {
            Workbook book = Workbook.getWorkbook(new File("E:\\11.xls"));
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            // 得到单元格
            for (int i = 1; i < sheet.getRows(); i++) {
            	String sql = "insert into [dbo].[house] "
        				+ "(uid,                  did,    cid,   ztai,      area,  quyu,          lceng,          zceng,           hxf,      hxt,      hxw,    zxiu,       mji,     zjia,   djia,  seeGX,    seeHM,   seeFH,   sh,    beizhu , luduan ,      fangshi) "
        				+ "values (638  ,   694  , 693,      4,    '[area]', '[quyu]',    '[lceng]',      '[zceng]',     [hxf] , [hxt],  [hxw] , '[zxiu]',  		[mji],   [zjia] ,   [djia]  ,  0 ,           1,          1 ,    1 ,   '[beizhu]',  '[luduan]' , 'import' )";
            	String bianhao = sheet.getCell(0, i).getContents();
            	sql = sql.replace("[beizhu]", "老编号: "+bianhao);
            	String quyu = sheet.getCell(1, i).getContents();
            	String area = sheet.getCell(2, i).getContents();
            	String louceng = sheet.getCell(3, i).getContents();
            	if(louceng==null){
            		louceng="";
            	}
            	louceng = louceng.replace("+", "/").replace("-", "/").replace("楼", "");
            	String[] lcArr = louceng.split("/");
            	if(lcArr.length>0){
            		sql = sql.replace("[lceng]", lcArr[0]);
            	}
            	if(lcArr.length>1){
            		sql = sql.replace("[zceng]", lcArr[1]);
            	}
            	String mianji = sheet.getCell(4, i).getContents();
            	if(mianji==null){
            		mianji = "";
            	}
            	mianji=mianji.replace("m", "");
            	try{
            		Float mji = Float.valueOf(mianji);
            		sql = sql.replace("[mji]", mji.toString());
            	}catch(Exception ex){
            		sql = sql.replace("[mji]", "NULL");
            	}
            	
            	String fangxing = sheet.getCell(4, i).getContents();
            	int ting = fangxing.indexOf("厅");
        		try{
        			if(ting>0){
        				String str = String.valueOf(fangxing.charAt(ting-1));
        				if(nums.containsKey(str)){
        					str = nums.get(str).toString();
        				}
        				sql = sql.replace("[hxf]", str);
        			}else{
        				sql = sql.replace("[hxf]", "0");
        			}
        		}catch(Exception ex){
        			sql = sql.replace("[hxf]", "0");
        		}
        		
        		int shi = fangxing.indexOf("室");
        		try{
        			if(shi>0){
        				String str = String.valueOf(fangxing.charAt(shi-1));
        				if(nums.containsKey(str)){
        					str = nums.get(str).toString();
        				}
        				sql = sql.replace("[hxs]", str);
        			}else{
        				sql = sql.replace("[hxs]", "0");
        			}
        		}catch(Exception ex){
        			sql = sql.replace("[hxs]", "0");
        		}
        		
        		sql = sql.replace("hxw", "0");
        		
            	String zhangxiu = sheet.getCell(5, i).getContents();
            	if(zhangxiu==null){
            		zhangxiu="";
            	}
            	String zxiu = "";
            	if(zhangxiu.contains("毛")){
            		zxiu = "毛坯";
            	}
            	if(zhangxiu.contains("中")){
            		zxiu = "中装";
            	}
            	if(zhangxiu.contains("简")){
            		zxiu = "简装";
            	}
            	if(zhangxiu.contains("精")){
            		zxiu = "精装";
            	}
            	if(zhangxiu.contains("豪")){
            		zxiu = "豪装";
            	}
            	sql = sql.replace("[zxiu]", zxiu);
            	String zongjia = sheet.getCell(6, i).getContents();
            	if(zongjia==null){
            		zongjia ="";
            	}
            	zongjia = zongjia.replace("万", "");
            	try{
            		float djia = Float.valueOf(zongjia)*10000/Float.valueOf(mianji);
            		sql = sql.replace("[djia]", String.valueOf(djia));
            	}catch(Exception ex){
            		sql = sql.replace("[djia]", "NULL");
            	}
            	sql = sql.replace("[zji]", zongjia);
            	sql = sql.replace("[quyu]", quyu);
            	sql = sql.replace("area", area);
            	sql = sql.replace("[luduan]", "");
            	System.out.println(sql);
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
