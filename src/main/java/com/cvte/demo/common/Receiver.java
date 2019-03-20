package com.cvte.demo.common;

import com.cvte.demo.pojo.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiverDirectQueue(User user) {
        logger.info("【receiver1监听到QUEUE消息】" + user.toString());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiverDirectQueueTwo(User user) {

        logger.info("【receiver2监听到QUEUE消息】" + user.toString());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE2)
    public void receiverDirectQueue2(User user) {

        logger.info("【receiver3监听到QUEUE2消息】" + user.toString());
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE2)
    public void receiverDirectQueue2Two(User user) {
        logger.info("【receiver4监听到QUEUE2消息】" + user.toString());
    }
}
