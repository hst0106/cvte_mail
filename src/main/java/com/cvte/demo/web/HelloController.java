package com.cvte.demo.web;

import com.cvte.demo.common.Sender;
import com.cvte.demo.pojo.Mail;
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

    @Autowired
    private Sender sender;

    @PostMapping("/sendDirectQueue")
    @ResponseBody
    public String  sendDirectQueue(Mail mail) {
       // msgProducer.sendMsg("成功了");
        sender.sendDirectQueue(mail);
        return "ok";
    }
}
