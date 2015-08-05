package com.youwei.zjb.job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.youwei.test.zjb.quartz.TestJob;
import com.youwei.zjb.StartUpListener;

public class ZJBQuartzScheduler {

    public static void AreaCoordinatStart() {
    	StartUpListener.initDataSource();
        try {
            JobDetail job = JobBuilder.newJob(AreaCoordinateTask.class)
            	       .withIdentity("AreaCoordinateTask")
            	       .build();

          //Trigger the job to run on the next round minute
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withSchedule(
                                 SimpleScheduleBuilder.simpleSchedule()
                                 .withIntervalInHours(8)
                                 .repeatForever())
                                               .build();

            // CronTrigger the job to run on the every 20 seconds
//            CronTrigger cronTrigger = TriggerBuilder.newTrigger()
//                                         .withIdentity("crontrigger","crontriggergroup1")
//                                         .withSchedule(CronScheduleBuilder.cronSchedule("10 * * * * ?"))
//                                         .build();
            
            SchedulerFactory schFactory = (SchedulerFactory) new StdSchedulerFactory();
            Scheduler sch = schFactory.getScheduler(); 
            
            	// Tell quartz to schedule the job using our trigger
            sch.start();
            sch.scheduleJob(job, trigger);

        } catch (Exception se) {
            se.printStackTrace();
        }
    }
}