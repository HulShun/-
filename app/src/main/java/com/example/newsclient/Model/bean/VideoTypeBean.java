package com.example.newsclient.Model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoTypeBean implements Parcelable {

    /**
     * term : Movies
     * label : 鐢靛奖
     * lang : zh_CN
     * genre : [{"term":"drama","label":"鍓ф儏","lang":"zh_CN"},{"term":"comedy","label":"鍠滃墽","lang":"zh_CN"},{"term":"romance","label":"鐖辨儏","lang":"zh_CN"},{"term":"action","label":"鍔ㄤ綔","lang":"zh_CN"},{"term":"thriller","label":"鎯婃倸","lang":"zh_CN"},{"term":"documentary","label":"绾綍鐗�","lang":"zh_CN"},{"term":"crime","label":"鐘姜","lang":"zh_CN"},{"term":"horror","label":"鎭愭��","lang":"zh_CN"},{"term":"animation","label":"鍔ㄧ敾","lang":"zh_CN"},{"term":"fantasy","label":"濂囧够","lang":"zh_CN"},{"term":"adventure","label":"鍐掗櫓","lang":"zh_CN"},{"term":"mystery","label":"鎮枒","lang":"zh_CN"},{"term":"science-fiction","label":"绉戝够","lang":"zh_CN"},{"term":"musical","label":"姝岃垶","lang":"zh_CN"},{"term":"war","label":"鎴樹簤","lang":"zh_CN"},{"term":"western","label":"瑗块儴","lang":"zh_CN"},{"term":"biography","label":"浼犺","lang":"zh_CN"},{"term":"history","label":"鍘嗗彶","lang":"zh_CN"},{"term":"opera","label":"鎴忔洸","lang":"zh_CN"},{"term":"martial","label":"姝︿緺","lang":"zh_CN"},{"term":"children","label":"鍎跨","lang":"zh_CN"},{"term":"short","label":"鐭墖","lang":"zh_CN"},{"term":"police-drama","label":"璀﹀尓","lang":"zh_CN"},{"term":"sports","label":"杩愬姩","lang":"zh_CN"},{"term":"youku-original","label":"浼橀叿鍑哄搧","lang":"zh_CN"}]
     */

    private List<VideoMainTypeBean> categories;

    public VideoTypeBean() {
    }

    protected VideoTypeBean(Parcel in) {
    }

    public static final Creator<VideoTypeBean> CREATOR = new Creator<VideoTypeBean>() {
        @Override
        public VideoTypeBean createFromParcel(Parcel in) {
            return new VideoTypeBean(in);
        }

        @Override
        public VideoTypeBean[] newArray(int size) {
            return new VideoTypeBean[size];
        }
    };

    public List<VideoMainTypeBean> getCategories() {
        return categories;
    }

    public void setCategories(List<VideoMainTypeBean> categories) {
        this.categories = categories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public static class VideoMainTypeBean implements Parcelable {
        private String term;
        private String label;
        private String lang;
        /**
         * term : drama
         * label : 鍓ф儏
         * lang : zh_CN
         */

        private List<VideoSubtitleBean> genre;

        public VideoMainTypeBean() {
        }

        protected VideoMainTypeBean(Parcel in) {
            term = in.readString();
            label = in.readString();
            lang = in.readString();
        }

        public static final Creator<VideoMainTypeBean> CREATOR = new Creator<VideoMainTypeBean>() {
            @Override
            public VideoMainTypeBean createFromParcel(Parcel in) {
                return new VideoMainTypeBean(in);
            }

            @Override
            public VideoMainTypeBean[] newArray(int size) {
                return new VideoMainTypeBean[size];
            }
        };

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public List<VideoSubtitleBean> getGenre() {
            return genre;
        }

        public void setGenre(List<VideoSubtitleBean> genre) {
            this.genre = genre;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(term);
            dest.writeString(label);
            dest.writeString(lang);
        }

        public static class VideoSubtitleBean implements Parcelable {
            private String term;
            private String label;
            private String lang;

            public VideoSubtitleBean() {
            }

            protected VideoSubtitleBean(Parcel in) {
                term = in.readString();
                label = in.readString();
                lang = in.readString();
            }

            public static final Creator<VideoSubtitleBean> CREATOR = new Creator<VideoSubtitleBean>() {
                @Override
                public VideoSubtitleBean createFromParcel(Parcel in) {
                    return new VideoSubtitleBean(in);
                }

                @Override
                public VideoSubtitleBean[] newArray(int size) {
                    return new VideoSubtitleBean[size];
                }
            };

            public String getTerm() {
                return term;
            }

            public void setTerm(String term) {
                this.term = term;
            }

            public String getLabel() {
                return label;
            }

            public void setLabel(String label) {
                this.label = label;
            }

            public String getLang() {
                return lang;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(term);
                dest.writeString(label);
                dest.writeString(lang);
            }
        }
    }
}
