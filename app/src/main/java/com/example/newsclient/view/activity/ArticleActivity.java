package com.example.newsclient.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.newsclient.R;
import com.example.newsclient.presenter.BasePresenter;

import butterknife.Bind;

/**
 * 新闻详情页
 */
public class ArticleActivity extends BaseActivity  {

    public static final int ARTICLE_CODE = 0x000506;

    @Bind(R.id.article_webview)
    WebView articleWebview;
    @Bind(R.id.article_progressbar)
    ProgressBar progressBar;


    private String mArticle_url;
    private String title;
    private String keyWord;

    @Override
    protected int getToolBarId() {
        return R.id.article_toorbar;
    }

    @Override
    protected void init() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // progressBar.setIndeterminate(false);


        Bundle bundle = getIntent().getBundleExtra("article");
        if (bundle == null) {
            return;
        }
        mArticle_url = bundle.getString("url");
        title = bundle.getString("title");
        keyWord = bundle.getString("key");
        getSupportActionBar().setTitle(title);

        initWebView();


    }

    private void initWebView() {
        WebSettings settings = articleWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(this.getExternalCacheDir().getPath());

        //判断网络加载过程
        articleWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);


                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        articleWebview.loadUrl(mArticle_url);
        //阻止系统浏览器或者第三方浏览器启动
        articleWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                    return true;
                }
                //调用内置浏览器
                //  Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //  startActivity(intent);
                //  view.loadUrl(url);
                return false;
            }

        });

        articleWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                // articleWebview.loadUrl(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_article;
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }


    @Override
    public void showSuccess() {

    }

    @Override
    public void showFaild(String msg) {

    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("key", keyWord);
                intent.putExtra("article", bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(ARTICLE_CODE, intent);
                ArticleActivity.this.finish();
                return true;

            case R.id.article_menu_open:
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(mArticle_url));
                startActivity(intent1);
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.article_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
