package com.cvte.demo.pojo;

import java.io.Serializable;

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
