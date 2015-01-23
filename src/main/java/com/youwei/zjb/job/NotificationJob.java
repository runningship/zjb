package com.youwei.zjb.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.bc.sdak.SimpDaoTool;

import com.youwei.zjb.StartUpListener;
import com.youwei.zjb.im.IMServer;
import com.youwei.zjb.user.entity.Department;

public class NotificationJob extends AbstractJob{

	public static void main(String[] args){
		StartUpListener.initDataSource();
		NotificationJob job = new NotificationJob();
		job.work();
	}
	@Override
	public void work() {
		//早上5点以后运行
		Calendar cal = Calendar.getInstance();
		int hour = cal.get(Calendar.HOUR_OF_DAY);
//		if(hour>=10 || hour<=5){
//			//5点到10点之间运行
//			return;
//		}
		if(System.currentTimeMillis()-this.getLastRunTime()<24*3600*1000){
			//离上次运行不到1天
			return;
		}
//		cal.set(Calendar.HOUR_OF_DAY, 23);
//		cal.set(Calendar.MINUTE, 59);
//		cal.set(Calendar.SECOND, 59);
		List<Department> list = SimpDaoTool.getGlobalCommonDaoService().listByParams(Department.class, "from Department where fid<>1 and deadline<?"	, cal.getTime());
		StringBuilder sb = new StringBuilder("以下为今天到期的店面:").append("<br/>");
		for(Department dept : list){
			sb.append(dept.Company().namea+" "+dept.namea).append("<br/>");
		}
		System.out.println(sb.toString());
		IMServer.sendMsgToGroup(1, sb.toString());
	}

	@Override
	public String getJobName() {
		return "notification";
	}

}
