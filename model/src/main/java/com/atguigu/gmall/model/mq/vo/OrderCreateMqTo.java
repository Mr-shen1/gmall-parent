package com.atguigu.gmall.model.mq.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateMqTo {

    private Long orderId;
    private String userId;
    private String tradeNo;
}
