package com.youwei.zjb.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class JobScheduler extends Thread{

	private Map<String,HouseRentJob> jobs = new HashMap<String,HouseRentJob>();
	
	public JobScheduler(){
		Pull58Rent job58 = new Pull58Rent();
		jobs.put(job58.getJobName(), job58);
		
		PullGJRent jobGJ = new PullGJRent();
		jobs.put(jobGJ.getJobName(), jobGJ);
		
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
		if(job.isAllowRun()){
			if(job.getLastRunTime()==-1 || System.currentTimeMillis()-job.getLastRunTime()>job.getInterval()){
				new Thread(){

					@Override
					public void run() {
						job.work();
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
	
	public void setInterval(String jobName,int interval){
		if(interval<30*1000){
			interval=30*1000;
		}
		jobs.get(jobName).setInterval(interval);
	}
}
