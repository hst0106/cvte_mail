package com.cvte.demo.web;

import com.cvte.demo.common.Sender;
import com.cvte.demo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private MailService mailService;

    @Value("${mail.fromMail.addr}")
    private String from;

    //设置发送方的接口

    //private MsgProducer msgProducer = new MsgProducer();
    @Autowired
    private Sender sender;

    @GetMapping("/sendDirectQueue")
    public Object sendDirectQueue() {
       // msgProducer.sendMsg("成功了");
        sender.sendDirectQueue();
        return "ok";
    }




}
