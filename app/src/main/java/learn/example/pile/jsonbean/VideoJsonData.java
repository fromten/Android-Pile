package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/5/24.
 */
public class VideoJsonData {

    /**
     * error : false
     * results : [{"_id":"5741a6526776597d979ad009","createdAt":"2016-05-22T20:30:10.811Z","desc":"日本团队用1000多杯拿铁做成了一部令人心动的动画，创意满分，甜蜜高糖！","publishedAt":"2016-05-24T11:56:12.924Z","source":"chrome","type":"休息视频","url":"http://weibo.com/p/2304449a1fb0b126ab01616812e39991c51aa8","used":true,"who":"lxxself"},{"_id":"5741a6346776597d9df10b51","createdAt":"2016-05-22T20:29:40.180Z","desc":"8分钟混剪，看尽电影世界里的哲学","publishedAt":"2016-05-23T10:54:25.890Z","source":"chrome","type":"休息视频","url":"http://www.miaopai.com/show/GzWUGh4N4rGxv3VDyL5KGw__.htm","used":true,"who":"lxxself"},{"_id":"573d3b9c6776591ca2f31b99","createdAt":"2016-05-19T12:05:48.633Z","desc":"Google IO 2016 KeyNote","publishedAt":"2016-05-19T12:09:32.865Z","source":"chrome","type":"休息视频","url":"https://www.youtube.com/watch?v=862r3XS2YB0","used":true,"who":"代码家"},{"_id":"573c877c6776591ca681f8c0","createdAt":"2016-05-18T23:17:16.446Z","desc":"美美美：新加坡JKAI携手美女《太阳的后裔》中文版Always","publishedAt":"2016-05-20T10:05:09.959Z","source":"chrome","type":"休息视频","url":"http://v.youku.com/v_show/id_XMTU3MjUwMDkxMg==.html","used":true,"who":"lxxself"},{"_id":"57374a0c6776591ca2f31b3e","createdAt":"2016-05-14T23:53:48.527Z","desc":"施瓦辛格的励志演讲","publishedAt":"2016-05-17T12:17:17.785Z","source":"chrome","type":"休息视频","url":"http://www.miaopai.com/show/4kU9dsswDerxmthxDsSPWg__.htm","used":true,"who":"LHF"}]
     */

    private boolean error=true;
    /**
     * _id : 5741a6526776597d979ad009
     * createdAt : 2016-05-22T20:30:10.811Z
     * desc : 日本团队用1000多杯拿铁做成了一部令人心动的动画，创意满分，甜蜜高糖！
     * publishedAt : 2016-05-24T11:56:12.924Z
     * source : chrome
     * type : 休息视频
     * url : http://weibo.com/p/2304449a1fb0b126ab01616812e39991c51aa8
     * used : true
     * who : lxxself
     */

    @SerializedName("results")
    private List<VideoItem> videoItemList;

    public boolean isError() {
        return error;
    }

    public List<VideoItem> getVideoItemList() {
        return videoItemList;
    }

    public static class VideoItem implements Parcelable {

        private String createdAt;
        private String desc;
        private String publishedAt;
        @SerializedName("url")
        private String htmlUrl;//源来源Html地址
        private String fileUrl;//视频文件实际地址
        private String imgUrl; //图片Url地址

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getHtmlUrl() {
            return htmlUrl;
        }

        public void setHtmlUrl(String htmlUrl) {
            this.htmlUrl = htmlUrl;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.createdAt);
            dest.writeString(this.desc);
            dest.writeString(this.publishedAt);
            dest.writeString(this.htmlUrl);
            dest.writeString(this.fileUrl);
            dest.writeString(this.imgUrl);
        }

        public VideoItem() {
        }

        protected VideoItem(Parcel in) {
            this.createdAt = in.readString();
            this.desc = in.readString();
            this.publishedAt = in.readString();
            this.htmlUrl = in.readString();
            this.fileUrl = in.readString();
            this.imgUrl = in.readString();
        }

        public static final Parcelable.Creator<VideoItem> CREATOR = new Parcelable.Creator<VideoItem>() {
            @Override
            public VideoItem createFromParcel(Parcel source) {
                return new VideoItem(source);
            }
            @Override
            public VideoItem[] newArray(int size) {
                return new VideoItem[size];
            }
        };
    }
}
