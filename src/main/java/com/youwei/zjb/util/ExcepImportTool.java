package com.youwei.zjb.util;

import java.io.File;

import org.apache.commons.lang.StringUtils;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class ExcepImportTool {

	public static void main(String[] args){
		try {
            Workbook book = Workbook.getWorkbook(new File("E:\\temp\\导入(1).xls"));
            // 获得第一个工作表对象
            Sheet sheet = book.getSheet(0);
            // 得到单元格
            for (int i = 1; i < sheet.getRows(); i++) {
            	String sql = "insert into [dbo].[house] "
        				+ "(uid,                  did,    cid,   ztai,      area,     dhao,      lceng,          zceng,       lxing,       hxf,      hxt,      hxw,    zxiu,       mji,     zjia,   djia,  seeGX,seeHM,   seeFH,   sh,    beizhu , luduan ,      fangshi) "
        				+ "values (638  ,   694  , 693,      4,    '[area]', '[dhao]',    '[lceng]',      '[zceng]',    '[lxing]' ,  [hxf] , [hxt],  [hxw] , '[zxiu]',  		[mji],   [zjia] ,   [djia]  ,  0 ,           1,          1 ,  1 ,   '[beizhu]',  '[luduan]' , 'import' )";
               String area = sheet.getCell(0, i).getContents();
               sql = sql.replace("[area]", area);
               String luduan = sheet.getCell(1, i).getContents();
               sql = sql.replace("[luduan]", luduan);
               String lceng = sheet.getCell(2, i).getContents();
               sql = sql.replace("[lceng]", lceng);
               String zceng = sheet.getCell(3, i).getContents();
               sql = sql.replace("[zceng]", zceng);
               String hxf = sheet.getCell(4, i).getContents();
               if(StringUtils.isEmpty(hxf)){
            	   sql = sql.replace("[hxf]", "NULL");
               }else{
            	   sql = sql.replace("[hxf]", hxf);
               	}
               
               String hxt = sheet.getCell(5, i).getContents();
               if(StringUtils.isEmpty(hxt)){
            	   sql = sql.replace("[hxt]", "NULL");
               }else{
            	   sql = sql.replace("[hxt]", hxt);
               }
               
               String hxw = sheet.getCell(6, i).getContents();
               if(StringUtils.isEmpty(hxw)){
            	   sql = sql.replace("[hxw]", "NULL");
               }else{
            	   sql = sql.replace("[hxw]", hxw);
               }
               
               String mji = sheet.getCell(7, i).getContents();
               
               if(StringUtils.isEmpty(mji)){
            	   sql = sql.replace("[mji]", "NULL");
               }else{
            	   sql = sql.replace("[mji]", mji);
               }
               
               String zjia = sheet.getCell(8, i).getContents();
               if(StringUtils.isEmpty(zjia)){
            	   sql = sql.replace("[zjia]", "NULL");
               }else{
            	   sql = sql.replace("[zjia]", zjia);
               }
               
               try{
	               Float djia = Float.valueOf(zjia)*10000/Float.valueOf(mji);
	               sql = sql.replace("[djia]", djia.toString());
               }catch(Exception e){
            	   //e.printStackTrace();
            	   sql = sql.replace("[djia]", "NULL");
               }
               String zxiu = sheet.getCell(9 ,i).getContents();
               sql = sql.replace("[zxiu]", zxiu);
               String beizhu = sheet.getCell(10, i).getContents();
               sql = sql.replace("[beizhu]", beizhu);
               String year = sheet.getCell(11, i).getContents();
               sql = sql.replace("[year]", year);
               String lxing = sheet.getCell(12, i).getContents();
               sql = sql.replace("[lxing]", lxing);
               String dhao = sheet.getCell(13, i).getContents();
               sql = sql.replace("[dhao]", dhao);
               System.out.println(sql);
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
