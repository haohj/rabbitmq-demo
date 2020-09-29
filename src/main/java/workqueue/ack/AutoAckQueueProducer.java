package workqueue.ack;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 */
public class AutoAckQueueProducer {
    public static void main(String[] args) throws Exception {
        //1.定义队列名称
        String queueName = "task_queue";
        String[] msgs = {"sleep", "task 1", "task 2", "task 3", "task 4", "task 5", "task 6"};

        //2.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");
        factory.setHandshakeTimeout(20000);

        //3.创建连接
        Connection connection = factory.newConnection();

        //4.创建channel
        Channel channel = connection.createChannel();

        //5.为channel设置队列
        /**
         * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         * queue 队列名称
         * durable 该队列是否需要持久化
         * exclusive 该通道是否独占该队列
         * autoDelete 该队列不再使用时，是否让RabbitMQ服务器自动删除该队列
         * arguments 其他参数
         * */
        channel.queueDeclare(queueName, false, false, false, null);

        //6.发送消息
        for (String msg : msgs) {
            /**
             * basicPublish(String exchange, String routingKey, BasicProperties props, byte[] body)
             * exchange 指定交换机，不指定默认(AMQP default交换机)
             * routingKey 指定路由，根据routingKey进行匹配
             * props
             * body
             * */
            channel.basicPublish("", queueName, null, msg.getBytes());
            System.out.println("消息" + msg + "发送完毕！");
        }

        //7.关闭连接
        channel.close();
        connection.close();

    }
}
