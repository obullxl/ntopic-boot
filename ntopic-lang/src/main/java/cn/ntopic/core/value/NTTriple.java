/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.value;

/**
 * 3个对象组
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class NTTriple<ONE, TWO, THREE> extends NTTuple<ONE, TWO> {

    /**
     * 第3个对象
     */
    private THREE three;

    protected NTTriple(ONE one, TWO two, THREE three) {
        super(one, two);
        this.three = three;
    }

    /**
     * 构建对象组
     */
    public static <ONE, TWO, THREE> NTTriple<ONE, TWO, THREE> of(ONE one, TWO two, THREE three) {
        return new NTTriple<>(one, two, three);
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public THREE getThree() {
        return three;
    }

    public void setThree(THREE three) {
        this.three = three;
    }

}
