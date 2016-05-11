package com.example.newsclient.Model.impl;

import com.example.newsclient.Model.bean.ImageJsonBean;
import com.example.newsclient.Model.bean.ImageTypeJsonBean;
import com.example.newsclient.Model.bean.NewsListBean;
import com.example.newsclient.Model.bean.VideoItemBean;
import com.example.newsclient.Model.bean.VideoListBean;
import com.example.newsclient.Model.bean.VideoTypeBean;
import com.example.newsclient.Model.bean.VideosInFormBean;

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


    /**
     * 优酷 多条视频基本信息 api
     *
     * @param params
     * @param query
     * @param field
     * @param caller
     * @return
     */
    @GET("router/rest.json")
    Observable<VideoListBean> loadVideos(@Query("opensysparams") String params,
                                         @Query("q") String query,
                                         @Query("fd") String field,
                                         @Query("cl") String caller);


    @GET("v2/videos/by_category.json")
    Observable<VideosInFormBean> loadVideosInform(@Query("client_id") String client_id,   //应用key
                                                  @Query("category") String category,    //分类（label）
                                                  @Query("genre") String genre,          //子分类
                                                  @Query("period") String period,        //时间范围
                                                  @Query("orderby") String orderby,       //排序方式
                                                  @Query("page") String page,
                                                  @Query("count") String count);

    @GET("v2/videos/by_category.json")
    Observable<VideosInFormBean> loadVideosInform(@Query("client_id") String client_id,
                                                  @Query("category") String category,
                                                  @Query("period") String period,
                                                  @Query("orderby") String orderby,
                                                  @Query("page") String page,
                                                  @Query("count") String count);

    @GET("v2/videos/show.json")
    Observable<VideoItemBean> loadVideoItem(@Query("client_id") String client_id,
                                            @Query("video_id") String video_id,
                                            @Query("ext") String ext);


    @GET("router/rest.json")
    Observable<String> loadVideoM8u3(@Query("opensysparams") String opensysparams,
                                     @Query("_id") String _id);

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
