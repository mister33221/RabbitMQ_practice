package com.example.rabbitMQPractice.basicModel.message;

import com.example.rabbitMQPractice.connectUtil.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer1 {

    public final static String Queue_Name = "Queue-1";

    public static void main(String[] args) throws IOException, TimeoutException {

        // create a connection between the consumer and the rabbitMQ server
        Connection factory = ConnectionUtil.getConnection();
        // create a chennel
        Channel channel = factory.createChannel();
        // declare a queue, if the queue does not exist, create it
        channel.queueDeclare(Queue_Name, true/*是否持久化*/, false/*是否獨享*/, true/*是否自動刪除*/, null/*其他參數*/);
        // limit the number of messages that the consumer can receive at a time
        // basicQos 可以設定一次接收多少個消息，如果設定為1，則表示一次只能接收一個消息，接收到消息後，必須回復ack，才能接收下一個消息，
        // 藉此讓消息可以平均分配給多個消費者，並確保善用效能。
        channel.basicQos(1);
        // create a consumer
        Consumer consumer = new DefaultConsumer(channel) {
            // when a message is received, the following method will be called
            // Envelope: the message envelope, which contains the message ID, routing key, exchange name, and message delivery mode
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                try {
                    // simulate the time-consuming process
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(envelope.getExchange() + "," + envelope.getRoutingKey() + "," + envelope.getDeliveryTag() + "," + message);
                // send a message to the server to confirm that the message has been received
                channel.basicAck(envelope.getDeliveryTag(), false/*是否批量確認*/);
            }
        };
        // set the consumer to the channel
        channel.basicConsume(Queue_Name, false/*是否自動確認*/, consumer);
    }
}
