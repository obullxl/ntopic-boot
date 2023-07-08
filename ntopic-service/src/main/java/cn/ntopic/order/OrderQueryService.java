/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order;

import cn.ntopic.ListResult;
import cn.ntopic.order.model.OrderModel;

import java.util.List;

/**
 * 订单服务
 *
 * @author obullxl 2023年05月20日: 新增
 */
public interface OrderQueryService {

    /**
     * 根据订单ID查询订单列表，仅返回最近3个月的ID信息
     *
     * @param orderIdList 订单ID列表
     * @return 最近3个月的订单信息
     */
    ListResult<OrderModel> findList(List<String> orderIdList);

}
