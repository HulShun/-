package com.example.newsclient.Model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016-05-04.
 */
public class ImageJsonBean implements Parcelable {


    private int showapi_res_code;
    private String showapi_res_error;


    private ShowapiResBodyBean showapi_res_body;

    protected ImageJsonBean(Parcel in) {
        showapi_res_code = in.readInt();
        showapi_res_error = in.readString();
    }

    public static final Creator<ImageJsonBean> CREATOR = new Creator<ImageJsonBean>() {
        @Override
        public ImageJsonBean createFromParcel(Parcel in) {
            return new ImageJsonBean(in);
        }

        @Override
        public ImageJsonBean[] newArray(int size) {
            return new ImageJsonBean[size];
        }
    };

    public int getShowapi_res_code() {
        return showapi_res_code;
    }

    public void setShowapi_res_code(int showapi_res_code) {
        this.showapi_res_code = showapi_res_code;
    }

    public String getShowapi_res_error() {
        return showapi_res_error;
    }

    public void setShowapi_res_error(String showapi_res_error) {
        this.showapi_res_error = showapi_res_error;
    }

    public ShowapiResBodyBean getShowapi_res_body() {
        return showapi_res_body;
    }

    public void setShowapi_res_body(ShowapiResBodyBean showapi_res_body) {
        this.showapi_res_body = showapi_res_body;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(showapi_res_code);
        dest.writeString(showapi_res_error);
    }

    public static class ShowapiResBodyBean implements Parcelable {


        private PagebeanBean pagebean;
        private int ret_code;

        protected ShowapiResBodyBean(Parcel in) {
            ret_code = in.readInt();
        }

        public static final Creator<ShowapiResBodyBean> CREATOR = new Creator<ShowapiResBodyBean>() {
            @Override
            public ShowapiResBodyBean createFromParcel(Parcel in) {
                return new ShowapiResBodyBean(in);
            }

            @Override
            public ShowapiResBodyBean[] newArray(int size) {
                return new ShowapiResBodyBean[size];
            }
        };

        public PagebeanBean getPagebean() {
            return pagebean;
        }

        public void setPagebean(PagebeanBean pagebean) {
            this.pagebean = pagebean;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(ret_code);
        }

        public static class PagebeanBean implements Parcelable {
            private int allNum;
            private int allPages;
            private int currentPage;
            private int maxResult;

            private List<ImageContentBean> contentlist;

            protected PagebeanBean(Parcel in) {
                allNum = in.readInt();
                allPages = in.readInt();
                currentPage = in.readInt();
                maxResult = in.readInt();
            }

            public static final Creator<PagebeanBean> CREATOR = new Creator<PagebeanBean>() {
                @Override
                public PagebeanBean createFromParcel(Parcel in) {
                    return new PagebeanBean(in);
                }

                @Override
                public PagebeanBean[] newArray(int size) {
                    return new PagebeanBean[size];
                }
            };

            public int getAllNum() {
                return allNum;
            }

            public void setAllNum(int allNum) {
                this.allNum = allNum;
            }

            public int getAllPages() {
                return allPages;
            }

            public void setAllPages(int allPages) {
                this.allPages = allPages;
            }

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getMaxResult() {
                return maxResult;
            }

            public void setMaxResult(int maxResult) {
                this.maxResult = maxResult;
            }

            public List<ImageContentBean> getContentlist() {
                return contentlist;
            }

            public void setContentlist(List<ImageContentBean> contentlist) {
                this.contentlist = contentlist;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(allNum);
                dest.writeInt(allPages);
                dest.writeInt(currentPage);
                dest.writeInt(maxResult);
            }
        }
    }
}
