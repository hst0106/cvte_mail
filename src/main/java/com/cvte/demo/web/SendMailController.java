package com.cvte.demo.web;

import com.cvte.demo.pojo.Mail;
import com.cvte.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
@Controller
public class SendMailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send_Attachment_Email")
    public String sendEmail( Mail mail){
            mailService.sendAttachment(mail);
        return "you are ok";
    }
}
