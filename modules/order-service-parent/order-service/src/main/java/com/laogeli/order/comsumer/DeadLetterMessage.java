package com.laogeli.order.comsumer;

import com.laogeli.common.core.config.RabbitConfig;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 死信消费
 */
@Slf4j
@Component
public class DeadLetterMessage {

    @RabbitListener(queues = RabbitConfig.DEAD_LETTER_QUEUE)
    public void dlxMessage(Message message, Channel channel) throws IOException {
        System.err.println("收到死信消息：" + new String(message.getBody()));
        // 将消息存入数据库，并推送消息给接单的人员，或者发送邮件给管理员
        // 接单人员 收到消息 转到异常订单列表，进行操作
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
