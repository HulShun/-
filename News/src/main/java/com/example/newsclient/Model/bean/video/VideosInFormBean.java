package com.example.newsclient.Model.bean.video;

import java.util.List;

/**
 * Created by Administrator on 2016-05-10.
 */
public class VideosInFormBean {


    private int total;
    private int page;
    private int count;
    /**
     * id : XMTU2MzI1NDY4NA==
     * title : 娆箰棰� 40闆嗛鍛�
     * link : http://v.youku.com/v_show/id_XMTU2MzI1NDY4NA==.html
     * thumbnail : http://r1.ykimg.com/05420408572F6B9B6A0A46044AE7AA45
     * bigThumbnail : http://r1.ykimg.com/05410408572F6B9B6A0A46044AE7AA45
     * thumbnail_v2 : http://r1.ykimg.com/05420408572F6B9B6A0A46044AE7AA45
     * duration : 164
     * category : 鐢佃鍓�
     * state : normal
     * view_count : 2851802
     * favorite_count : 112
     * comment_count : 124
     * up_count : 1685
     * down_count : 2
     * published : 2016-05-09 00:32:10
     * user : {"id":851539726,"name":null,"link":"http://i.youku.com/u/UMzQwNjE1ODkwNA=="}
     * operation_limit : []
     * streamtypes : ["3gphd","flvhd","hd","hd2"]
     * public_type : all
     * tags :
     * day_vv : 2347181
     */
    /*数据获取时间*/
    private String updateTime;

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    private List<VideosBean> videos;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<VideosBean> getVideos() {
        return videos;
    }

    public void setVideos(List<VideosBean> videos) {
        this.videos = videos;
    }

    public static class VideosBean {
        private String id;
        private String title;
        private String link;
        private String thumbnail;
        private String bigThumbnail;
        private String thumbnail_v2;
        private String duration;
        private String category;
        private String state;
        private int view_count;
        private int favorite_count;
        private int comment_count;
        private int up_count;
        private int down_count;
        private String published;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getBigThumbnail() {
            return bigThumbnail;
        }

        public void setBigThumbnail(String bigThumbnail) {
            this.bigThumbnail = bigThumbnail;
        }

        public String getThumbnail_v2() {
            return thumbnail_v2;
        }

        public void setThumbnail_v2(String thumbnail_v2) {
            this.thumbnail_v2 = thumbnail_v2;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public int getView_count() {
            return view_count;
        }

        public void setView_count(int view_count) {
            this.view_count = view_count;
        }

        public int getFavorite_count() {
            return favorite_count;
        }

        public void setFavorite_count(int favorite_count) {
            this.favorite_count = favorite_count;
        }

        public int getComment_count() {
            return comment_count;
        }

        public void setComment_count(int comment_count) {
            this.comment_count = comment_count;
        }

        public int getUp_count() {
            return up_count;
        }

        public void setUp_count(int up_count) {
            this.up_count = up_count;
        }

        public int getDown_count() {
            return down_count;
        }

        public void setDown_count(int down_count) {
            this.down_count = down_count;
        }

        public String getPublished() {
            return published;
        }

        public void setPublished(String published) {
            this.published = published;
        }
    }
}
