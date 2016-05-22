package com.example.newsclient.view.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.image.ImageContentBean;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.Model.bean.image.ImageTypeBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageListPresenter;
import com.example.newsclient.view.activity.ImageItemActivity;
import com.example.newsclient.view.activity.ImageListActivity;
import com.example.newsclient.view.adapter.ImagesAdapter;
import com.example.newsclient.view.impl.IImageListViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.widget.AutoRecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 二级图片分类下相册页面
 * Created by Administrator on 2016-05-04.
 */
public class ImagesFramgent extends BaseFragment<ImageListPresenter> implements IImageListViewImpl {


    @Bind(R.id.fragment_image_rc)
    AutoRecyclerView fragmentImageRc;
    @Bind(R.id.fragment_image_refresh)
    SwipeRefreshLayout fragmentImageRefresh;

    private ImageTypeBean mImageType;

    private int type;
    private int nowpage;
    private ImagesAdapter mAdapter;

    @Override
    protected ImageListPresenter initPresenter() {
        return new ImageListPresenter();
    }


    @Override
    protected void initLoading() {
        type = ((ImageListActivity) getActivity()).getType();
        nowpage = 1;
        getDatas(nowpage);

    }

    private void getDatas(int page) {
        Map<String, Integer> map = new HashMap<>();
        map.put("type", type);
        map.put("page", page);
        getPresenter().getImageDatas(ModelMode.INTERNET, map);
    }

    @Override
    protected void resume() {

    }

    @Override
    protected void initViews(View view) {
        ButterKnife.bind(this, view);

        initRecyclerView();
        //下拉刷新
        fragmentImageRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nowpage = 1;
                getDatas(nowpage);
            }
        });

        getLoadingView().setOnBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nowpage = 1;
                getDatas(nowpage);
            }
        });
    }


    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        fragmentImageRc.setLayoutManager(layoutManager);
        fragmentImageRc.setItemAnimator(new DefaultItemAnimator());


        fragmentImageRc.addOnScrollListener(new AutoRecyclerView.AutoLoadMoreListener() {
            @Override
            protected void loadMore() {
                mAdapter.showFooterLoading();
                nowpage++;
                getDatas(nowpage);
            }
        });
        if (mAdapter == null) {
            mAdapter = new ImagesAdapter();
            mAdapter.setFooterShow(true);
            mAdapter.setOnFooterListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nowpage++;
                    getDatas(nowpage);
                }
            });

            mAdapter.setOnItemClickListenner(new OnItemClickListener() {
                @Override
                public void onClick(RecyclerView.ViewHolder viewHolder, int position) {
                    //跳转页面
                    ImageContentBean data = (ImageContentBean) viewHolder.itemView.getTag();

                    Intent intent = new Intent(getContext(), ImageItemActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("page", nowpage);
                    intent.putExtra("itemid", data.getItemId());
                    startActivity(intent);

                }
            });
            fragmentImageRc.setAdapter(mAdapter);
        }

    }


    @Override
    int getLayoutId() {
        return R.layout.fragment_images;
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
        List<ImageContentBean> imageConents = data.getShowapi_res_body().getPagebean().getContentlist();
        if (imageConents == null || imageConents.isEmpty()) {
            getLoadingView().showFailed();
        }
        if (getLoadingView().isloading()) {
            getLoadingView().showSuccess();
        }
        //清空原来的数据
        mAdapter.clearData();
        mAdapter.addData(imageConents);
    }

    @Override
    public void onLoadMore(ImageJsonBean data) {
        mAdapter.addData(data.getShowapi_res_body().getPagebean().getContentlist());
    }

    @Override
    public void onCompleted() {
        if (fragmentImageRc != null) {
            fragmentImageRc.loadMoreCompleted();
        }

        if (fragmentImageRefresh != null) {
            fragmentImageRefresh.setRefreshing(false);

        }
    }

    @Override
    public void showNoNetWork() {
        super.showNoNetWork();
        mAdapter.showFooterBtn();
    }


}
