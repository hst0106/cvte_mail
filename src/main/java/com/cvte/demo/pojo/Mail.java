package com.cvte.demo.pojo;

import org.springframework.web.multipart.MultipartFile;

public class Mail {

    private Recevier[] receviers = null;//接收对象数组
    private String subject = "对方没写相关内容";//主题
    private String content= "无内容";//内容
    private int id = 1;//发送人id，默认为1

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    private MultipartFile file = null;

    public Recevier[] getReceviers() {
        return receviers;
    }

    public void setReceviers(Recevier[] receviers) {
        this.receviers = receviers;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
