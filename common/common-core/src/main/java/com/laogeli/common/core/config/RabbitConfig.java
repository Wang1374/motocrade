package com.laogeli.common.core.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class RabbitConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    /**
     * 消息交换机,它指定消息按什么规则,路由到哪个队列。
     */
    public static final String EXCHANGE_A = "my_mq_exchange_A";

    /**
     * 死信交换机。
     */
    public static final String DEAD_LETTER_EXCHANGE = "dead_letter_exchange";

    /**
     * 消息的载体,每个消息都会被投到一个或多个队列。
     */
    public static final String QUEUE_A = "QUEUE_A";

    /**
     * 死信队列。
     */
    public static final String DEAD_LETTER_QUEUE = "DEAD_LETTER_QUEUE";

    /**
     * 路由关键字,exchange根据这个关键字进行消息投递。
     */
    public static final String ROUTINGKEY_A = "spring-boot-routingKey_A";

    /**
     * 配置连接工厂
     */
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host, port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setPublisherConfirms(true); // 消息确认设置发送消息失败重试
        connectionFactory.setChannelCacheSize(50);// channel的缓存数量，默认值为25。解决多线程发送消息
        return connectionFactory;
    }

    /**
     * 配置消费者监听的容器,配置使用json转递数据
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动应答
        return factory;
    }

    /**
     * rabbitmq的模板配置
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)//必须是prototype原型类型
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        // 必须为true,否则无法触发returnedMessage回调，消息丢失
        template.setMandatory(true); // 设置发送消息失败重试
        return template;
    }

    /**
     * 声明直连交换机 支持持久化.
     */
    @Bean
    public DirectExchange defaultExchange() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new DirectExchange(EXCHANGE_A, true, false);
    }

    /**
     * 声明死信交换机 支持持久化.
     */
    @Bean
    public TopicExchange deadLetterExchange() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        return new TopicExchange(DEAD_LETTER_EXCHANGE, true, false);
    }

    /**
     * 声明队列A 支持持久化.
     */
    @Bean
    public Queue queueA() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        Map<String, Object> map = new HashMap<>();
        //设置队列的ttl, 时间单位为毫秒
        map.put("x-message-ttl", 10000);
        //指定死信交换机
        map.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        return new Queue(QUEUE_A, true, false, false, map);
    }

    /**
     * 声明死信队列 支持持久化.
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE, true);
    }

    /**
     * 通过绑定键 将指定队列A绑定到一个指定的交换机 .
     * 一个交换机可以绑定多个消息队列，也就是消息通过一个交换机,可以分发到不同的队列当中去。
     *
     * @return
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queueA()).to(defaultExchange()).with(RabbitConfig.ROUTINGKEY_A);
    }

    /**
     * 绑定死信队列到死信交换机
     *
     * @return
     */
    @Bean
    public Binding bindingDead() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with("#");// 无条件路由
    }
}
