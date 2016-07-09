package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/5/7.
 */
public class NewsJsonData {
    private int errNum;
    private String errMsg;

    @SerializedName("retData")
    private List<NewsItem> newsItemList;
    public static class NewsItem implements Parcelable {
        private String title;
        @SerializedName("url")
        private String newsUrl;
        @SerializedName("abstract")
        private String newsDes;
        @SerializedName("image_url")
        private String imageUrl;

        public String getTitle() {
            return title;
        }

        public String getNewsUrl() {
            return newsUrl;
        }

        public String getNewsDes() {
            return newsDes;
        }

        public String getImageUrl() {
            return imageUrl;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.newsUrl);
            dest.writeString(this.newsDes);
            dest.writeString(this.imageUrl);
        }

        public NewsItem() {
        }

        protected NewsItem(Parcel in) {
            this.title = in.readString();
            this.newsUrl = in.readString();
            this.newsDes = in.readString();
            this.imageUrl = in.readString();
        }

        public static final Parcelable.Creator<NewsItem> CREATOR = new Parcelable.Creator<NewsItem>() {
            @Override
            public NewsItem createFromParcel(Parcel source) {
                return new NewsItem(source);
            }

            @Override
            public NewsItem[] newArray(int size) {
                return new NewsItem[size];
            }
        };
    }

    public int getErrNum() {
        return errNum;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public List<NewsItem> getNewsItemList() {
        return newsItemList;
    }
}
