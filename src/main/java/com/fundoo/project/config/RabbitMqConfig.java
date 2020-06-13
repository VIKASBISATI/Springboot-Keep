package com.fundoo.project.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    @Value(value = "${spring.rabbitmq.template.default-receive-queue}")
    private String MyQueue;
    @Value(value = "${spring.rabbitmq.template.routing-key}")
    private String myRoutingKey;
    @Value(value = "${spring.rabbitmq.template.exchange}")
    private String MyQueueExchange;
    @Bean
    Queue queue(){return new Queue(MyQueue, true);}
    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(MyQueueExchange);
    }
    @Bean
    Binding queueBinding(Queue queue, DirectExchange directExchange){
      return BindingBuilder.bind(queue).to(directExchange).with(myRoutingKey);
    }
}
