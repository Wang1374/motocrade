package com.laogeli.distribute.config;

import com.laogeli.common.core.config.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 生产者
 */
@Slf4j
@Component
public class ProducerMessage implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ProducerMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this::confirm); // rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setReturnCallback(this::returnedMessage);
        rabbitTemplate.setMandatory(true);
    }

    public void sendMsg(Object content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_A, RabbitConfig.ROUTINGKEY_A, content, correlationId);
    }

    /**
     * 消息发送到队列中，进行消息确认
     *
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 消息确认的id： " + correlationData);
        if (ack) {
            //发送成功 删除本地数据库存的消息
            log.info("消息送达rabbitmq服务器");
        } else {
            // 根据本地消息的状态为失败，可以用定时任务去处理数据
            log.info("消息未送达rabbitmq服务器：id " + correlationData + "失败的原因" + cause);
        }
    }

    /**
     * 消息发送失败返回监控
     *
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("returnedMessage [消息从交换机到队列失败]  message：" + message);
    }
}
