package com.jeremy.Customer.bean.mine;

import java.io.Serializable;

/**
 * Created by xiaoshengping on 2015/6/19.
 */
public class BMerchantValueBean implements Serializable {
    private String BEcompanyName;
    private String BEphone;
    private String BEemail;
    private String BEweb;
    private String BEaddress;
    private String BEcompanyInfo;
    private String BEicon;

    public String getBEcompanyInfo() {
        return BEcompanyInfo;
    }

    public void setBEcompanyInfo(String BEcompanyInfo) {
        this.BEcompanyInfo = BEcompanyInfo;
    }

    public String getBEicon() {
        return BEicon;
    }

    public void setBEicon(String BEicon) {
        this.BEicon = BEicon;
    }

    public String getBEcompanyName() {
        return BEcompanyName;
    }

    public void setBEcompanyName(String BEcompanyName) {
        this.BEcompanyName = BEcompanyName;
    }

    public String getBEphone() {
        return BEphone;
    }

    public void setBEphone(String BEphone) {
        this.BEphone = BEphone;
    }

    public String getBEemail() {
        return BEemail;
    }

    public void setBEemail(String BEemail) {
        this.BEemail = BEemail;
    }

    public String getBEweb() {
        return BEweb;
    }

    public void setBEweb(String BEweb) {
        this.BEweb = BEweb;
    }

    public String getBEaddress() {
        return BEaddress;
    }

    public void setBEaddress(String BEaddress) {
        this.BEaddress = BEaddress;
    }

    @Override
    public String toString() {
        return "BMerchantValueBean{" +
                "BEcompanyName='" + BEcompanyName + '\'' +
                ", BEphone='" + BEphone + '\'' +
                ", BEemail='" + BEemail + '\'' +
                ", BEweb='" + BEweb + '\'' +
                ", BEaddress='" + BEaddress + '\'' +
                ", BEcompanyInfo='" + BEcompanyInfo + '\'' +
                ", BEicon='" + BEicon + '\'' +
                '}';
    }
}
