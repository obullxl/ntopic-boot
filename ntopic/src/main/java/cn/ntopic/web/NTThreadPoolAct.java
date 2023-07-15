/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.web;

import cn.ntopic.service.NTThreadPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * NTThreadPool测试
 *
 * @author obullxl 2021年06月05日: 新增
 */
@Controller("ntThreadPoolAct")
public class NTThreadPoolAct {

    @Autowired
    private NTThreadPoolService ntThreadPoolService;

    @GetMapping("/async")
    public void async() {
        this.ntThreadPoolService.asyncExecuteTest();
    }

}
