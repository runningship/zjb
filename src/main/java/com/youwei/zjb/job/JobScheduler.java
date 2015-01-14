package com.youwei.zjb.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class JobScheduler extends Thread{

	private Map<String,HouseRentJob> jobs = new HashMap<String,HouseRentJob>();
	
	public JobScheduler(){
		Pull58Rent job58 = new Pull58Rent();
		job58.setDetailPageInterval(100);
		jobs.put(job58.getJobName(), job58);
		
		PullGJRent jobGJ = new PullGJRent();
		jobGJ.setDetailPageInterval(3000);
		jobGJ.setListPageInterval(20000);
		jobs.put(jobGJ.getJobName(), jobGJ);
		
		Pull365Rent job365 = new Pull365Rent();
		job365.setDetailPageInterval(100);
		jobs.put(job365.getJobName(), job365);
		this.start();
	}
	
	@Override
	public void run() {
		while(true){
			for(String jobName : jobs.keySet()){
				schedule(jobName);
			}
			try {
				Thread.sleep(1000);
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
		if(interval<30*1000){
			interval=30*1000;
		}
		jobs.get(jobName).setListPageInterval(interval);
	}
	
	public void setDetailPageInterval(String jobName,int interval){
		jobs.get(jobName).setDetailPageInterval(interval);
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
