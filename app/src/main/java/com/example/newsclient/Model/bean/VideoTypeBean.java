package com.example.newsclient.Model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016-05-09.
 */
public class VideoTypeBean implements Parcelable {


    /**
     * id : 85
     * term : Variety
     * label : 缁艰壓
     * lang : zh_CN
     * genres : [{"term":"crosstalk","label":"鐩稿０","lang":"zh_CN"},{"term":"sketch","label":"灏忓搧","lang":"zh_CN"}]
     */

    private List<VideoCategoriesBean> categories;

    public VideoTypeBean() {
    }

    protected VideoTypeBean(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
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

    public List<VideoCategoriesBean> getCategories() {
        return categories;
    }

    public void setCategories(List<VideoCategoriesBean> categories) {
        this.categories = categories;
    }

    public static class VideoCategoriesBean implements Parcelable {
        private int id;
        private String term;
        private String label;
        private String lang;
        /**
         * term : crosstalk
         * label : 鐩稿０
         * lang : zh_CN
         */

        private List<VideoGenresBean> genres;

        public VideoCategoriesBean() {
        }

        protected VideoCategoriesBean(Parcel in) {
            id = in.readInt();
            term = in.readString();
            label = in.readString();
            lang = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(term);
            dest.writeString(label);
            dest.writeString(lang);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<VideoCategoriesBean> CREATOR = new Creator<VideoCategoriesBean>() {
            @Override
            public VideoCategoriesBean createFromParcel(Parcel in) {
                return new VideoCategoriesBean(in);
            }

            @Override
            public VideoCategoriesBean[] newArray(int size) {
                return new VideoCategoriesBean[size];
            }
        };

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

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

        public List<VideoGenresBean> getGenres() {
            return genres;
        }

        public void setGenres(List<VideoGenresBean> genres) {
            this.genres = genres;
        }

        public static class VideoGenresBean implements Parcelable {
            private String term;
            private String label;
            private String lang;

            public VideoGenresBean() {
            }

            protected VideoGenresBean(Parcel in) {
                term = in.readString();
                label = in.readString();
                lang = in.readString();
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(term);
                dest.writeString(label);
                dest.writeString(lang);
            }

            @Override
            public int describeContents() {
                return 0;
            }

            public static final Creator<VideoGenresBean> CREATOR = new Creator<VideoGenresBean>() {
                @Override
                public VideoGenresBean createFromParcel(Parcel in) {
                    return new VideoGenresBean(in);
                }

                @Override
                public VideoGenresBean[] newArray(int size) {
                    return new VideoGenresBean[size];
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
        }
    }
}
