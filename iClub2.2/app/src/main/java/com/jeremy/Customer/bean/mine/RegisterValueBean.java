package com.jeremy.Customer.bean.mine;

import java.io.Serializable;

/**
 * Created by xiaoshengping on 2015/5/27.
 */
public class RegisterValueBean implements Serializable {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "RegisterValueBean{" +
                "message='" + message + '\'' +
                '}';
    }
}