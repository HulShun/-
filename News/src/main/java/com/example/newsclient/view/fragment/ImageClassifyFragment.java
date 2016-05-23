package com.example.newsclient.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageMainTypeBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageClassifyPresenter;
import com.example.newsclient.view.activity.ImageListActivity;
import com.example.newsclient.view.adapter.ImageClassifyAdapter;
import com.example.newsclient.view.impl.IImageClassifyViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.widget.AutoRecyclerView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-05.
 */
public class ImageClassifyFragment extends BaseFragment<ImageClassifyPresenter> implements IImageClassifyViewImpl {

    private ImageMainTypeBean mImageType;
    private ImageClassifyAdapter mAdapter;
    int cout;
    @Bind(R.id.fragment_imageclassify_rc)
    AutoRecyclerView fragmentImageRc;
    @Bind(R.id.imageclassify_refresh)
    SwipeRefreshLayout refreshLayout;


    @Override
    protected ImageClassifyPresenter initPresenter() {
        return new ImageClassifyPresenter();
    }


    @Override
    protected void initLoading() {
        //获取当前页面下所有子view需要的数据
        Bundle bundle = getArguments();
        mImageType = bundle.getParcelable("type");
        loadImageData(ModelMode.LOCAL);

        getLoadingView().setOnBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLoadingView().showLoading();
            }
        });
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        initRecyclerView();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mAdapter.getData() != null) {
                    mAdapter.clearData();
                }
                //重置计数
                cout = 0;
                loadImageData(ModelMode.INTERNET);
            }
        });
    }

    private void initRecyclerView() {
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        fragmentImageRc.setLayoutManager(gridLayoutManager);
        if (mAdapter == null) {
            mAdapter = new ImageClassifyAdapter(getPresenter());
            mAdapter.setFooterShow(false);
            mAdapter.setOnItemClickListenner(new OnItemClickListener() {
                @Override
                public void onClick(RecyclerView.ViewHolder viewHolder, int position) {
                    //跳转页面
                    int type;
                    ImageJsonBean data = (ImageJsonBean) viewHolder.itemView.getTag();
                    type = data.getShowapi_res_body().getPagebean().getContentlist().get(0).getType();
                    Intent intent = new Intent(getContext(), ImageListActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("title", data.getShowapi_res_body().getPagebean().getContentlist().get(0).getTypeName());
                    startActivity(intent);
                }
            });
            fragmentImageRc.setAdapter(mAdapter);
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void loadImageData(int mode) {
        if (refreshLayout != null) {
            refreshLayout.setRefreshing(false);
        }
        //获取这个主分类下面的所有相册
        getPresenter().getImageData(mode, mImageType.getList());
    }


    @Override
    public void onLoadData(ImageJsonBean data) {
        cout++;
        //如果本地获取的这个数据为空，或者网络获取的相册集为空，就不添加数据到list中
        if (data == null || data.getShowapi_res_body().getPagebean().getContentlist().isEmpty()) {
            //如果当前加载的相册已经是这个type下面的最后一个相册了
            if (cout >= mImageType.getList().size()) {
                //网络去获取这个图片分类下的所有相册
                getPresenter().getImageData(ModelMode.INTERNET, mImageType.getList());
            }
            return;
        }
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }
        mAdapter.addData(data);
    }

    @Override
    public void onCompleted() {

    }


}
