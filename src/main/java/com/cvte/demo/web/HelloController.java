package com.cvte.demo.web;

import com.cvte.demo.common.Sender;
import com.cvte.demo.pojo.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private Sender sender;

    @PostMapping("/api/mail/mqSend")
    @ResponseBody
    public String  sendDirectQueue(Mail mail) {
        sender.sendDirectQueue(mail);
        return "ok";
    }

 }
