package com.example.newsclient.Model.impl;

import java.util.List;

/**
 * Created by Administrator on 2016-05-17.
 */
public class RecommendJsonVideoBean {

    /**
     * total : 100
     * videos : [{"id":"XMjg1MTcyNDQ0","title":"泡芙小姐的灯泡 11","link":"http://v.youku.com/v_show/id_XMjg1MTcyNDQ0.html","thumbnail":"http://g4.ykimg.com/0100641F464E1FC...","duration":"910","category":"原创","tags":"优酷出品,互象动画,泡芙小姐","state":"normal","view_count":2555843,"favorite_count":534,"comment_count":1000,"up_count":14859,"down_count":559,"published":"2011-07-15 09:00:42","user":{"id":58921428,"name":"泡芙小姐","link":"http://u.youku.com/user_show/id_UMjM1Njg1NzEy.html"},"operation_limit":["COMMENT_DISABLED"],"streamtypes":["flv","flvhd","hd"]}]
     */

    private int total;
    /**
     * id : XMjg1MTcyNDQ0
     * title : 泡芙小姐的灯泡 11
     * link : http://v.youku.com/v_show/id_XMjg1MTcyNDQ0.html
     * thumbnail : http://g4.ykimg.com/0100641F464E1FC...
     * duration : 910
     * category : 原创
     * tags : 优酷出品,互象动画,泡芙小姐
     * state : normal
     * view_count : 2555843
     * favorite_count : 534
     * comment_count : 1000
     * up_count : 14859
     * down_count : 559
     * published : 2011-07-15 09:00:42
     * user : {"id":58921428,"name":"泡芙小姐","link":"http://u.youku.com/user_show/id_UMjM1Njg1NzEy.html"}
     * operation_limit : ["COMMENT_DISABLED"]
     * streamtypes : ["flv","flvhd","hd"]
     */

    private List<RecommendVideoBean> videos;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RecommendVideoBean> getVideos() {
        return videos;
    }

    public void setVideos(List<RecommendVideoBean> videos) {
        this.videos = videos;
    }

    public static class RecommendVideoBean {
        private String id;
        private String title;
        private String link;
        private String thumbnail;
        private String duration;
        private String category;
        private String tags;
        private String state;
        private int view_count;
        private int favorite_count;
        private int comment_count;
        private int up_count;
        private int down_count;
        private String published;
        /**
         * id : 58921428
         * name : 泡芙小姐
         * link : http://u.youku.com/user_show/id_UMjM1Njg1NzEy.html
         */

        private UserBean user;
        private List<String> operation_limit;
        private List<String> streamtypes;

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

        public String getTags() {
            return tags;
        }

        public void setTags(String tags) {
            this.tags = tags;
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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<String> getOperation_limit() {
            return operation_limit;
        }

        public void setOperation_limit(List<String> operation_limit) {
            this.operation_limit = operation_limit;
        }

        public List<String> getStreamtypes() {
            return streamtypes;
        }

        public void setStreamtypes(List<String> streamtypes) {
            this.streamtypes = streamtypes;
        }

        public static class UserBean {
            private int id;
            private String name;
            private String link;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getLink() {
                return link;
            }

            public void setLink(String link) {
                this.link = link;
            }
        }
    }
}
