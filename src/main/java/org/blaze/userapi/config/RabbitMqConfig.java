package org.blaze.userapi.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class RabbitMqConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);

    public static final String EXCHANGE_NAME = "friendship.direct";
    public static final String FRIEND_REQUEST_QUEUE = "sendFriendRequestQueue";
    public static final String FRIEND_REQUEST_ROUTING_KEY = "friendship.request";

    // Test queue
    public static final String TEST_QUEUE = "testQueue";
    public static final String TEST_ROUTING_KEY = "friendship.test";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            log.info("Testing RabbitMQ connection...");

            // Use a safe, isolated test queue
            Map<String, Object> testMessage = new HashMap<>();
            testMessage.put("type", "test");
            testMessage.put("content", "RabbitMQ connection test");
            testMessage.put("id", UUID.randomUUID().toString());

            rabbitTemplate.convertAndSend(EXCHANGE_NAME, TEST_ROUTING_KEY, testMessage);
            log.info("Successfully sent test message to RabbitMQ testQueue");
        } catch (Exception e) {
            log.error("Failed to connect to RabbitMQ: {}", e.getMessage(), e);
        }
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue friendRequestQueue() {
        return new Queue(FRIEND_REQUEST_QUEUE, false); // durable queue
    }

    @Bean
    public Queue testQueue() {
        return new Queue(TEST_QUEUE, false);
    }

    @Bean
    public Binding friendRequestBinding() {
        return BindingBuilder.bind(friendRequestQueue())
                .to(directExchange())
                .with(FRIEND_REQUEST_ROUTING_KEY);
    }

    @Bean
    public Binding testQueueBinding() {
        return BindingBuilder.bind(testQueue())
                .to(directExchange())
                .with(TEST_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
