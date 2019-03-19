package com.cvte.demo.service.impl;

import com.cvte.demo.dao.UserEmailDAO;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.Recevier;
import com.cvte.demo.pojo.UserEmail;
import com.cvte.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    public void sendAttachment(Mail mail) {
        Recevier[] receviers = mail.getReceviers();
        for(Recevier recevier: receviers){
            sendTest(recevier.getEmail(),mail.getContent(),mail.getSubject(),
                    mail.getId(),mail.getFile());
        }
    }

    public void  sendTest(String email, String content, String subject,
                          int id,MultipartFile file){
        MimeMessage message=javaMailSender.createMimeMessage();
        UserEmail userEmail = userEmailDAO.getOne(id);
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(userEmail.getEmail());
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(content);
            String filePath = dealWithAttachment(file);
            FileSystemResource newFile=new FileSystemResource(new File(filePath));
            String fileName=filePath.substring(filePath.lastIndexOf(File.separator));
            //添加多个附件可以使用多条
            //helper.addAttachment(fileName,newFile);
            helper.addAttachment(fileName,newFile);
            javaMailSender.send(message);
            File closeFile = new File(filePath);
            closeFile.delete();
            System.out.println("带附件的邮件发送成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
        }
    }

    //把上传的文件缓存到本项目的upload文件下
    public String dealWithAttachment(MultipartFile file){
        String fileName = file.getOriginalFilename();//获取文件名
        //读取文件的内容
        byte[] bytes = null;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = "G:\\idea\\project\\demo\\upload\\"+fileName;
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
        return filePath;
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
