package com.cvte.demo.service.impl;

import com.cvte.demo.common.Const;
import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.dao.UserEmailDAO;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.Recevier;
import com.cvte.demo.pojo.UserEmail;
import com.cvte.demo.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.internet.MimeMessage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service("mailService")
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserEmailDAO userEmailDAO;

    @Value("${path.route}")
    private String route;
    //配置发送方接口
    @Override
    public ServerResponse<Integer> insertUserEmail(UserEmail userEmail) {
       UserEmail userEmail1 =  userEmailDAO.save(userEmail);
       if(userEmail == null){
           return ServerResponse.createByErrorMessage("发送方邮件配置失败");
       }
       return ServerResponse.createBySuccess("发送方邮件配置成功",userEmail.getId());
    }

    //发送邮件接口
    @Override
    public ServerResponse<String> sendAttachment(Mail mail) {
        Recevier[] receviers = mail.getReceviers();
        ServerResponse response = null;
        for(Recevier recevier: receviers){
            response = sendMail(recevier,mail.getContent(),mail.getSubject(),
                    mail.getId(),mail.getFile());
        }
        return response;
    }

    public ServerResponse<String> sendMail(Recevier recevier, String content, String subject,
                          int id,MultipartFile file){
        MimeMessage message=javaMailSender.createMimeMessage();
        UserEmail userEmail = userEmailDAO.getOne(id);
        try {
            MimeMessageHelper helper=new MimeMessageHelper(message,true);
            helper.setFrom(userEmail.getEmail());
            if(Const.BCC.equals(recevier.getEmailType())){
                helper.setBcc(recevier.getEmail());
            }else if(Const.CC.equals(recevier.getEmailType())){
                helper.setCc(recevier.getEmail());
            }else{
                helper.setTo(recevier.getEmail());
            }
            helper.setSubject(subject);
            helper.setText(content);
            String filePath = null;
            if(file != null){
                filePath = dealWithAttachment(file);
                FileSystemResource newFile=new FileSystemResource(new File(filePath));
                String fileName=filePath.substring(filePath.lastIndexOf(File.separator));
                //添加多个附件可以使用多条
                //helper.addAttachment(fileName,newFile);
                helper.addAttachment(fileName,newFile);
            }
            javaMailSender.send(message);
            if(filePath != null){
                File closeFile = new File(filePath);
                closeFile.delete();
            }
            System.out.println("带附件的邮件发送成功");
            return ServerResponse.createBySuccessMessage("发送邮件成功");
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
            return ServerResponse.createByErrorMessage("发送邮件失败");
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
        String filePath = route + fileName;
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
}
