package com.example.newsclient.Model.bean.image;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImageBean implements Parcelable {
    private String big;
    private String middle;
    private String small;

    public ImageBean() {
    }

    protected ImageBean(Parcel in) {
        big = in.readString();
        middle = in.readString();
        small = in.readString();
    }

    public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
        @Override
        public ImageBean createFromParcel(Parcel in) {
            return new ImageBean(in);
        }

        @Override
        public ImageBean[] newArray(int size) {
            return new ImageBean[size];
        }
    };

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(big);
        dest.writeString(middle);
        dest.writeString(small);
    }
}
