package com.example.newsclient.view.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.QQUserInfro;
import com.example.newsclient.Model.bean.TencentOpenBean;
import com.example.newsclient.Model.bean.image.ImageMainTypeBean;
import com.example.newsclient.Model.bean.video.VideoTypeBean;
import com.example.newsclient.MyApplication;
import com.example.newsclient.view.utils.AppUtil;
import com.example.newsclient.Model.utils.TencentUtil;
import com.example.newsclient.R;
import com.example.newsclient.presenter.MainViewPresenter;
import com.example.newsclient.view.Constant;
import com.example.newsclient.view.adapter.MyFragmentAdapter;
import com.example.newsclient.view.fragment.ImageClassifyFragment;
import com.example.newsclient.view.fragment.NewsClassifyFragment;
import com.example.newsclient.view.fragment.VideoClassifyFramgent;
import com.example.newsclient.view.impl.IMainViewImpl;
import com.example.newsclient.view.service.NetWorkBroadcastReceiver;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.util.List;

import butterknife.Bind;

public class MainActivity extends BaseActivity<MainViewPresenter> implements IMainViewImpl {


    private FragmentPagerAdapter mFragmentAdapter;
    private FragmentManager mFragmentManager;
    private List<String> mTab_list;

    private NetWorkBroadcastReceiver networkReceiver;
    private int nowMenuItemId;

    @Bind(R.id.tablayout)
    TabLayout tablayout;
    @Bind(R.id.main_viewpager)
    ViewPager mainViewpager;

    @Bind(R.id.main_navi)
    NavigationView mainNavi;
    private SearchView searchView;


    //侧边栏
    @Bind(R.id.main_drawer)
    DrawerLayout mainDrawer;


    TextView mUserName_tv, mUserMessage_tv;
    ImageView mUserhead_iv;
    Button qqlogin_btn, weibo_login_btn;
    LinearLayout login_layout;


    /*网络无连接提示条布局*/
    @Bind(R.id.main_messageview)
    RelativeLayout messageView;
    /*网络无连接提示条中的文本*/
    @Bind(R.id.main_messagetext)
    TextView messageText;
    /*网络无连接提示条中的按钮*/
    @Bind(R.id.main_messagebtn)
    TextView messageBtn;


    @Override
    protected void init() {
        initViews();
        initDatas();
        register();
    }

