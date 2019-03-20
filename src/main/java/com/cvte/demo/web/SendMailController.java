package com.cvte.demo.web;

import com.cvte.demo.common.RabbitMQConfig;
import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.UserEmail;
import com.cvte.demo.service.MailService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendMailController {

    @Autowired
    private MailService mailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ResponseBody
    @PostMapping("/send_Attachment_Email")
    public ServerResponse<String> sendEmail(Mail mail){
            return send(mail);
    }

    @ResponseBody
    @PostMapping("/configMail")
    public ServerResponse<Integer> setFrom(@RequestParam(value = "email") String email){
        if(email == null){
            return ServerResponse.createByErrorMessage("发送方邮箱输入为空");
        }
        UserEmail userEmail = new UserEmail();
        userEmail.setEmail(email);
        return mailService.insertUserEmail(userEmail);
    }


    //监听队列接口
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiverDirectQueue(Mail mail) {
        logger.info("【receiver1监听到QUEUE消息】" + mail.toString());
        send(mail);
    }

    public ServerResponse<String> send(Mail mail){
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return ServerResponse.createByErrorMessage("邮件内容为空或者或者主题为空或者没有输入接收方");
        }else{
            return mailService.sendAttachment(mail);
        }
    }


}
