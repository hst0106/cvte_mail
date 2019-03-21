package com.cvte.demo.service.impl;

import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.service.AsyncService;
import com.cvte.demo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @Author: huangshuntong
 * @Date: 2019/3/21 9:48
 * @description
 * */


@Service
public class AsyncServiceImpl implements AsyncService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MailService mailService;

    @Override
    @Async("asyncServiceExecutor")
    public ServerResponse<String> executeAsync(Mail mail) {
        logger.info("开始异步执行任务");
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return ServerResponse.createByErrorMessage("邮件内容为空或者或者主题为空或者没有输入接收方");
        }else{
            return mailService.sendAttachment(mail);
        }
    }
}
