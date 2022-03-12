package com.laogeli.order.comsumer;

import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.laogeli.common.core.config.RabbitConfig;
import com.laogeli.common.core.constant.CommonConstant;
import com.laogeli.common.core.utils.StringUtils;
import com.laogeli.common.core.utils.SysUtil;
import com.laogeli.order.api.module.Doors;
import com.laogeli.order.mapper.ClienteleMapper;
import com.laogeli.order.service.BusinessOrderService;
import com.laogeli.order.service.ClienteleService;
import com.laogeli.order.service.DoorsService;
import com.rabbitmq.client.Channel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 消费者
 */
@Slf4j
@AllArgsConstructor
@Component
public class ComsumerMessage {

    private final RedisTemplate redisTemplate;

    private final BusinessOrderService businessOrderService;

    private final ClienteleMapper clienteleMapper;

    private final DoorsService doorsService;

    @RabbitListener(queues = RabbitConfig.QUEUE_A)
    public void handleMessage(Message message, Channel channel) throws IOException {
        try {
            String json = new String(message.getBody());
            JSONObject jsonObject = JSON.parseObject(json);
            log.info("消费消息：" + json);

            /**
             * 防止重复消费，可以根据传过来的唯一ID先判断缓存数据中是否有数据
             * 1、有数据则不消费，直接应答处理
             * 2、缓存没有数据，则进行消费处理数据，处理完后手动应答
             * 3、如果消息 处理异常则，可以存入数据库中，手动处理（可以增加短信和邮件提醒功能）
             */
            if (redisTemplate.hasKey(CommonConstant.MQ_MSG_ID_KEY + jsonObject.get("id"))) {
                // 手动应答
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            //int i = 1 / 0;
            //业务处理。
            // 根据消息类型 调用对应的service
            if ("order".equals(jsonObject.getString("msgType"))) {
                businessOrderService.insertOrderReceiving(jsonObject);

            } else if ("insertDoor".equals(jsonObject.getString("msgType"))) {
                //根据客户名和平台公司id  792071937215041536   查询客户id
                String customerName = jsonObject.getString("creator");
                String id = clienteleMapper.findIdByCompanyIdAndName("792071937215041536", customerName);
                //根据客户id插入门点数据   同步平台          792071937215041536
                if (!StringUtils.isEmpty(id)) {
                    doorsService.insertCustomerDoor(jsonObject, id);
                }
            } else if ("updateDoor".equals(jsonObject.getString("msgType"))) {
                doorsService.updateCustomerDoor(jsonObject);
            }else if("clientOrder".equals(jsonObject.getString("msgType"))){
                businessOrderService.insertOrderReceiving(jsonObject);
            }
            // 手动应答
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            redisTemplate.opsForValue().set(CommonConstant.MQ_MSG_ID_KEY + jsonObject.get("id"), json);
            // 设置一分钟过期
            redisTemplate.expire(CommonConstant.MQ_MSG_ID_KEY + jsonObject.get("id"), 1, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("消费消息失败了【】error：" + message.getBody());
            log.error("OrderConsumer  handleMessage {} , error:", message, e);
            // 处理消息失败，将消息重新放回队列。尝试重发10s 后放入死信队列
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
