package learn.example.pile.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created on 2016/7/20.
 */
public class Comment  {

    //放入额外需要的数据
    private JsonObject mExtraMsg;


    private List<CommentItem> mComments;
    public Comment() {
    }

    public JsonObject getExtraMsg() {
        return mExtraMsg;
    }

    public void setComments(List<CommentItem> comments) {
        mComments = comments;
    }

    public List<CommentItem> getComments() {
        return mComments;
    }

    public void setExtraMsg(JsonObject extraMsg) {
        mExtraMsg = extraMsg;
    }

    public static class CommentItem implements Parcelable
    {
        private String author;
        private int likeNumber;
        private String time;
        private String usePic;
        private String address;
        private String content;

        public CommentItem(String author, int likeNumber, String time, String usePic, String address, String content) {
            this.author = author;
            this.likeNumber = likeNumber;
            this.time = time;
            this.usePic = usePic;
            this.address = address;
            this.content = content;
        }

        public CommentItem()
        {

        }

        public String getAuthor() {
            return author;
        }

        public int getLikeNumber() {
            return likeNumber;
        }

        public String getTime() {
            return time;
        }

        public String getUsePic() {
            return usePic;
        }

        public String getAddress() {
            return address;
        }

        public String getContent() {
            return content;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setLikeNumber(int likeNumber) {
            this.likeNumber = likeNumber;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setUsePic(String usePic) {
            this.usePic = usePic;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setContent(String content) {
            this.content = content;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.author);
            dest.writeInt(this.likeNumber);
            dest.writeString(this.time);
            dest.writeString(this.usePic);
            dest.writeString(this.address);
            dest.writeString(this.content);
        }

        protected CommentItem(Parcel in) {
            this.author = in.readString();
            this.likeNumber = in.readInt();
            this.time = in.readString();
            this.usePic = in.readString();
            this.address = in.readString();
            this.content = in.readString();
        }

        public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
            @Override
            public CommentItem createFromParcel(Parcel source) {
                return new CommentItem(source);
            }

            @Override
            public CommentItem[] newArray(int size) {
                return new CommentItem[size];
            }
        };
    }

}
