package com.cvte.demo.common;

import com.cvte.demo.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Sender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public void sendDirectQueue() {
        for(int i = 1; i <= 5; i++) {
            User user = new User();
            user.setUserId("这是第" + i + "个信息");
            user.setName("黄顺通");
            logger.info("【已向队列发送消息】");
            amqpTemplate.convertAndSend(RabbitMQConfig.FANOUT_EXCHANGE_NAME,"",user);
        }
    }

}
