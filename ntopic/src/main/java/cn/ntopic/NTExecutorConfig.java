/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic;

import cn.ntopic.core.concurrent.NTThreadPoolTaskExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 *
 * @author obullxl 2021年06月05日: 新增
 */
@Configuration
@EnableAsync
public class NTExecutorConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.EXECUTOR);

    /**
     * 核心线程数量
     */
    @Value("${ntopic.executor.core_pool_size}")
    private int corePoolSize;

    /**
     * 最大线程数量
     */
    @Value("${ntopic.executor.max_pool_size}")
    private int maxPoolSize;

    /**
     * 队列长度
     */
    @Value("${ntopic.executor.queue_capacity}")
    private int queueCapacity;

    /**
     * 线程池前缀
     */
    @Value("${ntopic.executor.thread_name_prefix}")
    private String namePrefix;

    /**
     * 拒绝策略
     */
    @Value("${ntopic.executor.rejected_execution_handler}")
    private Class<? extends RejectedExecutionHandler> rejectedExecutionHandler;

    /**
     * 构建线程池
     */
    @Bean(BeanConstants.NT_THREAD_POOL)
    public Executor ntThreadPool() throws Exception {
        LOGGER.info("start ntThreadPool");

        ThreadPoolTaskExecutor executor = new NTThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setQueueCapacity(this.queueCapacity);
        executor.setThreadNamePrefix(this.namePrefix);

        // 拒绝策略
        RejectedExecutionHandler handler;
        if (this.rejectedExecutionHandler != null) {
            handler = this.rejectedExecutionHandler.newInstance();
        } else {
            handler = new ThreadPoolExecutor.CallerRunsPolicy();
        }

        executor.setRejectedExecutionHandler(handler);

        // 执行初始化
        executor.initialize();

        return executor;
    }

}
