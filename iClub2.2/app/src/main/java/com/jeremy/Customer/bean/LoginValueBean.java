package com.jeremy.Customer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/21.
 */
public class LoginValueBean implements Serializable {
        private   String uid;
        private   String personId;
        private   String userName;
        private   String userIcon;
        private   String state;
        private   String mobile;
        private   String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "LoginValueBean{" +
                "uid='" + uid + '\'' +
                ", userName='" + userName + '\'' +
                ", userIcon='" + userIcon + '\'' +
                ", state='" + state + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
