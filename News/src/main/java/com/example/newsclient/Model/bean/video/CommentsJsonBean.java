package com.example.newsclient.Model.bean.video;

import java.util.List;

/**
 * Created by Administrator on 2016-05-13.
 */
public class CommentsJsonBean {


    /**
     * total : 100
     * video_id : XMjg1MTcyNDQ0
     * comments : [{"id":"4e263a4c67c6e27d4d000000","content":"光是装满爱的房间，光是房间这么说不合适，光是装满房间的爱还差不多。","published":"2011-07-19 10:40:03","user":{"id":28564403,"name":"macanying","link":"http://u.youku.com/user_show/id_UMTE0MjU3NjEy.html"}}]
     */

    private int total;
    private String video_id;
    /**
     * id : 4e263a4c67c6e27d4d000000
     * content : 光是装满爱的房间，光是房间这么说不合适，光是装满房间的爱还差不多。
     * published : 2011-07-19 10:40:03
     * user : {"id":28564403,"name":"macanying","link":"http://u.youku.com/user_show/id_UMTE0MjU3NjEy.html"}
     */

    private List<CommentBean> comments;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public List<CommentBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentBean> comments) {
        this.comments = comments;
    }

    public static class CommentBean {
        private String id;
        private String content;
        private String published;
        /**
         * id : 28564403
         * name : macanying
         * link : http://u.youku.com/user_show/id_UMTE0MjU3NjEy.html
         */

        private UserBean user;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
