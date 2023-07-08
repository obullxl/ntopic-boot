/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.value;

/**
 * 2个对象组
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTTuple<ONE, TWO> extends NTObject<ONE> {

    /**
     * 第2个对象
     */
    private TWO two;

    protected NTTuple(ONE one, TWO two) {
        super(one);
        this.two = two;
    }

    /**
     * 构建对象组
     */
    public static <ONE, TWO> NTTuple<ONE, TWO> of(ONE one, TWO two) {
        return new NTTuple<>(one, two);
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public TWO getTwo() {
        return two;
    }

    public void setTwo(TWO two) {
        this.two = two;
    }

}
