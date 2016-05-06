package com.example.newsclient.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.newsclient.Model.bean.ImageJsonBean;
import com.example.newsclient.Model.bean.ImageMainTypeBean;
import com.example.newsclient.Model.bean.ImageTpyeBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageClassifyPresenter;
import com.example.newsclient.view.adapter.ImageClassifyAdapter;
import com.example.newsclient.view.impl.IImageClassifyViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.widget.AutoRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016-05-05.
 */
public class ImageClassifyFragment extends BaseFragment<ImageClassifyPresenter> implements IImageClassifyViewImpl {

    private ImageMainTypeBean mImageType;
    private List<ImageJsonBean> mImageData;
    private ImageClassifyAdapter mAdapter;

    @Bind(R.id.fragment_imageclassify_rc)
    AutoRecyclerView fragmentImageRc;


    @Override
    protected ImageClassifyPresenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new ImageClassifyPresenter();
        }
        return mPresenter;
    }

    @Override
    protected void initLoading() {
        //获取当前页面下所有子view需要的数据
        Bundle bundle = getArguments();
        mImageType = bundle.getParcelable("type");
        loadImageData();

        getLoadingView().setOnBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImageData();
            }
        });
    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);
        initRecyclerView();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        fragmentImageRc.setLayoutManager(gridLayoutManager);

        if (mAdapter == null) {
            mAdapter = new ImageClassifyAdapter(getPresenter());
            mAdapter.setFooterShow(false);
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

            }

            @Override
            protected void pauseLoadImg() {
                if (mAdapter != null) {
                    mAdapter.pauseLoading(true);
                }
            }

            @Override
            protected void resumeLoadImg(int firstPosition, int lastPositon) {
                if (mAdapter != null) {
                    mAdapter.pauseLoading(false);
                    mAdapter.executeTask(firstPosition, lastPositon);
                }
            }
        });
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

    public void loadImageData() {
        //获取这个主分类下面的所有相册
        for (ImageTpyeBean bean : mImageType.getList()) {
            Map<String, String> map = new HashMap<>();
            map.put("type", String.valueOf(bean.getId()));
            map.put("page", String.valueOf(1));
            mPresenter.getImageData(map);
        }

    }

    @Override
    public void onLoadData(ImageJsonBean data) {
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }

        //加载当前分类下所有相册的第一页数据
        if (mImageData == null) {
            mImageData = new ArrayList<>();
        }
        mImageData.add(data);
        //选择每个相册的分类名称和第一张图片作为封面
        ItemBean bean = new ItemBean();
        bean.name = data.getShowapi_res_body().getPagebean().getContentlist().get(0).getTypeName();
        bean.img_url = data.getShowapi_res_body().getPagebean().getContentlist().get(0).getList().get(0).getMiddle();
        mAdapter.addData(bean);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void showSuccess() {

    }


    public static class ItemBean {
        public String name;
        public String img_url;
    }
}
