package com.jeremy.Customer.bean;

import java.io.Serializable;

/**
 * Created by xiaoshengping on 2015/10/8.
 */
public class MessageBean implements Serializable {

    private String message;
    private String usericon;

    public String getUsericon() {
        return usericon;
    }

    public void setUsericon(String usericon) {
        this.usericon = usericon;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
