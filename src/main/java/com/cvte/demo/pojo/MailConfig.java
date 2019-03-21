package com.cvte.demo.pojo;

import javax.persistence.*;

/**
 * @Author: huangshuntong
 * @Date: 2019/3/21 20:03
 * @description
 */
@Entity
@Table(name = "mailConfig")
public class MailConfig {

    @Column(name = "host")
    private String host;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "port")
    private int port;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
