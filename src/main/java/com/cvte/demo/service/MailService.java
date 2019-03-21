package com.cvte.demo.service;

import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.MailConfig;

public interface MailService {

    ServerResponse<String> sendAttachment(Mail mail);

    ServerResponse<Integer> saveMailConfig(MailConfig mailConfig);
}
