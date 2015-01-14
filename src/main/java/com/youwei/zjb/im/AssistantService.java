package com.youwei.zjb.im;

import org.apache.commons.lang.StringUtils;
import org.jsoup.examples.HtmlToPlainText;

import com.youwei.zjb.job.JobScheduler;
import com.youwei.zjb.util.HTMLSpirit;

public class AssistantService {
	private JobScheduler schedule = new JobScheduler();
	public String doAction(String cmd){
		cmd = HTMLSpirit.delHTMLTag(cmd);
		String msg = "完成";
		if(StringUtils.isEmpty(cmd)){
			msg="你要做什么?";
		}
		String[] arr = cmd.split(" ");
		if(arr.length==1){
			msg = "然后呢";
		}
		if(arr[0].equals("开始扫网")){
			schedule.start(arr[1]);
		}else if(arr[0].equals("停止扫网")){
			schedule.stop(arr[1]);
		}else if(arr[0].equals("设置58间隔")){
			try{
				Integer interval = Integer.valueOf(arr[1]);
				schedule.setInterval("58", interval);
			}catch(NumberFormatException ex){
				msg = "时间间隔必须是数字";
			}
		}else if(arr[0].equals("开始赶集间隔")){
			try{
				Integer interval = Integer.valueOf(arr[1]);
				schedule.setInterval("赶集", interval);
			}catch(NumberFormatException ex){
				msg = "时间间隔必须是数字";
			}
		}
		return msg;
	}
}
