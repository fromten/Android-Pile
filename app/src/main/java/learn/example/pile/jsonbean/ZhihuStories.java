package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/7/9.
 */
public class ZhihuStories {


    private String date;

    @SerializedName(value = "stories",alternate = "top_stories")
    private List<StoriesBean> stories;


    public String getDate() {
        return date;
    }


    public List<StoriesBean> getStories() {
        return stories;
    }


    public static class StoriesBean implements Parcelable {
        private int type;
        private int id;
        @SerializedName("ga_prefix")
        private String gaPrefix;
        private String title;
        private String[] images;
        private String image;
        public int getType() {
            return type;
        }

        public int getId() {
            return id;
        }

        public String getGaPrefix() {
            return gaPrefix;
        }

        public String getTitle() {
            return title;
        }

        public String[] getImages() {
            return images;
        }

        public String getImage() {
            return image;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
            dest.writeInt(this.id);
            dest.writeString(this.gaPrefix);
            dest.writeString(this.title);
            dest.writeStringArray(this.images);
            dest.writeString(this.image);
        }

        public StoriesBean() {
        }

        protected StoriesBean(Parcel in) {
            this.type = in.readInt();
            this.id = in.readInt();
            this.gaPrefix = in.readString();
            this.title = in.readString();
            this.images = in.createStringArray();
            this.image = in.readString();
        }

        public static final Parcelable.Creator<StoriesBean> CREATOR = new Parcelable.Creator<StoriesBean>() {
            @Override
            public StoriesBean createFromParcel(Parcel source) {
                return new StoriesBean(source);
            }

            @Override
            public StoriesBean[] newArray(int size) {
                return new StoriesBean[size];
            }
        };
    }

}
