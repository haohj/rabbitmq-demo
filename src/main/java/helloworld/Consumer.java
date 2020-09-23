package helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

/**
 * 消费者
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        //1.创建一个连接工厂，并配置连接信息
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setHandshakeTimeout(20000);
        //2.通过连接工厂创建一个连接
        Connection connection = factory.newConnection();
        //3.通过连接创建一个channel
        Channel channel = connection.createChannel();
        //4.声明创建一个queue队列
        String queueName = "test";
        //5.创建一个队列，参数：{队列名称，是否持久化，是否独占，是否自动删除，参数}
        channel.queueDeclare(queueName, true, false, false, null);

        //6.创建一个消费者
        QueueingConsumer consumer = new QueueingConsumer(channel);

        //设置channel
        channel.basicConsume(queueName, true, consumer);
        //消费消息
        while (true) {
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            System.out.println("消费到消息：" + msg);
        }
    }
}
