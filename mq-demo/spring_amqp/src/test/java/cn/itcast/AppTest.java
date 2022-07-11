package cn.itcast;

import cn.itcast.entity.Book;
import cn.itcast.entity.Wife;
import cn.itcast.listener.SpringRabbitListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class AppTest 
{

    private final String queueName = "simple.queue";

    @Autowired
    private RabbitTemplate amqpTemplate;


    /**
     *  生产者：发送消息到消息队列中的消息
     */
    @Test
    public void MQProducerTest()
    {

        String msg = "Hello SpringAMQP!";

        amqpTemplate.convertAndSend(queueName,msg);

    }

    /**
     *  消费者：获取消息队列中的消息
     */
    @Test
    public void MQConsumer() {
        System.out.println(amqpTemplate.receiveAndConvert(queueName));
    }


    /**
     * WorkQueue模型
     */
    @Test
    public void WorkMQSenderr() throws InterruptedException {
        for (int i = 0; i < 50 ; i++) {
            amqpTemplate.convertAndSend(queueName,"Flandre Scarlet");
            Thread.sleep(20);
        }
    }


    /**
     * FanoutExchangeQueue模型
     * 发送消息到交换机上，然后让交换机路由消息到不同的队列
     */
    @Test
    public void FanoutQueueSender() throws InterruptedException {
        //交换机名称
        String exchangeName = "itcast.fanout";
        //发送的消息
        String msg = "Hello Flandre Scarlet";
        //发送消息到交换机中
        amqpTemplate.convertAndSend(exchangeName,"",msg);
    }


    /**
     * DirectExchangeQueue模型
     * 发送消息到交换机上，然后让交换机根据key路由消息到不同的队列
     */
    @Test
    public void DirectQueueSender() throws InterruptedException {
        //交换机名称
        String exchangeName = "direct.exchange";
        //发送的消息
        String msg = "Hello Remilia Scarlet";
        //发送消息到交换机中
        amqpTemplate.convertAndSend(exchangeName,"blue",msg);
    }



    @Test
    public void TopicQueueSender1() throws InterruptedException {
        //交换机名称
        String exchangeName = "topic.exchange";
        //发送的消息
        String msg = "日本新闻报道";
        //发送消息到交换机中
        amqpTemplate.convertAndSend(exchangeName,"japan.news",msg);
    }

    @Test
    public void TopicQueueSender2() throws InterruptedException {
        //交换机名称
        String exchangeName = "topic.exchange";
        //发送的消息
        String msg = "中国新闻报道";
        //发送消息到交换机中
        amqpTemplate.convertAndSend(exchangeName,"china.news",msg);
    }


    /**
     * 发送对象到消息队列中
     */
    @Test
    public void ObjectQueueSender1() throws InterruptedException {
        //交换机名称
        String queueName = "object.queue";
        //发送的消息
        Book msg = new Book();
        msg.setId(1);
        msg.setName("杜林颢成名传");
        msg.setPrice(18.0);
        //发送消息到交换机中
        amqpTemplate.convertAndSend(queueName,msg);
    }


    /**
     * 发送对象到消息队列中
     */
    @Test
    public void ObjectQueueSender2() throws InterruptedException {
        //队列名称
        String queueName = "wife.queue";
        //赠予的老婆
        Wife w = new Wife();
        w.setName("Flandre Scarlet");
        //发送老婆到队列中
        amqpTemplate.convertAndSend(queueName,w);
    }
}
