package com.example.newsclient.Model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016-04-12.
 */
public class NewsBean {
    private String title;
    @SerializedName("abstract")
    private String abstractX;
    private String url;
    private String datetime;
    private String img_url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractX() {
        return abstractX;
    }

    public void setAbstractX(String abstractX) {
        this.abstractX = abstractX;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
