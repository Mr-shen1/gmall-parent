package com.atguigu.gmall.model.api;

import com.atguigu.gmall.model.order.OrderDetail;
import com.atguigu.gmall.model.user.UserAddress;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@Data
public class ConfirmOrderVo {

    private List<OrderDetail> detailArrayList;

    private Integer totalNum;

    private BigDecimal totalAmount;

    private List<UserAddress> userAddressList;


    private String tradeNo;


}
