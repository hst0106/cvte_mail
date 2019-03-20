package com.cvte.demo.common;

import com.cvte.demo.pojo.Mail;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class Sender implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnCallback{

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public void sendDirectQueue(Mail mail) {
        logger.info("【已向队列发送消息】");
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME,"",mail,correlationId);
    }


    //消息发送确认，分两步，是否到达交换器，是否到达队列
    @Override
    public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
        logger.info(" 回调id:" + correlationData);
        if (ack) {
            logger.info("消息发送成功");
        } else {
            logger.info("消息发送失败:" + cause);
        }
    }

    //没有到达队列时触发
    @Override
    public void returnedMessage(Message message, int i, String s, String s1, String s2) {
        System.out.println(message+"  "+i+"  " + s + s1 + s2);
    }
}
