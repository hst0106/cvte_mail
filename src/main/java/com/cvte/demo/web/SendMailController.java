package com.cvte.demo.web;

import com.cvte.demo.common.Const;
import com.cvte.demo.common.RabbitMQConfig;
import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.MailConfig;
import com.cvte.demo.service.AsyncService;
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
    private AsyncService asyncService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
    **同步方式发送邮件api
    * */
    @PostMapping("/api/mail/actions/send")
    public ServerResponse<String> sendEmail(Mail mail){
            return send(mail);
    }

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
        }else if(message.getMessageProperties().getRedelivered()){
            //重复处理失败，拒绝再次接收
            channel.basicReject(deliveryType,true);
        }else{
            //消息将再次返回队列处理
            channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
        }
    }

    /**
    *线程池异步发送邮件api
    * */
    @PostMapping("/api/mail/actions/threadPool/send")
    @ResponseBody
    public ServerResponse<String> sendMessage(Mail mail) throws IOException, ExecutionException, InterruptedException {
        Future<Integer> future = null;
        future = asyncService.executeAsync(mail);
        logger.info("状态显示为（0为成功，1为失败）：" + future.get());
        if(future.get().equals(Const.SUCCESS)){
            return ServerResponse.createBySuccessMessage("提交请求成功");
        }else{
            return ServerResponse.createByErrorMessage("提交请求失败");
        }
    }

    /**
     * 配置发送方api
     * */
    @PostMapping("/api/mail/config")
    public ServerResponse<Integer> configSender(MailConfig mailConfig){
        return mailService.saveMailConfig(mailConfig);
    }

    public ServerResponse<String> send(Mail mail){
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return ServerResponse.createByErrorMessage("邮件内容为空或者或者主题为空或者没有输入接收方");
        }else{
            return mailService.sendAttachment(mail);
        }
    }
}
