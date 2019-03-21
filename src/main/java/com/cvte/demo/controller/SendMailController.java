package com.cvte.demo.controller;

import com.cvte.demo.common.Const;
import com.cvte.demo.common.RabbitMQConfig;
import com.cvte.demo.common.Sender;
import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.MailConfig;
import com.cvte.demo.service.MailService;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class SendMailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private Sender sender;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
    * RabbitMQ异步发送邮件api
    * */
    @RabbitListener(queues = RabbitMQConfig.QUEUE)
    public void receiverDirectQueue(Mail mail,
                                    @Headers Map<String,Object> headers,
                                    Channel channel,Message message) throws IOException {
        logger.info("【receiver1监听到QUEUE消息】" + mail.toString());
        Long deliveryType = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        if(send(mail).isSuccess()){
            channel.basicAck(deliveryType,false);
        }else{
            //消息将再次返回队列处理
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }

    /**
    *发送邮件api
    * */
    @PostMapping("/api/mails")
    @ResponseBody
    public ServerResponse<String> sendMessage(Mail mail,Boolean async) throws IOException, ExecutionException, InterruptedException {
        //为true则异步发送
        if(async){
            Future<Integer> future = null;
            future = mailService.executeAsync(mail);
            logger.info("状态显示为（0为成功，1为失败）：" + future.get());
            if(future.get().equals(Const.SUCCESS)){
                return ServerResponse.createBySuccessMessage("提交请求成功");
            }else{
                return ServerResponse.createByErrorMessage("提交请求失败");
            }
        }else{//同步发送
            return send(mail);
        }
    }

    /**
     * 配置发送方api
     * */
    @PostMapping("/api/mails/config")
    public ServerResponse<Integer> configSender(MailConfig mailConfig){
        return mailService.saveMailConfig(mailConfig);
    }

    public ServerResponse<String> send(Mail mail){
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return ServerResponse.createByErrorMessage("邮件内容为空或者或者主题为空或者没有输入接收方");
        }else{
            return mailService.sendMail(mail);
        }
    }

    @PostMapping("/api/mails/mqSend")
    @ResponseBody
    public String  sendDirectQueue(Mail mail) {
        sender.sendDirectQueue(mail);
        return "ok";
    }
}
