/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.web;

import cn.ntopic.das.model.NTParamDO;
import cn.ntopic.service.NTUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 参数测试
 *
 * @author obullxl 2023年09月09日: 新增
 */
@RestController
public class NTParamAct {

    private final NTUserService ntUserService;

    public NTParamAct(@Qualifier("ntUserService") NTUserService ntUserService) {
        this.ntUserService = ntUserService;
    }

    @RequestMapping("/param")
    public List<NTParamDO> paramList() {
        return this.ntUserService.findUserParamList();
    }

}
