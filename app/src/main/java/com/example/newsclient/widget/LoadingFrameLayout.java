package com.example.newsclient.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newsclient.R;

/**
 * 加载页面
 * Created by Administrator on 2016-05-05.
 */
public abstract class LoadingFrameLayout extends FrameLayout {
    private View view;
    private Context context;
    private ImageView imageView;
    private Button button;
    private TextView textView;
    private int layoutId;

    private boolean isloading;
    private Bitmap loaingBiemap;

    public LoadingFrameLayout(Context context, int loadingLayoutId, int layoutId) {
        super(context);
        this.context = context;
        this.layoutId = layoutId;
        view = LayoutInflater.from(context).inflate(loadingLayoutId, null, false);
        imageView = (ImageView) view.findViewById(R.id.loading_img);
        button = (Button) view.findViewById(R.id.loading_btm);
        textView = (TextView) view.findViewById(R.id.loading_tv);

        addView(view);
    }

    public LoadingFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void showLoading() {
        isloading = true;
       /* if (loaingBiemap == null) {
            loaingBiemap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_nonet);
        }*/
        textView.setVisibility(VISIBLE);
        textView.setText("正在加载中...");
        button.setVisibility(INVISIBLE);
        // imageView.setImageBitmap(loaingBiemap);
        loading();
    }

    public abstract void loading();

    public void showNoNetWork() {
        textView.setVisibility(VISIBLE);
        textView.setText("网络连接失败...");
        button.setVisibility(VISIBLE);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_nonet);
        imageView.setImageBitmap(bitmap);
    }

    public void showFailed() {
        textView.setVisibility(VISIBLE);
        button.setVisibility(VISIBLE);
        textView.setText("加载失败");
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_failed);
        imageView.setImageBitmap(bitmap);
    }

    public void showSuccess() {
        isloading = false;
        imageView.setVisibility(GONE);
        textView.setVisibility(GONE);
        button.setVisibility(GONE);
        //加载正常的布局
        View view = LayoutInflater.from(context).inflate(layoutId, null, false);
        addView(view);
        //初始化布局的东西
        initContentViews(view);
    }

    public boolean isloading() {
        return isloading;
    }


    protected abstract void initContentViews(View view);

    public void setOnBtnClickListener(View.OnClickListener l) {
        button.setOnClickListener(l);
    }

    public void setText(String s) {
        textView.setText(s);
    }
}
