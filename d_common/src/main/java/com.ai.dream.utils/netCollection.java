package com.ai.dream.utils;

import java.io.Serializable;
import java.util.Date;

public class netCollection implements Serializable {

    private static final long serialVersionUID  = 6777772888199921121L;

    private long id;

    private String title;

    private String morder;

    private String hot;

    private Date createDate;

    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMorder() {
        return morder;
    }

    public void setMorder(String morder) {
        this.morder = morder;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "netCollection{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", morder='" + morder + '\'' +
                ", hot='" + hot + '\'' +
                ", createDate=" + createDate +
                ", state='" + state + '\'' +
                '}';
    }

}