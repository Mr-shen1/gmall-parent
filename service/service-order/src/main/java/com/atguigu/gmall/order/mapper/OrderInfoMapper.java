package com.atguigu.gmall.order.mapper;

import com.atguigu.gmall.model.order.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
* @author SSS
* @description 针对表【order_info(订单表 订单表)】的数据库操作Mapper
* @createDate 2021-12-24 18:10:34
* @Entity com.atguigu.gmall.order.domain.OrderInfo
*/
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    /**
     * 根据orderId获取订单总金额
     * @param orderId
     * @return
     */
    BigDecimal getOrderTotalAmount(@Param("orderId") Long orderId);

    void closeOrder(@Param("orderId") Long orderId, @Param("orderStatus") String orderStatus, @Param("processStatus") String processStatus);


    OrderInfo getOrderInfo(@Param("orderId") Long orderId);
}




