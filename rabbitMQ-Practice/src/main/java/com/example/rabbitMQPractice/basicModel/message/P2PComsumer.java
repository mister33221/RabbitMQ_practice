package com.example.rabbitMQPractice.basicModel.message;

import com.example.rabbitMQPractice.connectUtil.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class P2PComsumer {

    final static String QueueName = "firstQueue";

    public static void main(String[] args) throws IOException, TimeoutException {

        // create a connection between the consumer and the rabbitMQ server
        Connection factory = ConnectionUtil.getConnection();
        // create a chennel
        Channel channel = factory.createChannel();
        // declare a queue, if the queue does not exist, create it
        channel.queueDeclare(QueueName, true/*是否持久化*/, false/*是否獨享*/, true/*是否自動刪除*/, null/*其他參數*/);
        // create a consumer
        DefaultConsumer consumer = new DefaultConsumer(channel) {
            // when a message is received, the following method will be called
            // Envelope: the message envelope, which contains the message ID, routing key, exchange name, and message delivery mode
            @Override
            public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(envelope.getExchange() + "," + envelope.getRoutingKey() + "," + envelope.getDeliveryTag() + "," + message);
            }
        };
        // start consuming
        channel.basicConsume(QueueName, true/*是否自動確認*/, consumer);

    }

}
