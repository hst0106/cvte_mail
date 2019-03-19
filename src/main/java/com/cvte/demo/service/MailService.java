package com.cvte.demo.service;

import com.cvte.demo.pojo.UserEmail;

public interface MailService {
    /*
    *
    * */
    void sendMail(String to,String subject,String content);

    void sendAttachmentMail(int id,String subject,String content,String filepath,String... to);

    void insertUserEmail(UserEmail userEmail);
}
