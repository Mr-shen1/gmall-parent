package com.atguigu.gmall.order.listener.mq;

import com.atguigu.gmall.common.constant.RabbitMqConst;
import com.atguigu.gmall.model.enums.ProcessStatus;
import com.atguigu.gmall.model.mq.vo.OrderCreateMqTo;
import com.atguigu.gmall.order.service.OrderInfoService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * desc:
 *
 * @author: skf
 * @date: 2021/12/25
 */
@Service
@Slf4j
public class OrderCloseListener {

    @Autowired
    private OrderInfoService orderInfoService;

    /**
     * 方法参数签名
     * 自定义JavaBean类型  可选：  自动把消息内容转为指定的Bean
     * Message message  标配,
     * Channel channel  标配,
     *
     *
     * @param message
     * @param channel
     * @param orderCreateMqTo
     */
    @RabbitListener(queues = RabbitMqConst.ORDER_TIMEOUT_QUEUE)
    public void closeOrder(Channel channel, Message message, OrderCreateMqTo orderCreateMqTo)  {

        log.warn("closeOrder() called with parameters => 【channel = {}】, 【message = {}】, 【orderCreateMqTo = {}】",channel, message, orderCreateMqTo);

        // 获取队列消息的id(标签)

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            //消息有可能会被重复派发？
            //1、消费者消费成功了，但是没有及时回复服务器，就给断了。MQ把消息重复
            //2、发送者消息发成功了，服务器也收到了。但是网络传输断线了。数据库消息表以为服务器没收到，再发一遍
            //业务层处理【幂等性】
            // 这不是一个原子操作
            //OrderInfo orderInfo = orderInfoService.getById(orderId);
            //if(orderInfo.getOrderStatus().equals(OrderStatus.UNPAID.name())){
            //    //未支付才关单
            //
            //}
            //int 车= 10 / 0;
            //System.out.println("收到");

            //获取orderId
            Long orderId = orderCreateMqTo.getOrderId();
            // 关闭订单
            ProcessStatus closed = ProcessStatus.CLOSED;
            orderInfoService.closeOrder(orderId, closed.name(), closed.getOrderStatus().name());

            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("closeOrder() called with exception => 【channel = {}】, 【message = {}】", channel, message, e);
            // requeue：被拒绝的是否重新入队列
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        // multiple: 是否批量.true:将一次性ack所有小于deliveryTag的消息。

    }
}
