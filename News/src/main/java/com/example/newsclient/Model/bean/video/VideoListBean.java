package com.example.newsclient.Model.bean.video;

import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public class VideoListBean {

    /**
     * provider : searches.soku.top
     * code : 0
     * desc : OK
     */

    private EBean e;
    /**
     * e : {"provider":"searches.soku.top","code":0,"desc":"OK"}
     * total : 2
     * cost : 2
     * data : [{"keyword":"后宫甄嬛传","cate":"1","program":"18629","querycount":75065,"trend":-3464},{"keyword":"偏偏爱上你","cate":"1","program":"","querycount":73190,"trend":-19353}]
     */

    private int total;
    private int cost;
    /**
     * keyword : 后宫甄嬛传
     * cate : 1
     * program : 18629
     * querycount : 75065
     * trend : -3464
     */

    private List<VideoBean> data;

    public EBean getE() {
        return e;
    }

    public void setE(EBean e) {
        this.e = e;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public List<VideoBean> getData() {
        return data;
    }

    public void setData(List<VideoBean> data) {
        this.data = data;
    }

    public static class EBean {
        private String provider;
        private int code;
        private String desc;

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }


}
