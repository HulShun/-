package com.example.newsclient.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.newsclient.Model.LogUtil;
import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.video.VideoItemBean;
import com.example.newsclient.Model.utils.LoginAndShareManager;
import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoItemPresenter;
import com.example.newsclient.view.fragment.VideoBriefFramgent2;
import com.example.newsclient.view.fragment.VideoCommentsFramgent;
import com.example.newsclient.view.impl.IVideoItemViewImpl;
import com.sina.weibo.sdk.api.share.BaseRequest;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;

import butterknife.Bind;

/**
 * 推荐视频的跳转页面
 * Created by Administrator on 2016-05-11.
 */
public class VideoRecommendActivity extends BaseActivity<VideoItemPresenter> implements IVideoItemViewImpl {
    @Bind(R.id.videoitem_player)
    YoukuPlayerView mPlayerView;

    YoukuPlayer mPlayer;

    MyYoukuBasePlayerManager mPlayManager;


    private String id;
    private String[] tabs = {"简介", "评论"};


    @Override
    protected int getToolBarId() {
        return R.id.videoitem_toolbar;
    }

    @Bind(R.id.videoitem_back)
    ImageButton backBtn;
    @Bind(R.id.videoitem_share)
    CheckBox shareBtn;

    private PopupWindow mPopupWindow;
    private VideoItemBean videoData;

    @Override
    protected void init() {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        //获取视频信息
        getPresenter().loadVideoData(id, ModelMode.LOCAL);
        //初始化优酷播放器
        initYoukuPlayer();

        initViewPager();
        //返回按钮
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoRecommendActivity.this.finish();
            }
        });
        //分享按钮
        shareBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int xoff;

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (xoff == 0) {
                    WindowManager manager = getWindowManager();
                    DisplayMetrics dm = new DisplayMetrics();
                    manager.getDefaultDisplay().getMetrics(dm);
                    xoff = dm.widthPixels / 8;
                }
                if (isChecked) {
                    mPopupWindow.showAtLocation(buttonView, Gravity.LEFT | Gravity.TOP, buttonView.getWidth() / 2, 2 * buttonView.getHeight());
                } else {
                    mPopupWindow.dismiss();
                }
            }
        });
        initPopupWindow();


    }


    private CheckedTextView shareWechatBtn;
    private CheckedTextView shareWechatFBtn;
    private CheckedTextView shareQqBtn;
    private CheckedTextView shareWeiboBtn;

    private IWeiboShareAPI mWeiboShareAPI;
    private IWXAPI mWechatAPI;

    private void initPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popupwindow_videoitem, null, false);
        shareWechatBtn = (CheckedTextView) view.findViewById(R.id.share_wechat_btn);
        shareWechatFBtn = (CheckedTextView) view.findViewById(R.id.share_wechat_f_btn);
        shareQqBtn = (CheckedTextView) view.findViewById(R.id.share_qq_btn);
        shareWeiboBtn = (CheckedTextView) view.findViewById(R.id.share_weibo_btn);

        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(null, ""));
        mPopupWindow.setOutsideTouchable(true);

        //QQ分享
        shareQqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToQQ();
                mPopupWindow.dismiss();
            }
        });
        shareWeiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWeibo();
                mPopupWindow.dismiss();
            }
        });

        shareWechatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWechat(SendMessageToWX.Req.WXSceneSession);
                mPopupWindow.dismiss();
            }
        });

        shareWechatFBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToWechat(SendMessageToWX.Req.WXSceneTimeline);
                mPopupWindow.dismiss();
            }
        });
    }


    /**
     * @param flage 分享到聊天窗口还是朋友圈的标识
     */
    private void shareToWechat(int flage) {
        if (videoData == null) {
            Toast.makeText(VideoRecommendActivity.this, "视频信息获取失败...", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginAndShareManager.getInstance().shareToWechat(VideoRecommendActivity.this, videoData, flage);
    }


    private void shareToWeibo() {
        if (videoData == null) {
            Toast.makeText(VideoRecommendActivity.this, "视频信息获取失败...", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginAndShareManager.getInstance().shareToWeibo(VideoRecommendActivity.this, videoData);
    }

    private void shareToQQ() {
        if (videoData == null) {
            Toast.makeText(VideoRecommendActivity.this, "视频信息获取失败...", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginAndShareManager.getInstance().shareToQQ(VideoRecommendActivity.this, videoData);
    }

    @Bind(R.id.video_vp)
    ViewPager mViewpager;
    @Bind(R.id.videoitem_tablayout)
    TabLayout mTabLayout;

    private void initViewPager() {
        mViewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment;
                Bundle bundle = new Bundle();
                bundle.putCharSequence("vid", id);
                if (position == 0) {
                    fragment = new VideoBriefFramgent2();
                } else {
                    fragment = new VideoCommentsFramgent();
                }
                fragment.setArguments(bundle);
                return fragment;
            }

            @Override
            public int getCount() {
                return tabs.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabs[position];
            }
        });
        mTabLayout.setupWithViewPager(mViewpager);
    }

    private void initYoukuPlayer() {
        mPlayManager = new MyYoukuBasePlayerManager(VideoRecommendActivity.this) {
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
        videoData = data;
    }


    @Override
    public void onCompleted() {

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (mWechatAPI != null) {
            mWeiboShareAPI.handleWeiboRequest(intent, new IWeiboHandler.Request() {
                @Override
                public void onRequest(BaseRequest baseRequest) {
                    LogUtil.d("weibo", "分享后返回：" + baseRequest.getType());
                }
            });
        }

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
            getToolBar().setVisibility(View.GONE);
        }

        @Override
        public void onSmallscreenListener() {
            getToolBar().setVisibility(View.VISIBLE);
        }
    }
}
