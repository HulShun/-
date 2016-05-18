package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageTypeJsonBean;
import com.example.newsclient.Model.bean.video.CommentsJsonBean;
import com.example.newsclient.Model.bean.video.Commentsv2JsonBean;
import com.example.newsclient.Model.bean.video.VideoCommentUserBean;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.example.newsclient.Model.bean.video.VideosInFormBean;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016-04-12.
 */
public interface ApiService {
    /**
     * 热点新闻
     *
     * @param keyword 查询的关键词
     * @param page    页数
     * @param count   每页的条目数
     * @return
     */
    @GET("search_news")
    Observable<NewsListBean> loadNewsList(@Query("keyword") String keyword,
                                          @Query("page") String page,
                                          @Query("count") String count);


    /**
     * 优酷 节目分类信息
     *
     * @return
     */
    @GET("v2/schemas/video/category.json")
    Observable<VideoTypeBean> loadVideoType();


    @GET("v2/videos/by_category.json")
    Observable<VideosInFormBean> loadVideosInform(@Query("client_id") String client_id,
                                                  @Query("category") String category,
                                                  @Query("period") String period,
                                                  @Query("orderby") String orderby,
                                                  @Query("page") String page,
                                                  @Query("count") String count);

    /**
     * youku单条视频详细信息
     *
     * @param client_id
     * @param video_id
     * @param ext
     * @return
     */
    @GET("v2/videos/show.json")
    Observable<VideoItemBean> loadVideoItem(@Query("client_id") String client_id,
                                            @Query("video_id") String video_id,
                                            @Query("ext") String ext);


    /**
     * 智能推荐视频
     *
     * @param client_id
     * @param video_id
     * @return
     */
    @GET("v2/videos/by_related.json")
    Observable<RecommendJsonVideoBean> loadRecommendVideo(@Query("client_id") String client_id,
                                                          @Query("video_id") String video_id);

    /**
     * 评论 V3版本
     *
     * @param opensysparams
     * @param vid
     * @return
     */
    @GET("router/rest.json")
    Observable<CommentsJsonBean> loadVideoComments(@Query("opensysparams") String opensysparams,
                                                   @Query("vid") String vid
    );

    /**
     * 评论V2版本
     *
     * @param client_id
     * @param video_id
     * @param page
     * @return
     */
    @GET("v2/comments/by_video.json")
    Observable<Commentsv2JsonBean> loadVideoComments(@Query("client_id") String client_id,
                                                     @Query("video_id") String video_id,
                                                     @Query("page") String page);

    /**
     * 单条人物详细信息
     *
     * @param client_id
     * @param video_id
     * @return
     */
    @GET("v2/persons/show.json")
    Observable<VideoCommentUserBean> loadCommentUsers(@Query("client_id") String client_id,
                                                      @Query("person_id") String video_id);


    @GET("router/rest.json")
    Observable<VideoCommentUserBean> loadCommentUser(@Query("opensysparams") String opensysparams,
                                                     @Query("q") String q,
                                                     @Query("fd") String fd);


    /**
     * 美图大全api
     *
     * @param type
     * @param page
     * @return
     */
    @GET("pic/pic_search")
    Observable<ImageJsonBean> loadImages(@Query("type") String type,
                                         @Query("page") String page);

    /**
     * 美图大全的分类
     *
     * @return
     */
    @GET("pic/pic_type")
    Observable<ImageTypeJsonBean> loadImageTypes();
}
