package com.youwei.zjb.task;

import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.TransactionalServiceHelper;
import org.bc.web.ThreadSession;

import com.youwei.zjb.KeyConstants;
import com.youwei.zjb.StartUpListener;

public class TaskTest {

	static CommonDaoService dao = TransactionalServiceHelper.getTransactionalService(CommonDaoService.class);
	public static void main(String[] args){
		StartUpListener.initDataSource();
//		ThreadSession.setCityPY("hefei");
//		Task task = dao.getUniqueByKeyValue(Task.class, "name", "58阜阳");
//		TaskExecutor te = new TaskExecutor(task);
//		if(task.taskOn==0){
//			return;
//		}
////		if(task.status==KeyConstants.Task_Running){
////			return;
////		}
//		task.status = KeyConstants.Task_Running;
//		dao.saveOrUpdate(task);
//		te.execute();
//		task.status = KeyConstants.Task_Stop;
//		ThreadSession.setCityPY("hefei");
//		dao.saveOrUpdate(task);
		
		TaskScheduler ts = new TaskScheduler();
		ts.start();
	}
}
