/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order;

import cn.ntopic.order.dto.OrderDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单查询客户端实现
 *
 * @author obullxl 2023年05月20日: 新增
 */
@Component("orderQueryClient")
public class OrderQueryClientImpl implements OrderQueryClient {

    @Override
    public List<OrderDTO> queryList(List<String> orderIdList) {
        // 为了方便测试，这里直接返回null
        return new ArrayList<>();
    }
}
