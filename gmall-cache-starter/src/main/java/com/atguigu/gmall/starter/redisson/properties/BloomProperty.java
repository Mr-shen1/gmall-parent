package com.atguigu.gmall.starter.redisson.properties;

import lombok.Data;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/14
 */
@Data
public class BloomProperty {
    private String bloomName;
    private Long expectedInsertions;
    private Double falseProbability;
    private String skuRebildCron;

}
