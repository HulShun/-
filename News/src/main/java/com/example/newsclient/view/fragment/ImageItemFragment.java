package com.example.newsclient.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.example.newsclient.R;
import com.example.newsclient.presenter.BasePresenter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Administrator on 2016/5/22.
 */
public class ImageItemFragment extends BaseFragment {
    private PhotoView imageView;
    private String img_url;
    private String middle_url;
    private byte[] imgBytes;
    PhotoViewAttacher mAttacher;

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }


    @Override
    protected void initLoading() {
        Bundle bundle = getArguments();
        img_url = bundle.getString("big_url");
        middle_url = bundle.getString("middle_url");
        //图片列表中已经加载了的那张图片的二进制数组
        //  imgBytes = bundle.getByteArray("middle_img");

        getLoadingView().showSuccess();
    }

    @Override
    protected void initViews(View view) {
        imageView = (PhotoView) view.findViewById(R.id.fragmentitem_iv);

        mAttacher = new PhotoViewAttacher(imageView);
        //先去加载已经缓存过的图片显示出来
      /*  if (imgBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.length);
            imageView.setImageBitmap(bitmap);
        }*/
        getBigBitmap(R.drawable.ic_loading);

    }

    private void getBigBitmap(int resID) {
        if (resID == 0) {
            Picasso.with(getContext())
                    .load(img_url)
                    //加载大图，不做内存缓存：放弃在内存中查找，放弃缓存到内存
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mAttacher.update();
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            Picasso.with(getContext())
                    .load(img_url)
                    .placeholder(resID)
                    .error(resID)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            mAttacher.update();
                        }

                        @Override
                        public void onError() {

                        }
                    });
        }


    }

    @Override
    int getLayoutId() {
        return R.layout.fragment_imageitem;
    }

    @Override
    int getLoadingLayouId() {
        return R.layout.loadinglayout;
    }


}
