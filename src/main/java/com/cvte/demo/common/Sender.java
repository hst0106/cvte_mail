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
public class Sender{

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback(){
        @Override
        public void confirm(@Nullable CorrelationData correlationData, boolean ack, @Nullable String cause) {
            if(ack){
                logger.info("发送成功");
            }else{
                logger.info("发送失败");
            }
        }
    };

    public void sendDirectQueue(Mail mail) {
        logger.info("【已向队列发送消息】");
        rabbitTemplate.setConfirmCallback(confirmCallback);
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME,"",mail,correlationId);
    }
}
