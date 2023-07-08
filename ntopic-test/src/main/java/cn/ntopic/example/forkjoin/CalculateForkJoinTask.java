/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.example.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 * ForkJoinTask--数据计算样例：各一个数字区间，计算数字的累加值！
 *
 * 基本思路--总体为`总-分-总`思想，类似于MapReduce思路：
 *   1. 拆分任务：根据当前任务参数，决策是否需要进行任务拆分；如果需要拆分，则本任务的结果为所有拆分任务的汇总
 *   2. 汇总任务：根据第1点思路，第1个任务为总任务-拆分子任务-汇总子任务结果，那么第1个任务的值就是最终的值
 *
 * @author obullxl 2023年05月13日: 新增
 */
public class CalculateForkJoinTask extends RecursiveTask<Integer> {
    /** 任务拆分的阈值，超过该值则任务需要拆分*/
    public static final int THRESHOLD = 10;

    /**
     * 任务参数：数据计算的开始值
     */
    private final int start;

    /**
     * 任务参数：数据计算的结束值
     */
    private final int finish;

    public CalculateForkJoinTask(int start, int finish) {
        this.start = start;
        this.finish = finish;
    }

    @Override
    public Integer compute() {
        int sum = 0;

        // 检测单个任务计算量是否符合阈值，如果超过了的话进行任务拆分
        if ((this.finish - this.start) <= THRESHOLD) {
            for (int i = start; i <= finish; i++) {
                sum += i;
            }
        } else {
            // 单个任务量超过阈值，则进行任务拆分：这里是拆成了2个任务，可根据业务实际情况拆出多个任务
            int middle = (this.start + this.finish) / 2;

            RecursiveTask<Integer> leftTask = new CalculateForkJoinTask(this.start, middle);
            RecursiveTask<Integer> rightTask = new CalculateForkJoinTask(middle + 1, this.finish);

            // 执行每一个子任务：这里只有2个子任务
            leftTask.fork();
            rightTask.fork();

            // 等待并获取每个子任务执行的结束
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();

            // 合并子任务的执行结果
            sum = leftResult + rightResult;
        }

        // 本任务的结果：可能是最终的子任务，也可能是多个子任务是汇总结果
        return sum;
    }
}
