package com.example.newsclient.Model.bean.video;

import java.util.List;

/**
 * Created by Administrator on 2016-05-16.
 */
public class Commentsv2JsonBean {
    /**
     * total : 2
     * page : 1
     * count : 20
     * video_id : XMTU3MjExNzY5Ng==
     * comments : [{"id":"57394cdb9329233c3c352560","content":"还没上映怎么就有资源了[赞]","published":"2016-05-16 12:30:19","user":{"id":73577419,"name":"摇滚小鼠","link":"http://i.youku.com/u/UMjk0MzA5Njc2"},"source":{"name":"iPhone客户端","link":"http://mobile.youku.com"}},{"id":"57394ac1727600900e624f09","content":"[赞][赞][赞]","published":"2016-05-16 12:21:21","user":{"id":142874596,"name":"渣萌","link":"http://i.youku.com/u/UNTcxNDk4Mzg0"},"source":{"name":"优酷","link":"http://www.youku.com"}}]
     */

    private int total;
    private int page;
    private int count;
    private String video_id;
    /**
     * id : 57394cdb9329233c3c352560
     * content : 还没上映怎么就有资源了[赞]
     * published : 2016-05-16 12:30:19
     * user : {"id":73577419,"name":"摇滚小鼠","link":"http://i.youku.com/u/UMjk0MzA5Njc2"}
     * source : {"name":"iPhone客户端","link":"http://mobile.youku.com"}
     */

    private List<CommentBean> comments;

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
         * id : 73577419
         * name : 摇滚小鼠
         * link : http://i.youku.com/u/UMjk0MzA5Njc2
         */

        private UserBean user;
        private UserBean source;

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

        public UserBean getSource() {
            return source;
        }

        public void setSource(UserBean source) {
            this.source = source;
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
