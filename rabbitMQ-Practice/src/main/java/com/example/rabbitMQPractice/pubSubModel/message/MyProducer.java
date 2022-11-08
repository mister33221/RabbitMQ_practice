package com.example.rabbitMQPractice.pubSubModel.message;


import com.example.rabbitMQPractice.connectUtil.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

// 使用fanout實現消息的發布pub/訂閱sub模型
// fanout: 將消息發送到所有綁定的隊列中

public class MyProducer {

    public final static String EXCHANGE_NAME = "fkjava.fanout";
    public final static String ROUTING_KEY = "test1";

    public static void main(String[] args) throws IOException, TimeoutException {
        // use autoCloseable to close the connection and channel automatically
        try (
                // create a connection between the producer and the rabbitMQ server
                Connection connection = ConnectionUtil.getConnection();
                // create a channel
                Channel channel = connection.createChannel()
        ) {
            // declare an exchange, if the exchange does not exist, create it, set the exchange type to fanout
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT, true/*是否持久化*/, false/*是否自動刪除*/, null/*其他參數*/);
            // declare an exchange, if the exchange does not exist, create it
            channel.queueDeclare(Consumer1.Queue_Name, true/*是否持久化*/, false/*是否獨享*/, true/*是否自動刪除*/, null/*其他參數*/);
            // queueBind: bind a queue to an exchange
            channel.queueBind(Consumer1.Queue_Name, EXCHANGE_NAME, ROUTING_KEY, null);
            // declare an exchange, if the exchange does not exist, create it
            channel.queueDeclare(Consumer2.Queue_Name, true/*是否持久化*/, false/*是否獨享*/, true/*是否自動刪除*/, null/*其他參數*/);
            // queueBind: bind a queue to an exchange
            channel.queueBind(Consumer2.Queue_Name, EXCHANGE_NAME, ROUTING_KEY, null);
            // send a message
            for (int i = 0; i < 10; i++) {
                String message = "Hello RabbitMQ! " + i;
                // basicPublish: publish a message to the specify exchange 向指定的交換器發送消息
                // routingKey: the routing key of the message
                channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, message.getBytes());
                System.out.println("Sent: " + message);
            }
        }
    }
}
