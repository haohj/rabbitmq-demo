package workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 消费者
 */
public class QueueConsumer {
    public static void main(String[] args) throws Exception {
        //1.定义队列名称
        String queueName = "task_queue";
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

        //5.channel绑定queue
        channel.queueDeclare(queueName, false, false, false, null);

        //6.创建消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String msg = new String(body);
                doWork(msg);
            }
        };

        //7.消费消息
        channel.basicConsume(queueName, true, consumer);
    }

    private static void doWork(String msg) {
        try {
            //通过sleep来模拟需要耗时的操作
            if ("sleep".equals(msg)) {
                Thread.sleep(1000 * 60);
            } else {
                Thread.sleep(1000);
            }
            System.out.println("消息：" + msg + "处理完毕！");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
