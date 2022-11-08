package com.example.rabbitMQPractice.connectUtil;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {

    public static Connection getConnection() throws IOException, TimeoutException {
        // ConnectionFactory: a factory to create connections
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        // if the virtual host is not set, the default virtual host is "/"
        factory.setVirtualHost("/");
        return factory.newConnection();
    }
}
