package com.atguigu.gmall.order.config;

import com.atguigu.gmall.common.config.CustomerRabbitConfiguration;
import com.atguigu.gmall.common.config.FeignCommonConfig;
import com.atguigu.gmall.common.config.MybatisPlusConfig;
import com.atguigu.gmall.common.constant.RabbitMqConst;
import com.atguigu.gmall.order.config.properties.OrderProperties;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.HashMap;
import java.util.Map;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/24
 */
@Configuration
@Import({
        MybatisPlusConfig.class,
        FeignCommonConfig.class,
        CustomerRabbitConfiguration.class
})
@EnableFeignClients(basePackages = {
        "com.atguigu.gmall.client.cart",
        "com.atguigu.gmall.client.product",
        "com.atguigu.gmall.client.user",
        "com.atguigu.gmall.client.ware"

})
public class OrderConfig {


    @Autowired
    private OrderProperties orderProperties;


    /**
     * 声明订单使用的交换机
     * @return
     */
    @Bean
    public Exchange orderExchange() {

        /**
         * String name, 交换机的名字
         * boolean durable, 是否持久化
         * boolean autoDelete, 自动删除
         * Map<String, Object> arguments 设置项
         */
        return new TopicExchange(RabbitMqConst.ORDER_EXCHANGE_NAME, true, false, null);
    }


    /**
     * 创建延迟队列
     * @return
     */
    @Bean
    public Queue orderDelayQueue() {

        /**
         * String name, 队列的名字
         * boolean durable,
         * boolean exclusive, 排他
         * boolean autoDelete,
         * @Nullable Map<String, Object> arguments
         */
        Map<String, Object> map = new HashMap<>();
        /**
         * x-dead-letter-exchange: order-event-exchange
         * x-dead-letter-routing-key: order.timeout.rk
         * x-message-ttl: 60000  测试用: 一分钟意思下
         */
        map.put("x-dead-letter-exchange", RabbitMqConst.ORDER_EXCHANGE_NAME);
        map.put("x-dead-letter-routing-key", RabbitMqConst.ORDER_TIMEOUT_ROUNTING_KEY);
        map.put("x-message-ttl", orderProperties.getTimeout() * 1000);

        return new Queue(RabbitMqConst.ORDER_DELAY_QUEUE,
                true,
                false,
                false,
                map);
    }

    /**
     * 创建死信队列(过期的需要消费的)
     * @return
     */
    @Bean
    public Queue orderTimeoutQueue() {

        return new Queue(RabbitMqConst.ORDER_TIMEOUT_QUEUE, true, false, false);

    }

    /**
     * 将交换机和延迟队列绑定起来
     *
     * String destination, 目的地
     * DestinationType destinationType, 目的地类型
     * String exchange, 交换机
     * String routingKey, 路由键
     * Map<String, Object> arguments 参数
     * @return
     */
    @Bean
    public Binding orderDelayBinding() {

        return new Binding(RabbitMqConst.ORDER_DELAY_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitMqConst.ORDER_EXCHANGE_NAME,
                RabbitMqConst.ORDER_DELAY_ROUNTING_KEY,
                null);
    }


    @Bean
    public Binding orderTimeoutBinding() {

        return new Binding(RabbitMqConst.ORDER_TIMEOUT_QUEUE,
                Binding.DestinationType.QUEUE,
                RabbitMqConst.ORDER_EXCHANGE_NAME,
                RabbitMqConst.ORDER_TIMEOUT_ROUNTING_KEY, null);

    }


}
