package workqueue.ack;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者
 */
public class AutoAckQueueConsumer {
    public static void main(String[] args) throws Exception {
        //1.定义队列名称
        String queueName = "task_queue";

        //2.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setVirtualHost("/");
        factory.setHandshakeTimeout(20000);
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        //3.创建连接
        Connection connection = factory.newConnection();

        //4.创建channel
        Channel channel = connection.createChannel();

        //5.channel绑定queue
        /**
         * queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments)
         * queue 队列名称，即要从哪个队列接收消息
         * durable 队列是否需要持久化
         * exclusive 队列是否为该通道独占的（其他队列是否可消费此队列的消息）
         * autoDelete 该队列不再使用的时候，是否让RabbitMQ服务器自动删除该队列
         * arguments 队列的其他属性参数
         * */
        channel.queueDeclare(queueName, false, false, false, null);

        //6.创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                doWork(msg);
            }
        };

        //7.消费消息
        /**
         * String basicConsume(String queue, boolean autoAck, Consumer callback)
         * queue 队列名称，即要从那个队列中接收消息
         * autoAck 是否自动确认消息，默认是true
         * callback 消费者，即谁接收消息
         * */
        channel.basicConsume(queueName, true, consumer);
    }

    /**
     * 处理消息
     *
     * @param msg 消息内容
     */
    private static void doWork(String msg) {
        try {
            System.out.println("消费到消息: " + msg);
            // 通过sleep()来模拟需要耗时的操作
            if ("sleep".equals(msg)) {
                Thread.sleep(2000 * 60);
            } else {
                Thread.sleep(1000);
            }
            System.out.println("消息：" + msg + "已处理完！");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}