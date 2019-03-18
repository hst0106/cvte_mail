package com.cvte.demo.service;

public interface MailService {
    /*
    *
    * */
    void sendMail(String to,String subject,String content);

    void sendAttachmentMail(String to,String subject,String content,String filepath);
}
