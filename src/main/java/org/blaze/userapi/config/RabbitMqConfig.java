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
import jakarta.annotation.PostConstruct;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class RabbitMqConfig {
    private static final Logger log = LoggerFactory.getLogger(RabbitMqConfig.class);
    private static final String EXCHANGE_NAME = "friendship.direct";
    private static final String QUEUE_NAME = "sendFriendRequestQueue";
    private static final String ROUTING_KEY = "friendship.request";

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    // Connection Test
    @EventListener(ContextRefreshedEvent.class)
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // This will run after all beans are initialized
        RabbitTemplate rabbitTemplate = event.getApplicationContext().getBean(RabbitTemplate.class);
        try {
            log.info("Testing RabbitMQ connection...");
            
            // Create a simple test message that won't cause deserialization issues
            Map<String, Object> testMessage = new HashMap<>();
            testMessage.put("type", "test");
            testMessage.put("content", "RabbitMQ connection test");
            testMessage.put("id", UUID.randomUUID().toString());
            
            rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, testMessage);
            log.info("Successfully sent test message to RabbitMQ with ID: {}", testMessage.get("id"));
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
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public Binding binding(Queue friendRequestQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(friendRequestQueue)
                .to(directExchange)
                .with(ROUTING_KEY);
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
