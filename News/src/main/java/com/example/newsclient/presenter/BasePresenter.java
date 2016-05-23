package com.example.newsclient.presenter;

import com.example.newsclient.Model.impl.BaseModelImpl;
import com.example.newsclient.view.impl.IBaseViewImpl;

import java.util.Map;

/**
 * Created by Administrator on 2016-04-11.
 */
public abstract class BasePresenter<V extends IBaseViewImpl, M extends BaseModelImpl> {
    private V mView;
    private M mModel;

    public void bindView(V view) {
        mView = view;
    }

    public void unBindView() {
        if (mView != null) {
            mView = null;
        }
    }

    public V getView() {
        return mView;
    }

    public M getModel() {
        if (mModel == null) {
            mModel = instanceModel();
        }
        return mModel;
    }

    protected abstract M instanceModel();



}
