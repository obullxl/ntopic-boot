/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2023 All Rights Reserved.
 */
package cn.ntopic.example.mockito;

import cn.ntopic.ListResult;
import cn.ntopic.NTAbstractTest;
import cn.ntopic.order.OrderQueryClient;
import cn.ntopic.order.OrderQueryService;
import cn.ntopic.order.dto.OrderDTO;
import cn.ntopic.order.enums.OrderStatusEnum;
import cn.ntopic.order.model.OrderModel;
import com.google.common.collect.Lists;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * OrderService-单元测试
 *
 * @author obullxl 2023年05月20日: 新增
 */
public class OrderQueryServiceTest extends NTAbstractTest {

    /**
     * 测试对象
     */
    @Autowired
    @Qualifier("orderQueryService")
    private OrderQueryService orderQueryService;

    /**
     * Mock对象
     */
    @MockBean
    @Autowired
    @Qualifier("orderQueryClient")
    private OrderQueryClient orderQueryClient;

    /**
     * Case01-正常返回：测试过滤3个月内订单逻辑
     */
    @Test
    public void test_queryList_01() {
        final String orderId1 = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "0001";
        final String orderId2 = LocalDate.now().minusMonths(3L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0002";
        final String orderId3 = LocalDate.now().minusMonths(3L).minusDays(1L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0003";

        // 1. Mock客户端返回结果
        // 正常不过滤的订单
        OrderDTO orderDTO1 = new OrderDTO();
        orderDTO1.setId(orderId1);
        orderDTO1.setAmount("3.33");
        orderDTO1.setStatus(OrderStatusEnum.WAIT_PAY.getCode());

        // 刚好3个月的临界值，不过滤的订单
        OrderDTO orderDTO2 = new OrderDTO();
        orderDTO2.setId(orderId2);
        orderDTO2.setAmount("4.44");
        orderDTO2.setStatus(OrderStatusEnum.FINISHED.getCode());

        Mockito.when(this.orderQueryClient.queryList(Mockito.anyList())).thenReturn(Lists.newArrayList(orderDTO1, orderDTO2));

        // 2. 执行查询逻辑
        ListResult<OrderModel> orderResult = this.orderQueryService.findList(Lists.newArrayList(orderId1, orderId2, orderId3));

        // 3. 检查执行结果
        Assert.assertTrue(orderResult.isSuccess());

        List<OrderModel> modeList = orderResult.getResultObj();
        Assert.assertEquals(2, modeList.size());

        ArgumentCaptor<List<String>> captorOrderIdList = ArgumentCaptor.forClass(List.class);
        Mockito.verify(this.orderQueryClient, Mockito.times(1)).queryList(captorOrderIdList.capture());
        List<String> queryOrderIdList = captorOrderIdList.getValue();
        Assert.assertEquals(2, queryOrderIdList.size());
        Assert.assertTrue(queryOrderIdList.contains(orderId1));
        Assert.assertTrue(queryOrderIdList.contains(orderId2));
    }

    /**
     * Case02-客户端异常：测试订单客户端异常逻辑
     */
    @Test
    public void test_queryList_02() {
        final String orderId1 = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "0001";
        final String orderId2 = LocalDate.now().minusMonths(3L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0002";
        final String orderId3 = LocalDate.now().minusMonths(3L).minusDays(1L).format(DateTimeFormatter.BASIC_ISO_DATE) + "0003";
        final String errorMessage = "Mock异常";

        // 1. Mock客户端抛出异常
        Mockito.when(this.orderQueryClient.queryList(Mockito.anyList())).thenThrow(new RuntimeException(errorMessage));

        // 2. 执行查询逻辑
        ListResult<OrderModel> orderResult = this.orderQueryService.findList(Lists.newArrayList(orderId1, orderId2, orderId3));

        // 3. 检查执行结果
        Assert.assertFalse(orderResult.isSuccess());
        Assert.assertEquals("UNKNOWN_ERROR", orderResult.getCode());
        Assert.assertEquals(errorMessage, orderResult.getMessage());

        ArgumentCaptor<List<String>> captorOrderIdList = ArgumentCaptor.forClass(List.class);
        Mockito.verify(this.orderQueryClient, Mockito.atLeast(1)).queryList(captorOrderIdList.capture());
        List<String> queryOrderIdList = captorOrderIdList.getValue();
        Assert.assertEquals(2, queryOrderIdList.size());
        Assert.assertTrue(queryOrderIdList.contains(orderId1));
        Assert.assertTrue(queryOrderIdList.contains(orderId2));
    }
}
