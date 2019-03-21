package com.cvte.demo.service.impl;

import com.cvte.demo.common.Const;
import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.service.AsyncService;
import com.cvte.demo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

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
    public Future<Integer> executeAsync(Mail mail) {
        logger.info("开始异步执行任务");
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return new AsyncResult<Integer>(Const.FAILUED);
        }else{
             return new AsyncResult<Integer>(mailService.sendAttachment(mail).getStatus());
        }
    }
}
