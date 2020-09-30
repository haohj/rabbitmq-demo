package workqueue.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 手动消息确认
 * 生产者
 */
public class ManualAckQueueProducer {
    public static void main(String[] args) throws Exception {
        //1.定义队列名称
        String queueName = "task_queue";
        String[] msgs = {"sleep", "task 1", "task 2", "task 3", "task 4", "task 5", "task 6"};

        //2.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setPort(5672);
        factory.setHost("127.0.0.1");
        factory.setVirtualHost("/");
        factory.setHandshakeTimeout(20000);

        //3.创建连接
        Connection connection = factory.newConnection();

        //4.创建channel
        Channel channel = connection.createChannel();

        //5.channel绑定queue
        channel.queueDeclare(queueName, false, false, false, null);

        //6.发送消息
        for (String msg : msgs) {
            channel.basicPublish("", queueName, null, msg.getBytes());
            System.out.println("消息:" + msg + "发送完毕");
        }

        //7.关闭channel和连接
        channel.close();
        connection.close();

    }
}
