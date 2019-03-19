package com.cvte.demo.service.impl;

import com.cvte.demo.dao.UserEmailDAO;
import com.cvte.demo.pojo.UserEmail;
import com.cvte.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Optional;

@Service("mailService")
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${mail.fromMail.addr}")
    private String from;


    @Autowired
    private UserEmailDAO userEmailDAO;

    @Override
    public void sendMail(String to, String subject, String content) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);//发起者
        simpleMailMessage.setTo(to);//接受者
        simpleMailMessage.setTo("1294178282@qq.com");
        //如果发给多个人的：
        //mailMessage.setTo("1xx.com","2xx.com","3xx.com");    
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);
        try {
            javaMailSender.send(simpleMailMessage);
            System.out.println("发送简单邮件");
        }catch (Exception e){
            System.out.println("发送简单邮件失败");
        }
    }

    @Override
    public void sendAttachmentMail(int id,String subject, String content, String filePath,String... to) {
        for(String string : to){
            send(id,string,subject,content,filePath);
        }

    }

    @Override
    public void insertUserEmail(UserEmail userEmail) {
        userEmailDAO.save(userEmail);
    }

    public void send(int id,String to, String subject, String content, String filePath){
        MimeMessage message=javaMailSender.createMimeMessage();
        UserEmail userEmail = userEmailDAO.getOne(id);
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(userEmail.getEmail());
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            FileSystemResource file=new FileSystemResource(new File(filePath));
            String fileName=filePath.substring(filePath.lastIndexOf(File.separator));
            //添加多个附件可以使用多条
            //helper.addAttachment(fileName,file);
            helper.addAttachment(fileName,file);
            javaMailSender.send(message);
            System.out.println("带附件的邮件发送成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
        }
    }
}
