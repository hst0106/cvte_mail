package com.cvte.demo.pojo;

import org.hibernate.annotations.Proxy;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

//@Proxy(lazy = false)
public class Mail implements Serializable{

    private Recevier[] receviers;
    private String subject;
    private String content;
    private int id = 1;

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
