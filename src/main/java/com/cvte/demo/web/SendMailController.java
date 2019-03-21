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
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
public class SendMailController {

    @Autowired
    private MailService mailService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    //普通发送邮件接口
    @ResponseBody
    @PostMapping("/send_Attachment_Email")
    public ServerResponse<String> sendEmail(Mail mail){
            return send(mail);
    }

    //配置发送方接口
    @ResponseBody
    @PostMapping("/configMail")
    public ServerResponse<Integer> setFrom(@RequestParam(value = "email",required = false) String email){
        UserEmail userEmail = new UserEmail();
        userEmail.setEmail(email);
        return mailService.insertUserEmail(userEmail);
    }


    //监听队列接口
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiverDirectQueue(Mail mail,
                                    @Headers Map<String,Object> headers,
                                    Channel channel,Message message) throws IOException {
        logger.info("【receiver1监听到QUEUE消息】" + mail.toString());
        Long deliveryType = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        if(send(mail).isSuccess()){
            try {
                channel.basicAck(deliveryType,false);
            } catch (IOException e) {
                e.printStackTrace();
                if(message.getMessageProperties().getRedelivered()){
                    //重复处理失败，拒绝再次接收
                    channel.basicReject(deliveryType,true);
                }else{
                    //消息将再次返回队列处理
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
                }
            }
        }
    }

    public ServerResponse<String> send(Mail mail){
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return ServerResponse.createByErrorMessage("邮件内容为空或者或者主题为空或者没有输入接收方");
        }else{
            return mailService.sendAttachment(mail);
        }
    }
}
