package com.jeremy.Customer.bean.mine;

import java.io.Serializable;

/**
 * Created by xiaoshengping on 2015/10/30.
 */
public class CommentcountValueBean implements Serializable {
    private String body;
    private String time;
    private String resumeName;
    private String companyName;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResumeName() {
        return resumeName;
    }

    public void setResumeName(String resumeName) {
        this.resumeName = resumeName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "CommentcountValueBean{" +
                "body='" + body + '\'' +
                ", time='" + time + '\'' +
                ", resumeName='" + resumeName + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}
