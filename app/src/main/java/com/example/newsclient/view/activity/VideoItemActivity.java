package com.example.newsclient.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.example.newsclient.Model.bean.VideoItemBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.VideoItemPresenter;
import com.example.newsclient.view.impl.IVideoItemViewImpl;
import com.ykcloud.sdk.opentools.player.VODPlayer;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2016-05-11.
 */
public class VideoItemActivity extends BaseActivity<VideoItemPresenter> implements IVideoItemViewImpl {

    @Bind(R.id.videoitem_playerlayout)
    RelativeLayout playlayout;

    WebView mWebView;

    private String title;
    private String id;

    private VODPlayer mPlayer;
    private VideoItemBean mVideoData;

    private String play_url;


    @Override
    protected int getToolBarId() {
        return R.id.videoitem_toorbar;
    }


    @Override
    protected void init() {
        getToolBar().setNavigationIcon(R.drawable.icon_back);
        getToolBar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoItemActivity.this.finish();
            }
        });
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        getSupportActionBar().setTitle(title);
        mPlayer = new VODPlayer(this, true);
        playlayout.addView(mPlayer.getPlayRootLayout());

        initWebView();

        loadData();

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
        mWebView.loadUrl(data.getLink());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.destroyVideo();
            mPlayer = null;
        }
        if (mWebView != null) {
            mWebView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mWebView != null) {
            mWebView.destroy();
        }
        this.finish();
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        mWebView = new WebView(VideoItemActivity.this);

        WebSettings settings = mWebView.getSettings();

        // 支持javascript
        settings.setJavaScriptEnabled(true);

        // 支持localstorage和essionStorage
        settings.setDomStorageEnabled(true);

        // 开启应用缓存
        settings.setAppCacheEnabled(true);
        String cacheDir = this.getApplicationContext()
                .getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(cacheDir);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // 设置缓冲大小
        settings.setAppCacheMaxSize(1024 * 1024 * 10);

        settings.setAllowFileAccess(true);

        settings.setUserAgentString(
                "Mozilla/5.0 (iPhone; CPU iPhone OS 7_1 like Mac OS X) AppleWebKit/537.51.2 (KHTML, like Gecko) Version/7.0 Mobile/11D5145e Safari/9537.53");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

//		        view.loadUrl("javascript:window.js_method.showSource"+
//		                            "(document.getElementsByTagName('video')[0].src);");
//		    	view.loadUrl("javascript:player = new YKU.Player('youkuplayer',{styleid: '0',client_id: 'f9d1d03f2c04474e',vid: 'XOTA4MzAwNzk2',autoplay: true,"
//		    			+ "});</script>");
                String js = "var newscript = document.createElement(\"script\");";
                js += "newscript.src=\"http://player.youku.com/jsapi";
                js += "newscript.type=\"text/javascript\"";
                js += "newscript.onload=function(){player = new YKU.Player('youkuplayer',{styleid: '0',client_id: 'f9d1d03f2c04474e',vid: 'XOTA4MzAwNzk2',autoplay: true,});}";
                js += "document.body.appendChild(newscript);";
                super.onPageFinished(view, url);
                view.loadUrl("javascript:" + js);
            }

        });
        mWebView.addJavascriptInterface(new InJavaScriptLocalObj(), "js_method");
    }

    final class InJavaScriptLocalObj {

        @JavascriptInterface
        public void showSource(String html5url) {
            if (html5url != null) {
                if (html5url.contains("type=flv")) {
                    html5url = html5url.replace("type=flv", "type=mp4");
                } else if (html5url.contains("type=mp4")) {
                } else {
                    html5url = html5url + "&type=mp4";
                }
                Message msg = new Message();
                msg.obj = html5url;//这个就是M3U8文件地址
                Log.i("m3u78", html5url);
//	            handler.sendMessage(msg);
            }

        }
    }

}
