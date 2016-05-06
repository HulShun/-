package com.example.newsclient.Model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImageTpyeBean implements Parcelable {

    private int id;
    private String name;

    protected ImageTpyeBean(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<ImageTpyeBean> CREATOR = new Creator<ImageTpyeBean>() {
        @Override
        public ImageTpyeBean createFromParcel(Parcel in) {
            return new ImageTpyeBean(in);
        }

        @Override
        public ImageTpyeBean[] newArray(int size) {
            return new ImageTpyeBean[size];
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
