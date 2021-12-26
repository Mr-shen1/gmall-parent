package com.atguigu.gmall.payment.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.atguigu.gmall.common.config.FeignCommonConfig;
import com.atguigu.gmall.payment.config.properties.AlipayProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@Configuration
@Import(FeignCommonConfig.class)
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.client.order"
})
public class PaymentConfiguration {

    @Autowired
    private AlipayProperties alipayProperties;

    @Bean
    public AlipayClient defaultAlipayClient() {

        return new DefaultAlipayClient(alipayProperties.getGatewayUrl(),
                alipayProperties.getApp_id(),
                alipayProperties.getMerchant_private_key(),
                "json",
                alipayProperties.getCharset(),
                alipayProperties.getAlipay_public_key(),
                alipayProperties.getSign_type()
                );
    }
}
