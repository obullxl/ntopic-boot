/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.timer;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Timer;

/**
 * 调度器
 *
 * @author obullxl 2021年06月19日: 新增
 */
public final class NTTimerX {

    /**
     * 调度器
     */
    private final Timer ntTimer;

    /**
     * CTOR
     */
    public NTTimerX(String timerName) {
        String name = StringUtils.defaultIfEmpty(timerName, "NTTimerX");
        ntTimer = new Timer(name, true);
    }

    /**
     * 延迟执行
     *
     * @param delayMillis 延迟毫秒数
     */
    public void schedule(NTTaskX task, long delayMillis) {
        this.ntTimer.schedule(task, delayMillis);
    }

    /**
     * 定点执行
     *
     * @param time 执行时间点
     */
    public void schedule(NTTaskX task, Date time) {
        this.ntTimer.schedule(task, time);
    }

    /**
     * 首次延迟执行之后，按照固定周期循环执行
     *
     * @param delayMillis  首次延迟毫秒数
     * @param periodMillis 固定周期毫秒数
     */
    public void scheduleAtFixedRate(NTTaskX task, long delayMillis, long periodMillis) {
        this.ntTimer.schedule(task, delayMillis, periodMillis);
    }

    /**
     * 首次定点执行之后，按照固定周期重复执行
     *
     * @param firstTime    首次执行时间
     * @param periodMillis 固定周期毫秒数
     */
    public void scheduleAtFixedRate(NTTaskX task, Date firstTime, long periodMillis) {
        this.ntTimer.schedule(task, firstTime, periodMillis);
    }

}
