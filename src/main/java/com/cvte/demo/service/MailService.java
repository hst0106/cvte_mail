package com.cvte.demo.service;

import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.Recevier;
import com.cvte.demo.pojo.UserEmail;
import org.springframework.web.multipart.MultipartFile;

public interface MailService {
    /*
    *
    * */
    void sendMail(String to,String subject,String content);

    void sendAttachmentMail(int id,String subject,String content,String filepath,String... to);

    void insertUserEmail(UserEmail userEmail);

    void sendAttachment(Mail mail);
}
