package com.cvte.demo.pojo;

import org.hibernate.annotations.Proxy;

import java.io.Serializable;

//@Proxy(lazy = false)
public class Recevier implements Serializable{
    private String email;
    private String emailType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}
