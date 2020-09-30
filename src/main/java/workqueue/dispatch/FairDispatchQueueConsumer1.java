package workqueue.dispatch;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 公平分发
 * 消息消费者
 */
public class FairDispatchQueueConsumer1 {
    public static void main(String[] args) throws Exception {
        //1.定义队列名称
        String queueName = "task_queue";

        //2.创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHandshakeTimeout(20000);
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);

        //3.创建连接
        Connection connection = factory.newConnection();

        //4.创建channel
        final Channel channel = connection.createChannel();

        //5.channel绑定queue
        channel.queueDeclare(queueName, false, false, false, null);

        //6.创建consumer
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                doWork(msg);
                channel.basicAck(envelope.getDeliveryTag(), true);
            }
        };

        // 告诉RabbitMQ 我每次值处理一条消息，你要等我处理完了再分给我下一个
        channel.basicQos(1);

        //7.消费消息
        channel.basicConsume(queueName, false, consumer);
    }

    private static void doWork(String msg) {
        try {
            System.out.println("消费到消息：" + msg);
            if ("sleep".equals(msg)) {
                Thread.sleep(1000 * 60);
            } else {
                Thread.sleep(1000);
            }
            System.out.println("消息：" + msg + " 已处理");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
