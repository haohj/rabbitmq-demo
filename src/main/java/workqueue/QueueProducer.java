package workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 */
public class QueueProducer {
    public static void main(String[] args) throws Exception {
        //1.定义队列名称
        String queueName = "task_queue";

        //2.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHandshakeTimeout(20000);
        factory.setPort(5672);
        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");

        //3.创建连接
        Connection connection = factory.newConnection();

        //4.创建channel
        Channel channel = connection.createChannel();

        //5.为channel设置队列
        channel.queueDeclare(queueName, false, false, false, null);

        //6.发送消息
        for (int i = 0; i < 6; i++) {
            String msg = "task " + i;
            channel.basicPublish("", queueName, null, msg.getBytes());
            System.out.println("消息:" + msg + "发送完毕");
        }

        //7.关闭channel和连接
        channel.close();
        connection.close();
    }
}
