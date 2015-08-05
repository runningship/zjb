package com.youwei.zjb.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ThreadSession;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.util.LngAndLatUtil;

public class AreaCoordinateTask implements Job {

   private Logger log = Logger.getLogger(AreaCoordinateTask.class);

   public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
//      LogUtil.info("TestJob run successfully...");
	   CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	   
	   ThreadSession.setCityPY("wuhu");
	  List<District> list =  dao.listByParams(District.class, "from  District where 1=1");
	  for(District d : list){
		  Map<String, Double> map = LngAndLatUtil.getLngAndLat(d.name,ThreadSession.getCityPY());
		  if(map.get("lat") == null||map.get("lng") == null){
			  d.maplat = 0f ;
			  d.maplng = 0f ;
		  }else{
			  d.maplat = map.get("lat").floatValue();
			  d.maplng = map.get("lng").floatValue();
		  }
		  dao.saveOrUpdate(d);
	  }
   }

}