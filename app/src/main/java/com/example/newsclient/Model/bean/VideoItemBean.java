package com.example.newsclient.Model.bean;

/**
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemBean {

    /**
     * id : XMTU2MzI1NDY4NA==
     * title : 銆婃涔愰銆�40闆嗛鍛婄墖
     * link : http://v.youku.com/v_show/id_XMTU2MzI1NDY4NA==.html
     * thumbnail : http://r1.ykimg.com/05420408572F6B9B6A0A46044AE7AA45
     * bigThumbnail : http://r1.ykimg.com/05410408572F6B9B6A0A46044AE7AA45
     * duration : 164.00
     * category : 鐢佃鍓�
     * state : normal
     * created : 2016-05-09 00:26:09
     * published : 2016-05-09 00:32:10
     * description :
     * player : http://player.youku.com/player.php/sid/XMTU2MzI1NDY4NA==/partnerid/f3a5372f0293848d/v.swf
     * public_type : all
     * copyright_type : original
     * user : {"id":"851539726","name":"鐢佃鍓ф涔愰","link":"http://v.youku.com/user_show/id_UMzQwNjE1ODkwNA==.html"}
     * tags : 娆箰棰�
     * view_count : 2883061
     * favorite_count : 0
     * comment_count : 118
     * up_count : 1704
     * down_count : 2
     * operation_limit : []
     * streamtypes : ["hd2","flvhd","hd","3gphd"]
     * is_panorama : 0
     * ischannel : 0
     * show : {"id":"6b2fc09c55d011e59e2a","name":"娆箰棰�","link":"http://www.youku.com/show_page/id_z6b2fc09c55d011e59e2a.html","paid":0,"pay_type":null,"type":"棰勫憡鐗�","seq":"38","stage":"40","collecttime":"2016-05-09 00:38:44"}
     * source : {"id":"61131","name":"浼橀叿iku","link":"2"}
     */

    private String id;
    private String title;
    private String link;
    private String thumbnail;
    private String bigThumbnail;
    private String duration;
    private String category;
    private String state;
    private String published;
    private String description;
    private String player;
    private String public_type;
    private String copyright_type;
    /**
     * id : 851539726
     * name : 鐢佃鍓ф涔愰
     * link : http://v.youku.com/user_show/id_UMzQwNjE1ODkwNA==.html
     */

    private UserBean user;
    private String tags;
    private int view_count;
    private String favorite_count;
    private String comment_count;
    private String up_count;
    private String down_count;
    private String is_panorama;
    private String ischannel;
    /**
     * id : 6b2fc09c55d011e59e2a
     * name : 娆箰棰�
     * link : http://www.youku.com/show_page/id_z6b2fc09c55d011e59e2a.html
     * paid : 0
     * pay_type : null
     * type : 棰勫憡鐗�
     * seq : 38
     * stage : 40
     * collecttime : 2016-05-09 00:38:44
     */

    private ShowBean show;

    public VideoItemBean() {
    }

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

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPublic_type() {
        return public_type;
    }

    public void setPublic_type(String public_type) {
        this.public_type = public_type;
    }

    public String getCopyright_type() {
        return copyright_type;
    }

    public void setCopyright_type(String copyright_type) {
        this.copyright_type = copyright_type;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public String getFavorite_count() {
        return favorite_count;
    }

    public void setFavorite_count(String favorite_count) {
        this.favorite_count = favorite_count;
    }

    public String getComment_count() {
        return comment_count;
    }

    public void setComment_count(String comment_count) {
        this.comment_count = comment_count;
    }

    public String getUp_count() {
        return up_count;
    }

    public void setUp_count(String up_count) {
        this.up_count = up_count;
    }

    public String getDown_count() {
        return down_count;
    }

    public void setDown_count(String down_count) {
        this.down_count = down_count;
    }

    public String getIs_panorama() {
        return is_panorama;
    }

    public void setIs_panorama(String is_panorama) {
        this.is_panorama = is_panorama;
    }

    public String getIschannel() {
        return ischannel;
    }

    public void setIschannel(String ischannel) {
        this.ischannel = ischannel;
    }

    public ShowBean getShow() {
        return show;
    }

    public void setShow(ShowBean show) {
        this.show = show;
    }

    public static class UserBean {
        private String id;
        private String name;
        private String link;

        public UserBean() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

    public static class ShowBean {
        private String id;
        private String name;
        private String link;
        private int paid;
        private Object pay_type;
        private String type;
        private String seq;
        private String stage;
        private String collecttime;

        public ShowBean() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
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

        public int getPaid() {
            return paid;
        }

        public void setPaid(int paid) {
            this.paid = paid;
        }

        public Object getPay_type() {
            return pay_type;
        }

        public void setPay_type(Object pay_type) {
            this.pay_type = pay_type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getCollecttime() {
            return collecttime;
        }

        public void setCollecttime(String collecttime) {
            this.collecttime = collecttime;
        }
    }
}
