package com.youwei.zjb.task;

import java.util.List;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ThreadSession;

import com.youwei.zjb.KeyConstants;

public class TaskScheduler extends Thread{

	CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public void schedule(){
		ThreadSession.setCityPY("hefei");
		List<Task> list = dao.listByParams(Task.class, "from Task where taskOn=1 and  status=?", KeyConstants.Task_Stop);
		for(Task task : list){
			TaskExecutor te = new TaskExecutor(task);
			te.start();
		}
	}
	
	@Override
	public void run() {
		while(true){
			LogUtil.info("task schedular running");
			try{
				schedule();
			}catch(Exception ex){
				ex.printStackTrace();
			}
			try {
				Thread.sleep(30*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}
}
