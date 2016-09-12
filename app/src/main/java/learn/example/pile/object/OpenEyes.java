package learn.example.pile.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.jsonbean.OpenEyeVideo;

/**
 * Created on 2016/7/22.
 */
public class OpenEyes {

    /*
         udid= 5cf7bfcf19a84c618fd8d1e41f55518025ce2f94
         vc = 89
         vn= 1.13.1
         deviceModel= Samsung Galaxy S6 - 6.0.0 - API 23 - 1440x2560
    */

    public static final String HOT_URL="http://baobab.wandoujia.com/api/v2/feed?";
    public static final String APP_PARAMS="&udid=5cf7bfcf19a84c618fd8d1e41f55518025ce2f94&vc=126&vn=2.4.1&deviceModel=Samsung%20Galaxy%20S6%20-%206.0.0%20-%20API%2023%20-%201440x2560&first_channel=eyepetizer_web&last_channel=eyepetizer_web&system_version_code=23";
    public static final String APP_PARAMS_OLD="&udid=5cf7bfcf19a84c618fd8d1e41f55518025ce2f94&vc=89&vn=1.13.1&deviceModel=Samsung%20Galaxy%20S6%20-%206.0.0%20-%20API%2023%20-%201440x2560&first_channel=eyepetizer_web&last_channel=eyepetizer_web";
    public static final String CATEGORY_URL="http://baobab.wandoujia.com/api/v3/videos?";


    //视频策略
    public static class Strategy{
        public final static String SHARE_COUNT="shareCount";//按分享总数获得视频列表
        public final static String DATE="date";//按时间获得视频列表
    }

    //分类视频
    public static class Category{
        public static final int DRAMA=12;//剧情
        public static final int ART=2;//创意
        public static final int ADVERTISEMENT=14;//广告
        public static final int RECORD=22;//记录
        public static final int PREVIEW=8;//预告
        public static final int TRIP=6;//旅行
    }

    /**
     *  参数使用 APP_PARAMS+ 视频Id
     *  例如 id=8522,http://baobab.wandoujia.com/api/v1/replies/video?id=8522 + APP_PARAMS
     *  @see #APP_PARAMS
     *
     */
    public static final String COMMENT_URL="http://baobab.wandoujia.com/api/v1/replies/video";


    public static String getHotUrl(int num)
    {
        return HOT_URL+APP_PARAMS_OLD+"&num="+num;
    }

    public static String getNextUrl(String nextUrl)
    {
        return nextUrl+APP_PARAMS;
    }

    public static String getCategoryUrl(int categoryID,String strategy)
    {
        return CATEGORY_URL+"categoryId="+categoryID+"&strategy"+strategy+APP_PARAMS;
    }

    public static class VideoInfo implements Parcelable {
        private String title;
        private String playUrl;
        private String imgUrl;
        private int  duration;
        private int id;

        public VideoInfo(String title, String playUrl, String imgUrl,int duration,int id) {
            this.title = title;
            this.playUrl = playUrl;
            this.imgUrl = imgUrl;
            this.duration=duration;
            this.id=id;
        }

        public int getDuration() {
            return duration;
        }

        public String getTitle() {
            return title;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public int getId() {
            return id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.title);
            dest.writeString(this.playUrl);
            dest.writeString(this.imgUrl);
            dest.writeInt(this.duration);
            dest.writeInt(this.id);
        }

        protected VideoInfo(Parcel in) {
            this.title = in.readString();
            this.playUrl = in.readString();
            this.imgUrl = in.readString();
            this.duration = in.readInt();
            this.id = in.readInt();
        }

        public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
            @Override
            public VideoInfo createFromParcel(Parcel source) {
                return new VideoInfo(source);
            }

            @Override
            public VideoInfo[] newArray(int size) {
                return new VideoInfo[size];
            }
        };
    }


}
