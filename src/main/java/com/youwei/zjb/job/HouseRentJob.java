package com.youwei.zjb.job;

public interface HouseRentJob {

	public void work();
	
	public String getJobName();
	
	public void setInterval(int interval);
	
	public int getInterval();
	
	public void setAllowRun(boolean allow);
	
	public boolean isAllowRun();
	
	public long getLastRunTime();
	
	public void setLastRunTime(long lastRunTime);
}
