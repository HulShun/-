package com.example.newsclient.Model.bean.video;

/**
 * Created by Administrator on 2016-05-04.
 */
public class VideoBean {

    private String keyword;
    private String cate;
    private String program;
    private int querycount;
    private int trend;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCate() {
        return cate;
    }

    public void setCate(String cate) {
        this.cate = cate;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getQuerycount() {
        return querycount;
    }

    public void setQuerycount(int querycount) {
        this.querycount = querycount;
    }

    public int getTrend() {
        return trend;
    }

    public void setTrend(int trend) {
        this.trend = trend;
    }

}
