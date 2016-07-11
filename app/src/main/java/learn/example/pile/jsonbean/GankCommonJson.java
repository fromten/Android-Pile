package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created on 2016/6/3.
 */
public class GankCommonJson {

    /**
     * error : false
     * results : [{"_id":"57501d40421aa95655e4db8e","createdAt":"2016-06-02T19:49:20.594Z","desc":"Android 小品：神奇的画笔之PorterDuff","publishedAt":"2016-06-03T11:42:44.370Z","source":"web","type":"Android","url":"http://www.println.net/post/Android-PorterDuff","used":true,"who":"潇涧"},{"_id":"57501662421aa9565763b40d","createdAt":"2016-06-02T19:20:02.840Z","desc":"深入浅出 Retrofit","publishedAt":"2016-06-03T11:42:44.370Z","source":"web","type":"Android","url":"http://bugly.qq.com/bbs/forum.php?mod=viewthread&tid=1117#rd","used":true,"who":"潇涧"},{"_id":"574fc621421aa95655e4db80","createdAt":"2016-06-02T13:37:37.58Z","desc":"从Dagger2基础到Google官方架构MVP+Dagger2架构详解","publishedAt":"2016-06-03T11:42:44.370Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/01d3c014b0b1","used":true,"who":"Anthony"},{"_id":"574f9c87421aa910abe2bfba","createdAt":"2016-06-02T10:40:07.670Z","desc":"BubbleView是带箭头的气泡控件/容器类","publishedAt":"2016-06-02T11:30:26.566Z","source":"chrome","type":"Android","url":"https://github.com/cpiz/BubbleView","used":true,"who":"大熊"},{"_id":"574f7b3b421aa910b3910ae6","createdAt":"2016-06-02T08:18:03.832Z","desc":"Wow, Android View Animation!","publishedAt":"2016-06-03T11:42:44.370Z","source":"chrome","type":"Android","url":"https://github.com/hujiaweibujidao/wava","used":true,"who":"沙发还是我"},{"_id":"574e5f66421aa910b3910ada","createdAt":"2016-06-01T12:07:02.872Z","desc":"An Android app to demonstrate react-native-material-design","publishedAt":"2016-06-02T11:30:26.566Z","source":"chrome","type":"Android","url":"https://github.com/react-native-material-design/demo-app","used":true,"who":"wuzheng"},{"_id":"574e5941421aa910b7ff04fa","createdAt":"2016-06-01T11:40:49.727Z","desc":"一个集Gank.Io，Rxjava示例，操作符,MD控件使用,各种好玩Ap示例的学习App。","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"https://github.com/HotBitmapGG/StudyProject","used":true,"who":"HotbitmapGG"},{"_id":"574e5438421aa910b3910ad8","createdAt":"2016-06-01T11:19:20.520Z","desc":"JSON解析的成长史\u2014\u2014原来还可以这么简单","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"http://www.jianshu.com/p/cbc1aa0c7661","used":true,"who":"于连林"},{"_id":"574e52b1421aa910a742e78d","createdAt":"2016-06-01T11:12:49.773Z","desc":"BaseAdapter基本使用","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"http://mafei.site/2015/11/22/BaseAdapter%E4%BD%BF%E7%94%A8%E4%B9%8B%E9%80%97%E6%AF%94%E5%BC%8F%E3%80%81%E6%99%AE%E9%80%9A%E5%BC%8F%E5%92%8C%E6%96%87%E8%89%BA%E5%BC%8F/","used":true,"who":"马飞"},{"_id":"574e44d9421aa910b7ff04f2","createdAt":"2016-06-01T10:13:45.597Z","desc":"Android开发书籍推荐：从入门到精通系列学习路线书籍介绍","publishedAt":"2016-06-01T12:01:44.959Z","source":"web","type":"Android","url":"http://diycode.cc/wiki/androidbook","used":true,"who":null}]
     */

    private boolean error;
    /**
     * _id : 57501d40421aa95655e4db8e
     * createdAt : 2016-06-02T19:49:20.594Z
     * desc : Android 小品：神奇的画笔之PorterDuff
     * publishedAt : 2016-06-03T11:42:44.370Z
     * source : web
     * type : Android
     * url : http://www.println.net/post/Android-PorterDuff
     * used : true
     * who : 潇涧
     */

    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }


    public List<ResultsBean> getResults() {
        return results;
    }

    public static class ResultsBean implements Parcelable {
        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public String getSource() {
            return source;
        }

        public String getType() {
            return type;
        }

        public String getUrl() {
            return url;
        }

        public boolean isUsed() {
            return used;
        }

        public String getWho() {
            return who;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this._id);
            dest.writeString(this.createdAt);
            dest.writeString(this.desc);
            dest.writeString(this.publishedAt);
            dest.writeString(this.source);
            dest.writeString(this.type);
            dest.writeString(this.url);
            dest.writeByte(this.used ? (byte) 1 : (byte) 0);
            dest.writeString(this.who);
        }

        public ResultsBean() {
        }

        protected ResultsBean(Parcel in) {
            this._id = in.readString();
            this.createdAt = in.readString();
            this.desc = in.readString();
            this.publishedAt = in.readString();
            this.source = in.readString();
            this.type = in.readString();
            this.url = in.readString();
            this.used = in.readByte() != 0;
            this.who = in.readString();
        }

        public static final Parcelable.Creator<ResultsBean> CREATOR = new Parcelable.Creator<ResultsBean>() {
            @Override
            public ResultsBean createFromParcel(Parcel source) {
                return new ResultsBean(source);
            }

            @Override
            public ResultsBean[] newArray(int size) {
                return new ResultsBean[size];
            }
        };
    }
}
