package com.youwei.zjb.job;

public interface HouseRentJob {

	public void work();
	
	public String getJobName();
	
	public void setListPageInterval(int interval);
	
	public int getListPageInterval();
	
	public void setDetailPageInterval(int interval);
	
	public int getDetailPageInterval();
	
	public void setAllowRun(boolean allow);
	
	public boolean isAllowRun();
	
	public long getLastRunTime();
	
	public void setLastRunTime(long lastRunTime);
	
	public boolean isRunning();
	
	public void setRunning(boolean running);
}
