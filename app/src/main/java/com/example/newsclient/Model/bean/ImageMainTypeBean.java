package com.example.newsclient.Model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016-05-05.
 */
public class ImageMainTypeBean implements Parcelable {
    private String name;
    /**
     * id : 1001
     * name : 绀句細鏂伴椈
     */

    private List<ImageTpyeBean> list;

    public ImageMainTypeBean() {

    }

    protected ImageMainTypeBean(Parcel in) {
        name = in.readString();
        list = in.createTypedArrayList(ImageTpyeBean.CREATOR);
    }

    public static final Creator<ImageMainTypeBean> CREATOR = new Creator<ImageMainTypeBean>() {
        @Override
        public ImageMainTypeBean createFromParcel(Parcel in) {
            return new ImageMainTypeBean(in);
        }

        @Override
        public ImageMainTypeBean[] newArray(int size) {
            return new ImageMainTypeBean[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ImageTpyeBean> getList() {
        return list;
    }

    public void setList(List<ImageTpyeBean> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(list);
    }
}
