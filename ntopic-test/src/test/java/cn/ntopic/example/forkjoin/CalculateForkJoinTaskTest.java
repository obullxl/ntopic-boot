/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.example.forkjoin;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * CalculateForkJoinTask--测试验证
 *
 * @author obullxl 2023年05月13日: 新增
 */
public class CalculateForkJoinTaskTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {
        // 构建任务：累加1~100值
        RecursiveTask<Integer> task = new CalculateForkJoinTask(1, 100);

        // 执行任务
        Future<Integer> result = ForkJoinPool.commonPool().submit(task);

        // 验证结果
        Assert.assertEquals(5050, result.get().intValue());
    }
}

