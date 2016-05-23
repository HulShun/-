package com.example.newsclient.Model.bean.image;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImageContentBean implements Parcelable {

    private String ct;
    private String itemId;
    private String title;
    private int type;
    private String typeName;
    /**
     * big : http://image.tianjimedia.com/uploadImages/2015/163/07/1H6Z683M828D.jpg
     * middle : http://image.tianjimedia.com/uploadImages/2015/163/07/1H6Z683M828D_680x500.jpg
     * small : http://image.tianjimedia.com/uploadImages/2015/163/07/1H6Z683M828D_113.jpg
     */

    private List<ImageBean> list;

    public ImageContentBean() {
    }

    protected ImageContentBean(Parcel in) {
        ct = in.readString();
        itemId = in.readString();
        title = in.readString();
        type = in.readInt();
        typeName = in.readString();
    }

    public static final Creator<ImageContentBean> CREATOR = new Creator<ImageContentBean>() {
        @Override
        public ImageContentBean createFromParcel(Parcel in) {
            return new ImageContentBean(in);
        }

        @Override
        public ImageContentBean[] newArray(int size) {
            return new ImageContentBean[size];
        }
    };

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public List<ImageBean> getList() {
        return list;
    }

    public void setList(List<ImageBean> list) {
        this.list = list;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ct);
        dest.writeString(itemId);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeString(typeName);
    }
}
