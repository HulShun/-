package com.example.newsclient.view.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.example.newsclient.Model.ModelMode;
import com.example.newsclient.Model.bean.image.ImageContentBean;
import com.example.newsclient.Model.bean.image.ImageJsonBean;
import com.example.newsclient.R;
import com.example.newsclient.presenter.ImageListPresenter;
import com.example.newsclient.view.activity.ImageItemActivity;
import com.example.newsclient.view.activity.ImageListActivity;
import com.example.newsclient.view.adapter.ImagesAdapter;
import com.example.newsclient.view.impl.IImageListViewImpl;
import com.example.newsclient.view.impl.OnItemClickListener;
import com.example.newsclient.view.viewholder.ImagesViewHolder;
import com.example.newsclient.widget.AutoRecyclerView;

import java.io.ByteArrayOutputStream;
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
            //显示底部的加载item
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
                    Drawable drawable = ((ImagesViewHolder) viewHolder).imageView.getDrawable();
                    //将当前显示的图片转成二进制数组，传给图片查看器页面
                 //   byte[] bytes = getBitmapByte(drawable);
                    Intent intent = new Intent(getContext(), ImageItemActivity.class);
                 //   intent.putExtra("middle_img", bytes);
                    intent.putExtra("type", String.valueOf(type));
                    intent.putExtra("itemid", data.getItemId());
                    //计算当前position所在的页数
                    int page = (position / 20) + 1;
                    intent.putExtra("page", String.valueOf(page));


                    startActivity(intent);

                }
            });
            fragmentImageRc.setAdapter(mAdapter);
        }

    }

    private byte[] getBitmapByte(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
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
