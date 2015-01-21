package com.youwei.zjb.im;

import java.util.Calendar;

import org.apache.commons.lang.StringUtils;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;

import com.youwei.zjb.job.JobScheduler;
import com.youwei.zjb.user.entity.User;
import com.youwei.zjb.util.DataHelper;
import com.youwei.zjb.util.HTMLSpirit;

public class AssistantService {
	private JobScheduler schedule = new JobScheduler();
	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public String doAction(String cmd){
		cmd = HTMLSpirit.delHTMLTag(cmd);
		String msg = "完成";
		if(StringUtils.isEmpty(cmd)){
			msg="你要做什么?";
		}
		if("help".equals(cmd)){
			StringBuilder sb = new StringBuilder();
			sb.append("swq status: ").append("扫网器状态").append("<br/>");
			sb.append("count online coco users: ").append("所有IM在线用户数").append("<br/>");
			sb.append("list online coco users: ").append("所有IM在线用户数详细信息").append("<br/>");
			sb.append("start jobName(58|365|ganji|baixing|anjuke|fang): ").append("启动指定网站的扫网任务").append("<br/>");
			sb.append("stop jobName(58|365|ganji|baixing|anjuke|fang): ").append("停止指定网站的扫网任务").append("<br/>");
			sb.append("set jobName listPageInterval 60: ").append("设置扫网任务时间间隔,单位为秒").append("<br/>");
			sb.append("clear old house: ").append("删除老房源(2个月前)").append("<br/>");
			return sb.toString();
		}
		if("swq status".equals(cmd)){
			msg = schedule.getStatus();
			return msg;
		}
		if("clear old house".equals(cmd)){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -2);
			int count = dao.execute("delete from HouseRent where dateadd<?", cal.getTime());
			return "本地清理掉2个月前("+DataHelper.sdf.format(cal.getTime())+")的老房源"+count+"条";
		}
		if("count online coco users".equals(cmd)){
			StringBuilder sb = new StringBuilder();
			for(String city : IMServer.conns.keySet()){
				sb.append(city).append(": ").append(IMServer.conns.get(city).size()).append("人在线");
				sb.append("<br/>");
			}
			return sb.toString();
		}
		if("list online coco users".equals(cmd)){
			StringBuilder sb = new StringBuilder();
			for(String city : IMServer.conns.keySet()){
				sb.append(city).append(": ").append("<br/>");
				for(Integer uid : IMServer.conns.get(city).keySet()){
					User u = dao.get(User.class, uid);
					sb.append(u.Company().namea).append("-").append(u.Department().namea).append("-").append(u.uname);
					sb.append("<br/>");
				}
			}
			return sb.toString();
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
