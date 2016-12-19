package com.absurd.rabbitmq.springamqp.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;

/**
 * @author <a href="mailto:www_1350@163.com">王文伟</a>
 * @Title: mq-demo-parent
 * @Package com.absurd.rabbitmq.springamqp.config
 * @Description:
 * @date 2016/12/19 14:51
 */
@Configuration
public class RabbitmqConfig {
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }

    /****
     *  处理路由键
     *  需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配
     *  这是一个完整的匹配
     *  如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，不会转发dog.puppy，也不会转发dog.guard，只会转发dog
     * @return
     */
    @Bean
    public DirectExchange defaultExchange() {
        return new DirectExchange("absurd_direct_exchange");
    }

    /***
     * 不处理路由键
     * 你只需要简单的将队列绑定到交换机上
     * 一个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上
     * 很像子网广播，每台子网内的主机都获得了一份复制的消息
     * Fanout交换机转发消息是最快的
     * @return
     */
    @Bean
    public FanoutExchange defaultFanoutExchange() {
        return new FanoutExchange("absurd_exchange");
    }

    /***
     * 将路由键和某模式进行匹配
     * 此时队列需要绑定要一个模式上
     * 符号"#"匹配一个或多个词，符号""匹配不多不少一个词。因此"audit.#"能够匹配到"audit.irs.corporate"，但是"audit." 只会匹配到"audit.irs"
     * @return
     */
    @Bean
    public TopicExchange defaultTopicExchange() {
        return new TopicExchange("absurd_topic_exchange");
    }

    /***
     *Headers Exchange不同于上面三种Exchange
     * 根据Message的一些头部信息来分发过滤Message，忽略routing key的属性
     * 如果Header信息和message消息的头信息相匹配，那么这条消息就匹配上了
     * @return
     */
    @Bean
    public HeadersExchange defaultHeadersExchange() {
        return new HeadersExchange("absurd_header_exchange");
    }

//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(defaultTopicExchange()).with("abc");
//    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with("abc");
    }

//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue()).to(defaultFanoutExchange());
//    }

    @Bean
    public Queue queue() {
        return new Queue("userQueueDir");
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }
}
