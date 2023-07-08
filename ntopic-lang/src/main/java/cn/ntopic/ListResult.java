/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic;

import java.util.List;

/**
 * 请求结果列表包装器
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class ListResult<T> extends Result<List<T>> {

    public ListResult(boolean success) {
        super(success);
    }

}