    /**
     * 注册网络变化广播
     */
    private void register() {
        networkReceiver = new NetWorkBroadcastReceiver(myHandler);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkReceiver, intentFilter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected int getToolBarId() {
        return R.id.main_toolbar;
    }

    @Override
    protected MainViewPresenter initPresenter() {
        return new MainViewPresenter();
    }

    private void initDatas() {
        mFragmentManager = getSupportFragmentManager();
        nowMenuItemId = R.id.nav_news;
        mainNavi.setCheckedItem(nowMenuItemId);
        getPresenter().loadNewsType(this);

        String userType = MyApplication.getInstance().getUserTpye();
        if (userType.equals("qq")) {
            initQQLogin();
            getPresenter().getQQUserInfo(mTencent, MainActivity.this);
        }
    }

    private void initViews() {
        //不统一icon的颜色
        mainNavi.setItemIconTintList(null);

        //tab文本居中
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        //tab可滑动
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        if (!AppUtil.getInstance().isNetWorkConnected()) {
            myHandler.sendEmptyMessage(Constant.UPDATE_NONETWORK);
        }

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myHandler.sendEmptyMessage(Constant.UPDATE_NETWORK);
            }
        });

        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainDrawer.openDrawer(Gravity.LEFT);
            }
        });
        initHeaderView();


    }

    /**
     * 侧边栏
     */
    private void initHeaderView() {
        //导航栏菜单事件
        mainNavi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                item.setChecked(true);
                switch (id) {
                    //新闻
                    case R.id.nav_news:
                        mainDrawer.closeDrawers();
                        if (nowMenuItemId != id) {
                            getPresenter().loadNewsType(MainActivity.this);
                        }
                        break;
                    //图片
                    case R.id.nav_image:
                        mainDrawer.closeDrawers();
                        if (nowMenuItemId != id) {
                            getPresenter().loadImageTpyes();
                        }
                        break;
                    //视频
                    case R.id.nav_video:
                        mainDrawer.closeDrawers();
                        if (nowMenuItemId != id) {
                            getPresenter().loadVideoTypes();
                        }
                        break;

                }
                nowMenuItemId = id;
                return true;
            }
        });
        View view = mainNavi.getHeaderView(0);
        login_layout = (LinearLayout) view.findViewById(R.id.main_header_login_layout);
        qqlogin_btn = (Button) view.findViewById(R.id.main_header_login_qq_btn);
        weibo_login_btn = (Button) view.findViewById(R.id.main_header_login_weibo_btn);
        mUserName_tv = (TextView) view.findViewById(R.id.header_name);
        mUserhead_iv = (ImageView) view.findViewById(R.id.header_imageView);
        mUserMessage_tv = (TextView) view.findViewById(R.id.header_msg);

        login_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);*/
            }
        });
        //微博登录
        weibo_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressDialog == null) {
                    initProgressDialog();
                }
                mProgressDialog.show();

                if (mWeiBoAuthInfo == null) {
                    initWeiboLogin();
                }
                getPresenter().weiboLogin(mWeiboSsonHandler, MainActivity.this);

            }
        });
        //QQ登录按钮
        qqlogin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProgressDialog == null) {
                    initProgressDialog();
                }
                mProgressDialog.show();

                if (mTencent == null) {
                    initQQLogin();
                }

                getPresenter().qqlogin(mTencent, MainActivity.this);
            }
        });
    }

    /*QQ登陆*/
    private Tencent mTencent;
    private IUiListener mTencentListener;
    /*微博登陆*/
    private AuthInfo mWeiBoAuthInfo;
    /*SSO即客户端登陆模式*/
    private SsoHandler mWeiboSsonHandler;

    private ProgressDialog mProgressDialog;

    private void initProgressDialog() {
        mProgressDialog = new ProgressDialog(MainActivity.this);
        mProgressDialog.setMessage("正在加载...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    private void initWeiboLogin() {
        mWeiBoAuthInfo = new AuthInfo(MainActivity.this, Configuration.WEIBO_APPID, Configuration.WEIBO_REDIRECT_URL, "statuses_to_me_read");
        mWeiboSsonHandler = new SsoHandler(MainActivity.this, mWeiBoAuthInfo);
    }

    private void initQQLogin() {
        //QQ登陆对象实例化
        mTencent = Tencent.createInstance(Configuration.TENCENT_APPID, this.getApplicationContext());
        TencentOpenBean data = TencentUtil.getToken(MainActivity.this);
        mTencent.setOpenId(data.getOpenid());
        mTencent.setAccessToken(data.getAccess_token(), String.valueOf(data.getExpires_in()));
    }


    @Override
    public void showNoNetWork() {
        Toast.makeText(MainActivity.this, "网络连接失败了...", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onImageTabs(List<ImageMainTypeBean> tabs) {
        if (tabs.size() == 0) {
            showFaild("tab获取失败");
            return;
        }
        mFragmentAdapter = new MyFragmentAdapter(mFragmentManager, tabs, ImageClassifyFragment.class);
        setViewpagerAndTablayout();
    }

    @Override
    public void onVideoTabs(List<VideoTypeBean.VideoCategoriesBean> tabs) {
        if (tabs.size() == 0) {
            showFaild("tab获取失败");
            return;
        }

        mFragmentAdapter = new MyFragmentAdapter(mFragmentManager, tabs, VideoClassifyFramgent.class);
        setViewpagerAndTablayout();
    }

    @Override
    public void onNewsTabs(List<String> tabs) {
        mFragmentAdapter = new MyFragmentAdapter(mFragmentManager, tabs, NewsClassifyFragment.class);
        setViewpagerAndTablayout();
    }

    @Override
    public void onQQUserInfoResult(QQUserInfro qqUserInfro) {
        mUserName_tv.setText(qqUserInfro.getNickname());
        ImageLoader.getInstance().displayImage(qqUserInfro.getFigureurl_qq_2(), mUserhead_iv, AppUtil.getInstance().getHeadImageOptions());
        login_layout.setVisibility(View.GONE);
    }


    public void setViewpagerAndTablayout() {

        List<Fragment> fragments = mFragmentManager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            fragments.clear();
        }
        mainViewpager.setAdapter(mFragmentAdapter);
        tablayout.setupWithViewPager(mainViewpager);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkReceiver);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ArticleActivity.ARTICLE_CODE:
                //从新闻详情页返回，要将tab显示为当前的新闻所在的tab页
                Bundle bundle = data.getBundleExtra("article");
                String key = bundle.getString("key");
                int position = 0;
                for (int i = 0; i < mTab_list.size(); i++) {
                    if (key.equals(mTab_list.get(i))) {
                        position = i;
                    }
                }
                if (mainViewpager != null) {
                    mainViewpager.setCurrentItem(position);
                }
        }
        //QQ登录返回信息
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mTencentListener);
            mProgressDialog.dismiss();
        }
        //微博登录返回信息
        if (mWeiboSsonHandler != null) {
            mWeiboSsonHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        //获得searchview实例
        final MenuItem menuItem = menu.findItem(R.id.main_menu_search);
        searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setQueryHint("输入新闻关键词");
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //   menuItem.collapseActionView();
                //按下键盘上的搜索键
                Intent intent = new Intent(MainActivity.this, SearchNewsActivity.class);
                intent.putExtra(Configuration.KEYWORD, query);
                startActivity(intent);
                searchView.setQuery("", false);
                //关闭搜索的编辑框
                searchView.onActionViewCollapsed();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //输入字符时候激活
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.UPDATE_NETWORK:
                    if (messageView.getVisibility() == View.VISIBLE) {
                        messageView.setVisibility(View.GONE);
                    }
                    break;

                case Constant.UPDATE_NONETWORK:
                    messageView.setVisibility(View.VISIBLE);
                    messageText.setText("没有连接网络...");
            }
        }
    };


}
