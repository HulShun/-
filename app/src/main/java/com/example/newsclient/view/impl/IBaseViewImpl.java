package com.example.newsclient.view.impl;

/**
 * Created by Administrator on 2016-04-11.
 */
public interface IBaseViewImpl {
    void showSuccess();

    void showFaild(String msg);

    void showNoNetWork();

    boolean checkNetWork();

    boolean isVisiable();
}
