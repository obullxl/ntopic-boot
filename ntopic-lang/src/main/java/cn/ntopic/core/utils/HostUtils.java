/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.utils;

import cn.ntopic.LogConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;

/**
 * 服务器信息
 *
 * @author obullxl 2021年03月27日: 新增
 */
public class HostUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogConstants.UTIL);

    /**
     * 服务器名
     */
    private static String HOST_NAME = "";

    /**
     * 获取服务器名
     */
    public static String fetchHostName() {
        if (StringUtils.isBlank(HOST_NAME)) {
            try {
                HOST_NAME = InetAddress.getLocalHost().getHostName();
            } catch (Throwable e) {
                LOGGER.error("HostUtils:获取服务器名异常.", e);
            }
        }

        return HOST_NAME;
    }

}
