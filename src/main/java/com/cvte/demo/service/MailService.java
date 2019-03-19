package com.cvte.demo.service;

import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.UserEmail;

public interface MailService {
    ServerResponse<Integer> insertUserEmail(UserEmail userEmail);

    ServerResponse<String> sendAttachment(Mail mail);
}
