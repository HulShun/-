package com.example.newsclient.view.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.image.ImageContentBean;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageTypeBean;
import com.example.newsclient.Model.utils.AppUtil;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageListPresenter;
import com.example.newsclient.view.adapter.ImagesAdapter;
import com.example.newsclient.view.impl.IImageListViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.widget.AutoRecyclerView;
import com.example.newsclient.widget.GridLayoutItemDecoration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 一级图片分类下相册页面
 * Created by Administrator on 2016-05-04.
 */
public class ImagesFramgent extends BaseFragment<ImageListPresenter> implements IImageListViewImpl {


    @Bind(R.id.fragment_image_rc)
    AutoRecyclerView fragmentImageRc;
    @Bind(R.id.fragment_image_refresh)
    SwipeRefreshLayout fragmentImageRefresh;

    private boolean isloadingMore;

    private ImageTypeBean mImageType;
    private int nowPage;

    private ImagesAdapter mAdapter;

    @Override
    protected ImageListPresenter initPresenter() {
        return new ImageListPresenter();
    }


    @Override
    protected void initLoading() {

    }

    @Override
    protected void resume() {

    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        mImageType = bundle.getParcelable("type");
        update();
        initRecyclerView();
        //下拉刷新
        fragmentImageRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update();
            }
        });
    }


    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        fragmentImageRc.setLayoutManager(gridLayoutManager);
        fragmentImageRc.addItemDecoration(new GridLayoutItemDecoration(getContext()));
        fragmentImageRc.setItemAnimator(new DefaultItemAnimator());
        if (mAdapter == null) {
            mAdapter = new ImagesAdapter(getPresenter());
            mAdapter.setFooterShow(true);

            mAdapter.setOnFooterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isloadingMore = true;
                    getMore();
                }
            });

            mAdapter.setOnItemClickListenner(new OnItemClickListener() {
                @Override
                public void onClick(RecyclerView.ViewHolder viewHolder, int position) {
                    //跳转页面
                }
            });
            fragmentImageRc.setAdapter(mAdapter);
        }

        fragmentImageRc.addOnScrollListener(new AutoRecyclerView.AutoLoadMoreListener() {
            @Override
            protected void loadMore() {
                mAdapter.showFooterLoading();
                getMore();
            }
        });
    }

    private void getMore() {
        Map<String, String> params = new HashMap<>();
        params.put("type", String.valueOf(mImageType.getId()));
        nowPage += 1;
        params.put("page", String.valueOf(nowPage));

        getPresenter().getImageDatas(ModelMode.REFRESH, params);
    }

    private void update() {
        Map<String, String> params = new HashMap<>();
        params.put("type", String.valueOf(mImageType.getId()));
        nowPage = 1;
        params.put("page", String.valueOf(nowPage));

        getPresenter().getImageDatas(ModelMode.REFRESH, params);
    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_imageclassify;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }

    @Override
    public void showSuccess() {

    }

    @Override
    public void showFaild(String msg) {
        super.showFaild(msg);
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefreshed(ImageJsonBean data) {
        //清空原来的数据
        mAdapter.clearData();
        List<ImageContentBean> imageConents = data.getShowapi_res_body().getPagebean().getContentlist();
        mAdapter.addData(imageConents);
    }

    @Override
    public void onLoadMore(ImageJsonBean data) {
        isloadingMore = false;
        mAdapter.addData(data.getShowapi_res_body().getPagebean().getContentlist());
    }

    @Override
    public void onCompleted() {
        if (fragmentImageRefresh != null) {
            fragmentImageRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showNoNetWork() {
        super.showNoNetWork();
        mAdapter.showFooterBtn();
    }

    @Override
    public boolean checkNetWork() {
        return AppUtil.getInstance().isNetWorkConnected();
    }


}
