package com.example.newsclient.Model.bean.image;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImageTypeBean implements Parcelable {

    private int id;
    private String name;

    public ImageTypeBean() {
    }

    protected ImageTypeBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<ImageTypeBean> CREATOR = new Creator<ImageTypeBean>() {
        @Override
        public ImageTypeBean createFromParcel(Parcel in) {
            return new ImageTypeBean(in);
        }

        @Override
        public ImageTypeBean[] newArray(int size) {
            return new ImageTypeBean[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
