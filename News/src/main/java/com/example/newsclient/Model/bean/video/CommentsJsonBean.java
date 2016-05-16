package com.example.newsclient.Model.bean.video;

import java.util.List;

/**
 * Created by Administrator on 2016-05-13.
 */
public class CommentsJsonBean {


    /**
     * error : 1
     * total : 30
     * cost : 0.0010538101196289
     * data : [{"_id":"4facfa21cd2c6d4228000002","v":95511637,"u":65542463,"ct":1336736289,"sid":368000590,"rec":0,"rpc":0,"pcid":"","scmt":"","con":"震惊！真是一场大毁灭啊","ip":-1210974066,"ps":999,"st":0}]
     */

    private int error;
    private int total;
    private double cost;
    /**
     * _id : 4facfa21cd2c6d4228000002
     * v : 95511637
     * u : 65542463
     * ct : 1336736289
     * sid : 368000590
     * rec : 0
     * rpc : 0
     * pcid :
     * scmt :
     * con : 震惊！真是一场大毁灭啊
     * ip : -1210974066
     * ps : 999
     * st : 0
     */

    private List<CommentBean> data;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<CommentBean> getData() {
        return data;
    }

    public void setData(List<CommentBean> data) {
        this.data = data;
    }

    public static class CommentBean {
        private String _id;
        private int v;
        private int u;
        private int ct;
        private int sid;
        private int rec;
        private int rpc;
        private String pcid;
        private String scmt;
        private String con;
        private int ip;
        private int ps;
        private int st;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public int getV() {
            return v;
        }

        public void setV(int v) {
            this.v = v;
        }

        public int getU() {
            return u;
        }

        public void setU(int u) {
            this.u = u;
        }

        public int getCt() {
            return ct;
        }

        public void setCt(int ct) {
            this.ct = ct;
        }

        public int getSid() {
            return sid;
        }

        public void setSid(int sid) {
            this.sid = sid;
        }

        public int getRec() {
            return rec;
        }

        public void setRec(int rec) {
            this.rec = rec;
        }

        public int getRpc() {
            return rpc;
        }

        public void setRpc(int rpc) {
            this.rpc = rpc;
        }

        public String getPcid() {
            return pcid;
        }

        public void setPcid(String pcid) {
            this.pcid = pcid;
        }

        public String getScmt() {
            return scmt;
        }

        public void setScmt(String scmt) {
            this.scmt = scmt;
        }

        public String getCon() {
            return con;
        }

        public void setCon(String con) {
            this.con = con;
        }

        public int getIp() {
            return ip;
        }

        public void setIp(int ip) {
            this.ip = ip;
        }

        public int getPs() {
            return ps;
        }

        public void setPs(int ps) {
            this.ps = ps;
        }

        public int getSt() {
            return st;
        }

        public void setSt(int st) {
            this.st = st;
        }
    }
}
