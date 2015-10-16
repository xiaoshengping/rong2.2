package com.jeremy.Customer.bean.mine;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/5/20.
 */
public class Picture implements Serializable {
    private String id;
    private int path;
    private int pid;
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPath() {
        return path;
    }

    public void setPath(int path) {
        this.path = path;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
