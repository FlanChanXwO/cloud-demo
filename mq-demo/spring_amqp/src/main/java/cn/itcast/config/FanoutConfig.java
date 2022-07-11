package cn.itcast.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息队列 - 发布订阅模型：
 * FanoutExchange Fanout交换机配置方式
 * 让多个队列绑定到交换机中，让交换机路由消息到多个队列中，使其让多个消费者可以从消息队列中获取到消息
 */
@Configuration
public class FanoutConfig {

    /**
     * 初始化Fanout交换机
     * @return Fanout交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("itcast.fanout");
    }


    /**
     * @return Fanout队列1
     */
    @Bean
    public Queue fanoutQueue1 () {
        return new Queue("fanout.queue1");
    }


    /**
     * @return Fanout队列2
     */
    @Bean
    public Queue fanoutQueue2 () {
        return new Queue("fanout.queue2");
    }


    /**
     * 绑定队列1到交换机中
     * @param fanoutQueue1 注入队列1的bean，让队列1与交换机绑定
     * @param fanoutExchange fanout交换机，用于让队列与交换机绑定
     */
    @Bean
    public Binding fanoutBinding1(Queue fanoutQueue1 , FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue1).to(fanoutExchange);
    }


    /**
     * 绑定队列2到交换机中
     * @param fanoutQueue2 注入队列2的bean，让队列2与交换机绑定
     * @param fanoutExchange fanout交换机，用于让队列与交换机绑定
     */
    @Bean
    public Binding fanoutBinding2(Queue fanoutQueue2 , FanoutExchange fanoutExchange) {
        return BindingBuilder.bind(fanoutQueue2).to(fanoutExchange);
    }
}
