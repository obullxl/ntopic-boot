/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.web;

import cn.ntopic.LogConstants;
import cn.ntopic.core.NTAuthX;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 登录控制器
 *
 * @author obullxl 2021年06月19日: 新增
 */
@Controller("ntLoginAct")
public class NTLoginAct {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.WEB);

    @RequestMapping("/")
    public String showHome(ModelMap modelMap) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("当前认证信息：" + auth);

        String name = auth.getName();
        LOGGER.info("当前登陆用户：" + name);

        modelMap.put("msg", "NTopic用户:" + auth);
        return "NTopic";
    }

    @RequestMapping("/login")
    public String showLogin() {
        return "NTLogin";
    }

    @RequestMapping("/admin")
    @ResponseBody
    @PreAuthorize(NTAuthX.HAS_ROLE_ADMIN)
    public String printAdmin() {
        return "如果你看见这句话，说明你有ROLE_ADMIN角色";
    }

    @RequestMapping("/user")
    @ResponseBody
    @PreAuthorize(NTAuthX.HAS_ROLE_USER)
    public String printUser() {
        return "如果你看见这句话，说明你有ROLE_USER角色";
    }

}
