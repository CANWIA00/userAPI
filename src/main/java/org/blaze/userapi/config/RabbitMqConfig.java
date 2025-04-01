package org.blaze.userapi.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitMqConfig {

     String EXCHANGE_NAME = "friendship.exchange";
     String QUEUE_NAME = "sendFriendRequestQueue";
     String ROUTING_KEY = "friendship.request";

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange friendshipExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue sendFriendRequestQueue() {
        return new Queue(QUEUE_NAME,true);
    }

    @Bean
    Binding bindingFriendRequest(Queue sendFriendRequestQueue, DirectExchange directExchange) {
            return BindingBuilder.bind(sendFriendRequestQueue).to(directExchange).with(ROUTING_KEY);
    }


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder){
        return builder.build();
    }




}
