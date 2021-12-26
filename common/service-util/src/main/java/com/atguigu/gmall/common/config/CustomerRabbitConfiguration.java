package com.atguigu.gmall.common.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitTemplateConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@EnableRabbit
@Configuration
public class CustomerRabbitConfiguration {


    /**
     * 创建一个将json转换器
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(RabbitTemplateConfigurer configurer, ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate();
        configurer.configure(template, connectionFactory); // 自己配的 configurer 会生效


        /**
         * CorrelationData correlationData, boolean ack, @Nullable String cause
         */
        template.setConfirmCallback((correlationData, ack, cause) -> {

            // 可以回复ack, 关联修改本地表
            //correlationData.getId()

            System.out.println("回调::::[ConfirmCallback]: + correlationData=" + correlationData + "ack=" + ack + "cause=" + cause);

        });

        /**
         * Message message,
         * int     replyCode,
         * String  replyText,
         * String  exchange,
         * String  routingKey
         */
        // TODO 有文章说使用 ReturnCallback 必须开启这个??
        //template.setMandatory(true);

        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            System.out.println("回调：：：：【ReturnCallback】：message="+message+"，replyCode="+replyCode+"，replyText"+replyText+"，exchange="+exchange+"，routingKey"+routingKey);
        });

        return template;
    }
}
