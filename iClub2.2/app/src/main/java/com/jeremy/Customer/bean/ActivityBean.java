package com.jeremy.Customer.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/15.
 */
public class ActivityBean implements Serializable {
    private String activitieid;
    private String content;
    private String date;
    private String image;
    private String title;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getActivitieid() {
        return activitieid;
    }

    public void setActivitieid(String activitieid) {
        this.activitieid = activitieid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
