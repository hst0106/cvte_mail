package com.cvte.demo.web;

import com.cvte.demo.common.Const;
import com.cvte.demo.common.Sender;
import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.service.AsyncService;
import com.cvte.demo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private MailService mailService;

    @Autowired
    private Sender sender;

    @Autowired
    private AsyncService asyncService;

    //rabbitmq测试接口
    @PostMapping("/sendDirectQueue")
    @ResponseBody
    public String  sendDirectQueue(Mail mail) {
        sender.sendDirectQueue(mail);
        return "ok";
    }

    //线程池测试接口
    @PostMapping("/send_witn_threadPool")
    @ResponseBody
    public ServerResponse<String> sendMessage(Mail mail) throws IOException, ExecutionException, InterruptedException {
        Future<Integer> future = null;
        future = asyncService.executeAsync(mail);
        logger.info(future.get()+"状态");
        if(future.get().equals(Const.SUCCESS)){
            return ServerResponse.createBySuccessMessage("提交请求成功");
        }else{
            return ServerResponse.createByErrorMessage("提交请求失败");
        }
    }
 }
