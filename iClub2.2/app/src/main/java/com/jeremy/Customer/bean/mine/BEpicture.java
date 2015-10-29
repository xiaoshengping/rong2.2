package com.jeremy.Customer.bean.mine;

import java.io.Serializable;

/**
 * Created by xiaoshengping on 2015/10/29.
 */
public class BEpicture implements Serializable {
     private int id;
     private String path;
     private String pid;
     private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BEpicture{" +
                "id=" + id +
                ", path='" + path + '\'' +
                ", pid='" + pid + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
