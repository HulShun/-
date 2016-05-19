package com.example.newsclient.view.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.example.newsclient.Configuration;
import com.example.newsclient.Model.bean.TencentOpenBean;
import com.example.newsclient.Model.impl.MyWeiboAuthListener;
import com.example.newsclient.Model.impl.TencentBaseUIListenner;
import com.example.newsclient.Model.utils.TencentUtil;
import com.example.newsclient.Model.utils.WeiboUtil;
import com.example.newsclient.R;
import com.example.newsclient.presenter.BasePresenter;
import com.google.gson.Gson;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import butterknife.Bind;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016-05-19.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.login_qq)
    Button loginQq;
    @Bind(R.id.login_weibo)
    Button loginWeiboBtn;


    @Override
    protected int getToolBarId() {
        return 0;
    }

    /*QQ登陆*/
    private Tencent mTencent;
    private IUiListener mTencentListener;
    /*微信登陆*/
    private IWXAPI wechatAPI;
    /*微信登陆时候的状态码*/
    private String wechat_state;

    /*微博登陆*/
    private AuthInfo mWeiBoAuthInfo;
    /*SSO即客户端登陆模式*/
    private SsoHandler mWeiboSsonHandler;

    @Override
    protected void init() {

        loginQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTencent == null) {
                    initQQLogin();
                }
                qqLogin();
            }
        });

        loginWeiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWeiBoAuthInfo == null) {
                    initWeiboLogin();
                }
                mWeiboSsonHandler.authorize(new MyWeiboAuthListener() {
                    @Override
                    protected void doComplete(Oauth2AccessToken oauth2AccessToken) {
                        WeiboUtil.saveToken(LoginActivity.this, oauth2AccessToken);
                    }
                });
            }
        });

    }

    private void initWechatLogin() {
        wechatAPI = WXAPIFactory.createWXAPI(LoginActivity.this, Configuration.WECHAT_APPID, true);
        wechatAPI.registerApp(Configuration.WECHAT_APPID);
    }

    private void initWeiboLogin() {
        mWeiBoAuthInfo = new AuthInfo(LoginActivity.this, Configuration.WEIBO_APPID, Configuration.WEIBO_REDIRECT_URL, "statuses_to_me_read");
        mWeiboSsonHandler = new SsoHandler(LoginActivity.this, mWeiBoAuthInfo);
    }

    private void initQQLogin() {
        //QQ登陆对象实例化
        mTencent = Tencent.createInstance(Configuration.TENCENT_APPID, this.getApplicationContext());
        //QQ登陆回调
        mTencentListener = new TencentBaseUIListenner() {
            @Override
            protected void doComplete(JSONObject values) {
                super.doComplete(values);

                Observable.just(values.toString())
                        .map(new Func1<String, TencentOpenBean>() {
                            @Override
                            public TencentOpenBean call(String s) {
                                Gson gson = new Gson();
                                TencentOpenBean data = gson.fromJson(s, TencentOpenBean.class);
                                return data;
                            }
                        }).subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.immediate())
                        .subscribe(new Action1<TencentOpenBean>() {
                            @Override
                            public void call(TencentOpenBean tencentOpenBean) {
                                TencentUtil.saveToken(LoginActivity.this, tencentOpenBean);
                            }
                        });
            }
        };
    }

    private void qqLogin() {
        TencentOpenBean data = TencentUtil.getToken(LoginActivity.this);
        mTencent.setOpenId(data.getOpenid());
        mTencent.setAccessToken(data.getAccess_token(), String.valueOf(data.getExpires_in()));
        mTencent.login(LoginActivity.this, "get_user_info", mTencentListener);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (wechatAPI != null) {
            wechatAPI.handleIntent(intent, new IWXAPIEventHandler() {
                @Override
                public void onReq(BaseReq baseReq) {

                }

                @Override
                public void onResp(BaseResp baseResp) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mTencentListener);
        }
        if (mWeiboSsonHandler != null) {
            mWeiboSsonHandler.authorizeCallBack(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
