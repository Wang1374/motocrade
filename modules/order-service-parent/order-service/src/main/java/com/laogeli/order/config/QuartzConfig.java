package com.laogeli.order.config;

import com.laogeli.order.Job.OrderOilJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;


@Configuration
public class QuartzConfig {

    // 定义任务详情
    @Bean
    public JobDetail orderOilJobDetail() {
        return JobBuilder.newJob(OrderOilJob.class)
                .withIdentity("OrderOilJob")
                .storeDurably()
                .build();
    }

    // 定义触发器
    @Bean
    public Trigger orderOilTrigger() {
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInMinutes(30) //设置时间周期单位分,目前设置为2分钟一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(orderOilJobDetail())
                .withIdentity("OrderOilJob")
                .withSchedule(scheduleBuilder)
                .build();
    }

    // 项目启动，执行一次任务
    @Bean
    public SimpleTriggerFactoryBean SimpleTriggerBean() {
        SimpleTriggerFactoryBean simpleTriggerFactoryBean = new SimpleTriggerFactoryBean();
        simpleTriggerFactoryBean.setJobDetail(orderOilJobDetail());
        simpleTriggerFactoryBean.setStartDelay(500);
        simpleTriggerFactoryBean.setRepeatInterval(0);
        simpleTriggerFactoryBean.setRepeatCount(0);
        return simpleTriggerFactoryBean;
    }
}
