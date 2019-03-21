package com.cvte.demo.service;

import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.MailConfig;

import java.util.concurrent.Future;

public interface MailService {

    ServerResponse<String> sendMail(Mail mail);

    ServerResponse<Integer> saveMailConfig(MailConfig mailConfig);

    Future<Integer> executeAsync(Mail mail);
}
