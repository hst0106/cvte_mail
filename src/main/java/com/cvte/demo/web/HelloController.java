package com.cvte.demo.web;

import com.cvte.demo.pojo.UserEmail;
import com.cvte.demo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@RestController
public class HelloController {

    private static Logger logger = LoggerFactory.getLogger(HelloController.class);

    @Autowired
    private MailService mailService;

    @Value("${mail.fromMail.addr}")
    private String from;

    //带附件的邮件发送接口
    @PostMapping("/sendMail")
    public String sendMail(@RequestParam(value = "sendId",defaultValue = "1") Integer sendId,
                           @RequestParam(value = "subject",defaultValue = "带附件的邮件") String subject,
                           @RequestParam(value = "content",defaultValue = "有附件，请查收") String content,
                           @RequestParam(value = "file") MultipartFile file,
                           @RequestParam(value = "to",defaultValue = "1294178282@qq.com") String... to) {
        proxy(file,sendId,subject,content,to);
        return "successfully!!!!!!";
    }

    //设置发送方的接口
    @GetMapping("/sendMail")
    public int setFrom(@RequestParam(defaultValue = "1294178282@qq.com") String email){
        UserEmail userEmail = new UserEmail();
        userEmail.setEmail(email);
        mailService.insertUserEmail(userEmail);
        return userEmail.getId();
    }

    //简单邮件（文本）的发送接口-----测试接口
    public String hello() {
        mailService.sendMail(from,"简单邮件","springboot实现邮件发送");
        for (int i = 0; i < 5; i++) {
            logger.trace("跟踪信息");
            logger.debug("调试信息");
            logger.info("输出信息");
            logger.warn("警告信息");
            logger.error("错误信息");
        }
        return "Hello Spring Boot!";
    }

    public void proxy(MultipartFile file,int sendId,String subject,String content,String... to){
        String string = file.getOriginalFilename();
        byte[] bytes = null;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = "G:\\idea\\project\\demo\\upload\\"+string;
        File f = new File(filePath);
        if(!f.exists()){
            try {
                f.createNewFile();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                        new FileOutputStream(f));
                bufferedOutputStream.write(bytes);
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mailService.sendAttachmentMail(sendId,subject,content,filePath,to);
        f.delete();
    }

}
