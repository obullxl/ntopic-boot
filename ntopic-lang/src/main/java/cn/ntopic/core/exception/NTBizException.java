/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.exception;

import cn.ntopic.core.NTEnum;

/**
 * 业务服务异常
 *
 * @author obullxl 2021年03月20日: 新增
 */
public class NTBizException extends RuntimeException {

    /**
     * 错误码枚举
     */
    private final NTEnum ntEnum;

    private NTBizException(NTEnum ntEnum) {
        super(ntEnum.getCode() + "::" + ntEnum.getMessage());
        this.ntEnum = ntEnum;
    }

    private NTBizException(NTEnum ntEnum, Object... args) {
        super(ntEnum.getCode() + "::" + String.format(ntEnum.getMessage(), args));
        this.ntEnum = ntEnum;
    }

    /**
     * 构建异常
     */
    public static NTBizException of(NTEnum ntEnum) {
        return new NTBizException(ntEnum);
    }

    /**
     * 构建异常
     */
    public static NTBizException of(NTEnum ntEnum, Object... args) {
        if (args == null || args.length <= 0) {
            return new NTBizException(ntEnum);
        } else {
            return new NTBizException(ntEnum, args);
        }
    }

    // ~~~~~~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~~~ //

    public NTEnum getNtEnum() {
        return ntEnum;
    }

}
