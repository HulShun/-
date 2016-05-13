package com.example.newsclient.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.LinearLayout;

import com.example.newsclient.Model.bean.VideoItemBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoItemPresenter;
import com.example.newsclient.view.fragment.CommentsFramgent;
import com.example.newsclient.view.impl.IVideoItemViewImpl;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemActivity extends BaseActivity<VideoItemPresenter> implements IVideoItemViewImpl {
    @Bind(R.id.videoitem_player)
    YoukuPlayerView mPlayerView;

    YoukuPlayer mPlayer;

    MyYoukuBasePlayerManager mPlayManager;

    private String title;
    private String id;
    private String[] tabs = {"评论", "相关节目"};

    private VideoItemBean mVideoData;


    @Override
    protected int getToolBarId() {
        return R.id.videoitem_toorbar;
    }


    @Override
    protected void init() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        initYoukuPlayer();
        initViewPager();

        loadData();

    }

    @Bind(R.id.video_vp)
    ViewPager mViewpager;
    @Bind(R.id.videoitem_tablayout)
    TabLayout mTabLayout;

    private void initViewPager() {
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return new CommentsFramgent();
            }

            @Override
            public int getCount() {
                return tabs.length;
            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }

    private void initYoukuPlayer() {
        mPlayManager = new MyYoukuBasePlayerManager(VideoItemActivity.this) {
            @Override
            public void onInitializationSuccess(YoukuPlayer player) {
                // 初始化成功后需要添加该行代码
                addPlugins();
                // 实例化YoukuPlayer实例
                mPlayer = player;
                // 进行播放
                goPlay();
            }
        };
        mPlayManager.onCreate();

        //控制竖屏和全屏时候的布局参数。这两句必填。
        mPlayerView
                .setSmallScreenLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        mPlayerView
                .setFullScreenLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        // 初始化播放器相关数据
        mPlayerView.initialize(mPlayManager);
    }

    private void goPlay() {
        mPlayer.playVideo(id);
    }

    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        getPresenter().loadData(map);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_videoitem;
    }

    @Override
    protected VideoItemPresenter initPresenter() {
        return new VideoItemPresenter();
    }


    @Override
    public void loadVideoItemInform(VideoItemBean data) {
        mVideoData = data;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPlayManager.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mPlayManager.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean managerKeyDown = mPlayManager.onKeyDown(keyCode, event);
        if (mPlayManager.shouldCallSuperKeyDown()) {
            return super.onKeyDown(keyCode, event);
        } else {
            return managerKeyDown;
        }

    }

    @Override
    public void onLowMemory() { // android系统调用
        super.onLowMemory();
        mPlayManager.onLowMemory();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPlayManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayManager.onResume();
    }

    @Override
    public boolean onSearchRequested() { // android系统调用
        return mPlayManager.onSearchRequested();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPlayManager.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlayManager.onStop();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mPlayManager.onConfigurationChanged(newConfig);
    }

    class MyYoukuBasePlayerManager extends YoukuBasePlayerManager {

        public MyYoukuBasePlayerManager(Activity context) {
            super(context);
        }

        @Override
        public void setPadHorizontalLayout() {

        }

        @Override
        public void onInitializationSuccess(YoukuPlayer player) {

        }

        @Override
        public void onFullscreenListener() {

        }

        @Override
        public void onSmallscreenListener() {

        }
    }
}
