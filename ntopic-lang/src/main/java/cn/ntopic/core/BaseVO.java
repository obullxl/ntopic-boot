/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core;

import cn.ntopic.core.builder.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * VO基类
 *
 * @author obullxl 2021年06月05日: 新增
 */
public abstract class BaseVO extends ToString {

    /**
     * 扩展值
     */
    private Map<String, String> extMap;

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public Map<String, String> getExtMap() {
        if (this.extMap == null) {
            this.extMap = new HashMap<>();
        }

        return this.extMap;
    }

    public void setExtMap(Map<String, String> ntMapX) {
        this.extMap = ntMapX;
    }

}
