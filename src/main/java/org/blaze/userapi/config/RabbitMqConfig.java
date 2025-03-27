package org.blaze.userapi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class RabbitMqConfig {

    private static final String EXCHANGE_NAME = "friendship.exchange";
    private static final String QUEUE_NAME = "sendFriendRequest";
    private static final String ROUTING_KEY = "friendship.request";

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue sendFriendRequestQueue() {
        return new Queue(QUEUE_NAME,true);
    }

    @Bean
    public DirectExchange friendshipExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }


    @Bean
    Binding bindingFriendRequest(Queue sendFriendRequestQueue, DirectExchange friendshipExchange) {
            return BindingBuilder.bind(sendFriendRequestQueue).to(friendshipExchange).with(ROUTING_KEY);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
