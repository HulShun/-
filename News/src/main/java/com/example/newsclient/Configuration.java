package com.example.newsclient;

/**
 * Created by Administrator on 2016-04-12.
 */
public class Configuration {
    public final static String HTTP_URL = "http://apis.baidu.com/songshuxiansheng/real_time/s";
    public final static String HTTP_ARG = "b6a9a8eb83a23e67479eb27b2d3ea365";

    public final static String IMAGE_TYPE_URL = "http://apis.baidu.com/showapi_open_bus/pic/pic_type";

    public final static String KEYWORD = "keyword";

    public static final String NEWS_DB_NAME = "news.db";
    public static final String NEWS_TABLE_NAME = "news";

    public static final String IMAGE_TYPE_DB_NAEM = "image_type.db";
    public static final String IMAGE_MAIN_TYPE_TABLE_NAEM = "image_main_type";
    public static final String IMAGE_TYPE_TABLE_NAEM = "image_type";
    public static final String IMAGE_INFORM_TABLE_NAEM = "image_inform";
    public static final String IMAGE_TABLE_NAEM = "images";

    public static final String YOUKU_CLIENT_ID = "f3a5372f0293848d";
    public static final String YOUKU_SECRET = "52814a350fb11005df8440835488fef9";

    public final static String YOUKU_API_BASE_URL = "https://openapi.youku.com/";
    public static final String VIDEO_TYPE_DB_NAEM = "video_type.db";
    public static final String VIDEO_TYPE_TABLE_NAEM = "video_type";
    public static final String VIDEO_TYPE_SUBTITLE_TABLE_NAEM = "video_subtitle_type";

    public static final String VIDEO_DB_NAEM = "video.db";
    public static final String VIDEOS_TABLE_NAEM = "videos";
    public static final String VIDEOITEM_TABLE_NAEM = "video_detail";


    public static final String TENCENT_APPID = "1105409954";

    public static final String WECHAT_APPID = "wxafea8174545dfea9";
    public static final String WECHAT_SECRET = "9cda3bb37c092c69da7d9e8a4d89f3d3";

    public static final String WEIBO_APPID = "96968995";
    public static final String WEIBO_SECRET = "54c0c58bb9961aea2ba1f98a2066072b";
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String WEIBO_SCOPE = // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

}
