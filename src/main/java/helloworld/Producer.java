package helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 */
public class Producer {
    public static void main(String[] args) throws Exception {
        //1.创建一个连接工厂，并设置连接信息
        ConnectionFactory factory = new ConnectionFactory();
        //设置主机地址
        factory.setHost("127.0.0.1");
        //设置端口
        factory.setPort(5672);
        //设置虚拟主机
        factory.setVirtualHost("/");
        //设置握手超时
        factory.setHandshakeTimeout(20000);

        //2.通过连接工厂创建一个连接
        Connection conn = factory.newConnection();
        //3.通过连接创建一个channel
        Channel channel = conn.createChannel();

        //4.发送对应消息
        for (int i = 0; i < 5; i++) {
            System.out.println("发送消息：" + i);
            String msg = "hello RabbitMQ " + i;
            /**
             * void basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body) throws IOException;
             * exchange 指定交换机 不指定默认(AMQP default交换机)
             * routingKey 指定路由，根据routingKey进行匹配
             * props 消息属性
             * body 消息体
             * */
            channel.basicPublish("", "test", null, msg.getBytes());
        }

        //5.关闭连接
        channel.close();
        conn.close();
    }
}
