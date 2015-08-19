package com.youwei.test.zjb.sys;

import com.youwei.zjb.util.MailUtil;
public class mailTest {
    public static void  main(String args[]){
        try {
        	MailUtil.send_email("253187898@qq.com", "中介宝试用申请", "测试信息");
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}