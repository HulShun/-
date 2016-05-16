package com.example.newsclient.view.fragment;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newsclient.Model.utils.AppUtil;
import com.example.newsclient.presenter.BasePresenter;
import com.example.newsclient.view.impl.IBaseViewImpl;
import com.example.newsclient.widget.LoadingFrameLayout;

/**
 * Created by Administrator on 2016-04-11.
 */
public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements IBaseViewImpl {
    private T mPresenter;
    public View rootView;

    public LoadingFrameLayout loadingFrameLayoutPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected abstract T initPresenter();

    public T getPresenter() {
        if (mPresenter == null) {
            mPresenter = initPresenter();
        }
        return mPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            mPresenter = this.getPresenter();
            if (mPresenter != null) {
                mPresenter.bindView((BaseFragment) this);
            }

            loadingFrameLayoutPager = new LoadingFrameLayout(this.getActivity(), getLoadingLayouId(), getLayoutId()) {
                @Override
                public void loading() {
                    //先去加载数据
                    initLoading();
                }

                @Override
                protected void initContentViews(View view) {
                    initViews(view);
                }
            };

            loadingFrameLayoutPager.showLoading();

            rootView = loadingFrameLayoutPager;
        } else {
            resume();
        }
        return loadingFrameLayoutPager;
    }

    public LoadingFrameLayout getLoadingView() {
        if (loadingFrameLayoutPager != null) {
            return loadingFrameLayoutPager;
        } else return null;
    }

    protected abstract void initLoading();

    /**
     * 页面恢复之后的操作
     */
    protected void resume() {
    }

    protected abstract void initViews(View view);

    abstract int getLayoutId();

    abstract int getLoadingLayouId();

    public boolean isVisiable() {
        return this.isVisible();
    }

    public boolean checkNetWork() {
        return AppUtil.getInstance().isNetWorkConnected();
    }

    @CallSuper
    @Override
    public void showFaild(String msg) {
        if (getLoadingView().isloading()) {
            getLoadingView().showFailed();
        }
    }

    @Override
    public void showSuccess() {

    }

    @CallSuper
    @Override
    public void showNoNetWork() {
        if (getLoadingView().isloading()) {
            getLoadingView().showNoNetWork();
        }
    }

    @Override
    public void onCompleted() {

    }
}
