/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.value;

import cn.ntopic.core.builder.ToString;

/**
 * 1个对象组
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTObject<ONE> extends ToString {

    /**
     * 第1个对象
     */
    private ONE one;

    protected NTObject(ONE one) {
        this.one = one;
    }

    /**
     * 构建对象组
     */
    public static <ONE> NTObject<ONE> of(ONE one) {
        return new NTObject<>(one);
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public ONE getOne() {
        return one;
    }

    public void setOne(ONE one) {
        this.one = one;
    }

}
