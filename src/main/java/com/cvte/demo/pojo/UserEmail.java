package com.cvte.demo.pojo;

import org.hibernate.annotations.Proxy;

import javax.persistence.*;

@Entity
@Table(name = "useremail")
//@Proxy(lazy = false)
public class UserEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    private String email;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
