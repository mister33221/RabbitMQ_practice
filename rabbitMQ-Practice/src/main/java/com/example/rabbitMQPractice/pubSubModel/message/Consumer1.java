package com.example.rabbitMQPractice.pubSubModel.message;

import com.example.rabbitMQPractice.connectUtil.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer1 {

    final static String Queue_Name = "queue1";

    public static void main(String[] args) throws IOException, TimeoutException {

        // create the connection between the consumer and the rabbitMQ server
        Connection connection = ConnectionUtil.getConnection();
        // create a channel
        Channel channel = connection.createChannel();
        // declare a queue, if the queue does not exist, create it
        channel.queueDeclare(Queue_Name, true/*是否持久化*/, false/*是否獨享*/, true/*是否自動刪除*/, null/*其他參數*/);
        // create a consumer
        Consumer consumer = new DefaultConsumer(channel) {
            // when a message is received, the following method will be called
            @Override
            public void handleDelivery(String consumerTag, com.rabbitmq.client.Envelope envelope, com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Received: " + message);
            }
        };
        // get the message from the queue
//        channel.basicConsume(Queue_Name, true/*是否自動確認*/, consumer);

    }

}
