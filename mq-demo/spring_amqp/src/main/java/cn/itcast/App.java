package cn.itcast;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * MQ：消息队列，主要用于异步通讯，如短信推送服务，QQ聊天
 */
@SpringBootApplication
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class,args);
    }

    /**
     * 声明SpringAMQP消息队列的类型转换器
     * 让对象通过JSON格式来传输
     * 注意：消息提供者和消息消费者两者都必须使用相同的消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
