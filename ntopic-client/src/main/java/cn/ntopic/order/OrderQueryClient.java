/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order;


import cn.ntopic.order.dto.OrderDTO;

import java.util.List;

/**
 * 订单查询客户端
 *
 * @author obullxl 2023年05月20日: 新增
 */
public interface OrderQueryClient {

    /**
     * 查询订单列表
     *
     * @param orderIdList 订单ID列表
     * @return 订单基本信息
     */
    List<OrderDTO> queryList(List<String> orderIdList);
}
