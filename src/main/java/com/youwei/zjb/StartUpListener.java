package com.youwei.zjb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspFactory;

import org.bc.sdak.MutilSessionFactoryBuilder;
import org.bc.sdak.SQL2008Dialect;
import org.bc.sdak.SessionFactoryMapper;
import org.bc.web.ModuleManager;
import org.bc.web.PublicFieldSupportingELResolver;
import org.hibernate.cfg.AvailableSettings;

import com.youwei.zjb.cache.ConfigCache;
import com.youwei.zjb.im.IMServer;
import com.youwei.zjb.job.JobScheduler;
import com.youwei.zjb.job.ZJBQuartzScheduler;

public class StartUpListener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent event) {
		initDataSource();
		initModule();
		JspFactory.getDefaultFactory()
        .getJspApplicationContext(event.getServletContext())
        .addELResolver(new PublicFieldSupportingELResolver());
//		TaskScheduler ts = new TaskScheduler();
//		ts.start();
//		try {
//			IMServer.startUp();
//		} catch (Throwable e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		JobScheduler pullRentHouseJob = new JobScheduler();
		ZJBQuartzScheduler.AreaCoordinatStart();
	}

	private void initModule() {
		ModuleManager.add("com.youwei.zjb");
	}

	public static synchronized void initDataSource(){
		MutilSessionFactoryBuilder.sfm = new SessionFactoryMapper(){

			@Override
			public String getKey() {
				return ThreadSessionHelper.getCityPinyin();
			}

			@Override
			public Map<String, String> getSettings() {
				Map<String,String> settings = new HashMap<String , String>();
//				settings.put(AvailableSettings.URL, "jdbc:mysql://localhost:3306/ihouse?characterEncoding=utf-8");
//				settings.put(AvailableSettings.USER, "root");
//				settings.put(AvailableSettings.PASS, "");
				settings.put(AvailableSettings.SHOW_SQL, "false");
				settings.put(AvailableSettings.GENERATE_STATISTICS, "true");
//				settings.put(AvailableSettings.DRIVER, "com.microsoft.sqlserver.jdbc.SQLServerDriver");
//				settings.put(AvailableSettings.DRIVER, "com.p6spy.engine.spy.P6SpyDriver");
				
//				settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.SQLServer2008Dialect");
				settings.put(AvailableSettings.DIALECT, SQL2008Dialect.class.getName());
				
//				settings.put(AvailableSettings.DRIVER, "com.mysql.jdbc.Driver");
//				settings.put(AvailableSettings.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
				
				settings.put(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//				settings.put(AvailableSettings.HBM2DDL_AUTO, "update");
				settings.put(AvailableSettings.POOL_SIZE, "1");
				settings.put(AvailableSettings.CACHE_REGION_FACTORY, "org.hibernate.cache.ehcache.EhCacheRegionFactory");
				settings.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
				
//				settings.put(AvailableSettings.PROXOOL_XML, "proxool.xml");//相对目录为classes
				settings.put(AvailableSettings.PROXOOL_XML, ConfigCache.get(ThreadSessionHelper.getCityPinyin()+"_proxool_xml" ,ThreadSessionHelper.getCityPinyin()+"_proxool.xml"));//相对目录为classes
				settings.put(AvailableSettings.PROXOOL_EXISTING_POOL, "false");
				settings.put(AvailableSettings.PROXOOL_POOL_ALIAS, ThreadSessionHelper.getCityPinyin());
				
//				settings.put("annotated.packages", HouseRent.class.getPackage().getName());
				settings.put("annotated.packages", "com.youwei.zjb");
				return settings;
			}
			
		};
		
//		SessionFactoryBuilder.applySettings(settings);
//		SimpDaoTool.getGlobalCommonDaoService().getUnique(User.class, 0);
	}
}
