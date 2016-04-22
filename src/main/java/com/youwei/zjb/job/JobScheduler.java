package com.youwei.zjb.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.bc.web.ThreadSession;

import com.youwei.zjb.cache.ConfigCache;

public class JobScheduler extends Thread{

	private Map<String,HouseRentJob> jobs = new HashMap<String,HouseRentJob>();
	
	public JobScheduler(){
//		Pull58Rent job58 = new Pull58Rent();
//		job58.setDetailPageInterval(10000);
//		jobs.put(job58.getJobName(), job58);
//		
//		PullGJRent jobGJ = new PullGJRent();
//		jobGJ.setDetailPageInterval(1000);
//		jobGJ.setListPageInterval(40000);
//		jobs.put(jobGJ.getJobName(), jobGJ);
		
		Pull365Rent job365 = new Pull365Rent();
		job365.setDetailPageInterval(100);
		jobs.put(job365.getJobName(), job365);
		
//		PullAJKRent jobAjk = new PullAJKRent();
//		jobAjk.setDetailPageInterval(100);
//		jobs.put(jobAjk.getJobName(), jobAjk);
		
//		PullBXRent jobBaixing = new PullBXRent();
//		jobBaixing.setDetailPageInterval(100);
//		jobs.put(jobBaixing.getJobName(), jobBaixing);
		
		PullFangRent jobSofang = new PullFangRent();
		jobSofang.setDetailPageInterval(100);
		jobs.put(jobSofang.getJobName(), jobSofang);
		
//		NotificationJob nJob = new NotificationJob();
//		nJob.setListPageInterval(3600);
//		jobs.put(nJob.getJobName(), nJob);
		
		String flag = ConfigCache.get("startJob", "1");
		if("1".equals(flag)){
			this.start();
		}
	}
	
	@Override
	public void run() {
		while(true){
			for(String jobName : jobs.keySet()){
				schedule(jobName);
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}



	private void schedule(String jobName) {
		final HouseRentJob job = jobs.get(jobName);
		if(job.isAllowRun() && job.isRunning()==false){
			if(job.getLastRunTime()==-1 || System.currentTimeMillis()-job.getLastRunTime()>job.getListPageInterval()){
				new Thread(){
					
					@Override
					public void run() {
						job.setRunning(true);
						ThreadSession.setCityPY("hefei");
						job.work();
						job.setRunning(false);
						job.setLastRunTime(System.currentTimeMillis());
					}
				}.start();
			}
		}
	}

	public void start(String jobName){
		if(StringUtils.isEmpty(jobName)){
			return;
		}
		if(jobs.containsKey(jobName)){
			jobs.get(jobName).setAllowRun(true);
		}
	}
	
	public void stop(String jobName){
		if(StringUtils.isEmpty(jobName)){
			return;
		}
		if(jobs.containsKey(jobName)){
			jobs.get(jobName).setAllowRun(false);
		}
	}
	
	public void setListPageInterval(String jobName,int interval){
		if(interval<30){
			interval=30;
		}
		jobs.get(jobName).setListPageInterval(interval*1000);
	}
	
	public void setDetailPageInterval(String jobName,int interval){
		jobs.get(jobName).setDetailPageInterval(interval*1000);
	}
	
	public String getStatus(){
		StringBuilder sb = new StringBuilder();
		for(HouseRentJob job : jobs.values()){
			sb.append(job.getJobName()).append(": ");
			if(job.isAllowRun()){
				sb.append("已经启动");
			}else{
				sb.append("已经停止");
			}
			if(job.isRunning()){
				sb.append(" 正在运行");
			}else{
				sb.append(" 处于运行间隔期");
			}
			sb.append(",列表页面刷新间隔为").append(job.getListPageInterval()/1000f).append("秒");
			sb.append(",详情页面打开时间间隔为").append(job.getDetailPageInterval()/1000f).append("秒");
			sb.append("<br/>");
		}
		return sb.toString();
	}
}
