package com.youwei.zjb.job;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.bc.sdak.CommonDaoService;
import org.bc.sdak.SimpDaoTool;
import org.bc.sdak.utils.LogUtil;
import org.bc.web.ThreadSession;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.youwei.zjb.house.entity.District;
import com.youwei.zjb.sys.CityService;
import com.youwei.zjb.util.LngAndLatUtil;

public class AreaCoordinateTask implements Job {

	CommonDaoService dao = SimpDaoTool.getGlobalCommonDaoService();
	
   public void execute(JobExecutionContext jExeCtx) throws JobExecutionException {
//      LogUtil.info("TestJob run successfully...");
	   CityService cs = new CityService();
	   JSONArray citys = cs.getCitys();
	   for(int i=0;i<citys.size();i++){
		   JSONObject jobj = citys.getJSONObject(i);
		   try{
			   getAreaCoordinate((String)jobj.get("py"));
		   }catch(Exception ex){
			   LogUtil.log(Level.WARN, "获取楼盘坐标失败,city="+jobj.getString("py"), ex);
		   }
	   }
   }

   private void getAreaCoordinate(String cityPy){
	   ThreadSession.setCityPY(cityPy);
	  List<District> list =  dao.listByParams(District.class, "from  District where maplat is null or maplng is null");
	  for(District d : list){
		  Map<String, Double> map = LngAndLatUtil.getLngAndLat(d.name,ThreadSession.getCityPY());
		  if(map.get("lat") == null||map.get("lng") == null){
			  d.maplat = 0f ;
			  d.maplng = 0f ;
		  }else{
			  d.maplat = map.get("lat").floatValue();
			  d.maplng = map.get("lng").floatValue();
			  LogUtil.info("cityPy="+cityPy+",成功获得小区 "+d.name+" 的坐标,lat="+d.maplat +",lng="+d.maplng);
		  }
		  dao.saveOrUpdate(d);
	  }
	  LogUtil.info("cityPy="+cityPy+", 楼盘坐标处理完成..");
   }
}