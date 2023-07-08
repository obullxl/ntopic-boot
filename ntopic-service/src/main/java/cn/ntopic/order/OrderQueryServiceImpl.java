/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.order;

import cn.ntopic.ListResult;
import cn.ntopic.order.dto.OrderDTO;
import cn.ntopic.order.model.OrderModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单服务实现
 *
 * @author obullxl 2023年05月20日: 新增
 */
@Component("orderQueryService")
public class OrderQueryServiceImpl implements OrderQueryService {

    /**
     * 订单查询客户端，远程服务不可控
     */
    private final OrderQueryClient orderQueryClient;

    public OrderQueryServiceImpl(@Qualifier("orderQueryClient") OrderQueryClient orderQueryClient) {
        this.orderQueryClient = orderQueryClient;
    }

    @Override
    public ListResult<OrderModel> findList(List<String> orderIdList) {
        // 参数校验
        if (CollectionUtils.isEmpty(orderIdList)) {
            return new ListResult<>(true);
        }

        // 订单ID过滤：假设仅支持3个月内容的订单
        List<String> destOrderIdList = orderIdList.stream().filter(this::filterOrderId).collect(Collectors.toList());

        ListResult<OrderModel> orderResult;
        try {
            // 查询订单信息
            List<OrderDTO> orderList = this.orderQueryClient.queryList(destOrderIdList);

            // 订单模型转换
            List<OrderModel> modelList = orderList.stream().map(OrderModel::from).collect(Collectors.toList());

            // 返回成功结果
            orderResult = new ListResult<>(true);
            orderResult.setResultObj(modelList);
        } catch (Throwable e) {
            // 测试日志输出
            orderResult = new ListResult<>(false);
            orderResult.setCode("UNKNOWN_ERROR");
            orderResult.setMessage(e.getMessage());
        }

        return orderResult;
    }

    /**
     * yyyyMMdd日期检查：订单ID格式为日期开头，且日期为最近3个月内
     */
    private boolean filterOrderId(String orderId) {
        if (StringUtils.length(orderId) < 8) {
            return false;
        }

        // 3个月的日期
        LocalDate minOrderDate = LocalDate.now().minusMonths(3L);

        // 当前订单的日期
        LocalDate orderDate;
        try {
            String prefix = StringUtils.substring(orderId, 0, 8);
            orderDate = LocalDate.parse(prefix, DateTimeFormatter.BASIC_ISO_DATE);

            // 比较是否为3个月之内
            return !orderDate.isBefore(minOrderDate);
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
