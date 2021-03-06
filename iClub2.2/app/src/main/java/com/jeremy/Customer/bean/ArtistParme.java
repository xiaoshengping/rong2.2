package com.jeremy.Customer.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoshengping on 2015/9/8.
 */
public class ArtistParme<T> implements Serializable {


    private String state;
    private int total;
    private List<T> value;

    public String getState() {
        return state;
    }

    public List<T> getValue() {
        return value;
    }

    public int getTotal() {
        return total;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setValue(List<T> value) {
        this.value = value;
    }
}

