/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.service.impl;

import cn.ntopic.BeanConstants;
import cn.ntopic.service.NTThreadPoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 线程池服务测试实现
 *
 * @author obullxl 2021年06月05日: 新增
 */
@Service("ntThreadPoolService")
public class NTThreadPoolServiceImpl implements NTThreadPoolService {
    private static final Logger LOGGER = LoggerFactory.getLogger(NTThreadPoolService.class);

    /**
     * 异步执行测试
     */
    @Override
    @Async(BeanConstants.NT_THREAD_POOL)
   public void asyncExecuteTest() {
        LOGGER.info("start executeAsync");

        System.out.println("异步线程要做的事情");
        System.out.println("可以在这里执行批量插入等耗时的事情");

        LOGGER.info("end executeAsync");
    }

}
