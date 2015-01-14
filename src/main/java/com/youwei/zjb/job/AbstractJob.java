package com.youwei.zjb.job;

public abstract class AbstractJob implements HouseRentJob{

	private int interval = 1000*60;
	private boolean allowRun = false;
	private long lastRunTime = -1;
	@Override
	public void setInterval(int interval) {
		this.interval = interval;
	}

	@Override
	public int getInterval() {
		return interval;
	}

	@Override
	public void setAllowRun(boolean allow) {
		this.allowRun = allow;
	}

	@Override
	public boolean isAllowRun() {
		return allowRun;
	}

	@Override
	public long getLastRunTime() {
		return this.lastRunTime;
	}

	@Override
	public void setLastRunTime(long lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

}
