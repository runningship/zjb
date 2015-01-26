package com.youwei.zjb.job;

public abstract class AbstractJob implements HouseRentJob{

	private int listPageInterval = 1000*60;
	private int detailPageInterval = 1000*5;
	private boolean allowRun = true;
	private long lastRunTime = -1;
	private boolean running = false;
	
	@Override
	public void setListPageInterval(int interval) {
		this.listPageInterval = interval;
	}

	@Override
	public int getListPageInterval() {
		return listPageInterval;
	}

	@Override
	public void setDetailPageInterval(int interval) {
		this.detailPageInterval = interval;
	}

	@Override
	public int getDetailPageInterval() {
		return detailPageInterval;
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

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setRunning(boolean running) {
		this.running = running;
	}
}
