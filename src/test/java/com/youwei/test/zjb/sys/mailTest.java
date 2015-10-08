package com.youwei.test.zjb.sys;

import java.util.ArrayList;
import java.util.List;

import com.youwei.zjb.util.MailUtil;
public class mailTest {
    public static void  main(String args[]){
        try {
        	List<String> toList = new ArrayList<String>();
    		toList.add("253187898@qq.com");
    		toList.add("7687849@qq.com");
        	MailUtil.send_email(toList, "中介宝试用申请", "测试信息");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}