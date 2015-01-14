package com.youwei.zjb.im;

import org.apache.commons.lang.StringUtils;

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
		if("status".equals(cmd)){
			msg = schedule.getStatus();
			return msg;
		}
		String[] arr = cmd.split(" ");
		if(arr.length==1){
			msg = "然后呢";
		}
		if(arr[0].equals("start")){
			schedule.start(arr[1]);
		}else if(arr[0].equals("stop")){
			schedule.stop(arr[1]);
		}else if(arr[0].equals("set")){
			if(arr.length<4){
				msg = "命令不完整";
				return msg;
			}
			try{
				String jobName = arr[1];
				String intervalName  = arr[2];
				Integer interval = Integer.valueOf(arr[3]);
				if("detailPageInterval".equals(intervalName)){
					schedule.setDetailPageInterval(jobName, interval);
				}else if("listPageInterval".equals(intervalName)){
					schedule.setListPageInterval(jobName, interval);
				}
				msg = "设置成功";
			}catch(NumberFormatException ex){
				msg = "时间间隔必须是数字";
			}
		}
		return msg;
	}
}
