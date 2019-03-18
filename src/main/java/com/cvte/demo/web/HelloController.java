package com.cvte.demo.web;

import com.cvte.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailService mailService;

    @Value("${mail.fromMail.addr}")
    private String from;

    @RequestMapping("/hello")
    public String hello() {
        mailService.sendMail(from,"简单邮件","springboot实现邮件发送");
        return "Hello Spring Boot!";
    }

}
