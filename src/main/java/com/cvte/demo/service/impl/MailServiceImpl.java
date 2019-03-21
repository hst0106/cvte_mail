package com.cvte.demo.service.impl;

import com.cvte.demo.common.Const;
import com.cvte.demo.common.ResponseCode;
import com.cvte.demo.common.ServerResponse;
import com.cvte.demo.dao.MailConfigDAO;
import com.cvte.demo.pojo.Mail;
import com.cvte.demo.pojo.MailConfig;
import com.cvte.demo.pojo.Recevier;
import com.cvte.demo.service.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.Future;

@Service("mailService")
public class MailServiceImpl implements MailService{

    @Autowired
    private JavaMailSenderImpl javaMailSenderImpl;

    @Autowired
    private MailConfigDAO mailConfigDAO;

    @Value("${path.route}")
    private String path;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    @Async("asyncServiceExecutor")
    public Future<Integer> executeAsync(Mail mail) {
        logger.info("开始异步执行任务");
        if(mail.getContent()==null || mail.getReceviers() == null || mail.getSubject() == null){
            return new AsyncResult<Integer>(Const.FAILUED);
        }else{
            return new AsyncResult<Integer>(sendMail(mail).getStatus());
        }
    }

    /**
    * 把发送方的配置信息持久化到数据库，并返回唯一ID
    * */
    @Override
    public ServerResponse<Integer> saveMailConfig(MailConfig mailConfig) {
        if(mailConfig.getHost() == null || mailConfig.getPort() == 0
                || mailConfig.getPassword() == null || mailConfig.getUsername() == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        MailConfig mailConfig1 = mailConfigDAO.save(mailConfig);
        if(mailConfig1 == null){
            return ServerResponse.createByErrorMessage("发送方邮件配置失败");
        }
        return ServerResponse.createBySuccess("发送方邮件配置成功",mailConfig1.getId());
    }

    /**
    * 开始发送邮件
    * */
    @Override
    public ServerResponse<String> sendMail(Mail mail) {
        byte[] bytes = null;
        if(mail.getFile() != null){
            try {
                bytes  = mail.getFile().getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Recevier[] receviers = mail.getReceviers();
        ServerResponse<String> response = null;
        for(Recevier recevier: receviers){
            response = sendMailTest(recevier,mail,bytes);
        }
        return response;
    }

    /**
    * 逐个发送
    * */
    public ServerResponse<String> sendMailTest(Recevier recevier,Mail mail,byte[] bytes){
        javaMailSenderImpl.setHost(mailConfigDAO.getOne(mail.getId()).getHost());
        javaMailSenderImpl.setPort(mailConfigDAO.getOne(mail.getId()).getPort());
        javaMailSenderImpl.setUsername(mailConfigDAO.getOne(mail.getId()).getUsername());
        javaMailSenderImpl.setPassword(mailConfigDAO.getOne(mail.getId()).getPassword());
        Properties properties = new Properties();
        properties.put("mail.smtp.auth",true);
        javaMailSenderImpl.setJavaMailProperties(properties);
        MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
            helper.setFrom(mailConfigDAO.getOne(mail.getId()).getUsername());
            if(Const.BCC.equals(recevier.getEmailType())){
                helper.setBcc(recevier.getEmail());
            }else if(Const.CC.equals(recevier.getEmailType())){
                helper.setCc(recevier.getEmail());
            }else{
                helper.setTo(recevier.getEmail());
            }
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getContent());
            String filePath = null;
            if(mail.getFile() != null){
                filePath = dealWithAttachment(mail.getFile(),bytes);
                FileSystemResource newFile=new FileSystemResource(new File(filePath));
                String fileName = mail.getFile().getOriginalFilename();
                helper.addAttachment(fileName,newFile);
            }
            javaMailSenderImpl.send(mimeMessage);
            if(filePath != null){
                File closeFile = new File(filePath);
                closeFile.delete();
            }
            System.out.println("带附件的邮件发送成功");
            return ServerResponse.createBySuccessMessage("发送邮件成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("发送带附件的邮件失败");
            return ServerResponse.createByErrorMessage("发送邮件失败");
        }
    }

    /**
     *
     * 把上传的文件缓存到项目中，并返回文件的路径，发送完再delete
    * */
    public String dealWithAttachment(MultipartFile file,byte[] bytes){
        String fileName = file.getOriginalFilename();
        String filePath = new File("").getAbsolutePath()+"\\"+fileName;
        System.out.println(filePath);
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
