package com.cvte.demo.web;

import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.UserEmail;
import com.cvte.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SendMailController {

    @Autowired
    private MailService mailService;

    @ResponseBody
    @PostMapping("/send_Attachment_Email")
    public ServerResponse<String> sendEmail(Mail mail){
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return ServerResponse.createByErrorMessage("邮件内容为空或者或者主题为空没有输入接收方");
        }else{
            return mailService.sendAttachment(mail);
        }
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
}
