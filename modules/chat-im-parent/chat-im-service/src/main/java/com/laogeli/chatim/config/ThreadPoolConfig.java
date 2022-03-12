package com.laogeli.chatim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * threadpool
 * @author beifang
 * @Date 2021-09-03 9:10
 **/
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();

    @Primary
    @Bean(name = "threadPool")
    public ThreadPoolTaskExecutor getAsyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        taskExecutor.setCorePoolSize(corePoolSize);
        //配置最大线程数
        taskExecutor.setMaxPoolSize(1000);
        //配置队列大小
        taskExecutor.setQueueCapacity(100000);
        // 最大线程空闲时间，超过将销毁，退回到核心线程数 设置90秒
        taskExecutor.setKeepAliveSeconds(90);
        //配置线程池中的线程的名称前缀
        taskExecutor.setThreadNamePrefix("pool-thread-");
        // 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //调度器shutdown被调用时等待当前被调度的任务完成
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
        //等待时长
        taskExecutor.setAwaitTerminationSeconds(60);
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Bean(name = "scheduledPool")
    public ThreadPoolTaskScheduler getThreadPoolTaskScheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(500);
        //配置线程池中的线程的名称前缀
        threadPoolTaskScheduler.setThreadNamePrefix("pool-thread-scheduler-");
        //调度器shutdown被调用时等待当前被调度的任务完成
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池对拒绝任务（无线程可用）的处理策略，目前只支持AbortPolicy、CallerRunsPolicy；默认为后者
        threadPoolTaskScheduler.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //等待时长
        threadPoolTaskScheduler.setAwaitTerminationSeconds(60);
        threadPoolTaskScheduler.initialize();
        return threadPoolTaskScheduler;
    }
}
