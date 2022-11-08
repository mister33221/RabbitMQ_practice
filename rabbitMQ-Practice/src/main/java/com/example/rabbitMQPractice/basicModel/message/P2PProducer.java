package com.example.rabbitMQPractice.basicModel.message;

import com.example.rabbitMQPractice.connectUtil.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

// P2P 消息模型
// 一個生產者對應一個消費者

public class P2PProducer {

    public static void main(String[] args) {

        // use auto closeable to close the connection automatically to manage Connection, Channel, and other resources
        try {
            // create RabbitMQ connection
            Connection connection = ConnectionUtil.getConnection();
            // create a channel
            Channel channel = connection.createChannel();
            // declare a queue, if the queue does not exist, create it
            channel.queueDeclare("firstQueue", true/*是否持久化*/, false/*是否獨享*/, true/*是否自動刪除*/, null/*其他參數*/);
            // send a message
            for (int i = 0; i < 10; i++) {
                String message = "Hello RabbitMQ! " + i;
                channel.basicPublish("", "firstQueue", null, message.getBytes());
                System.out.println("Sent: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
