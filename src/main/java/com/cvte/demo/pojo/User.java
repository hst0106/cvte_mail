package com.cvte.demo.pojo;

import lombok.Data;
import java.io.Serializable;

@Data
public class User implements Serializable {

    private static final long serialVersionUID = -1262627851741431084L;

    private String userId;

    private String name;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
