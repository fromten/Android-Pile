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
        private String userName;
        private int likeNumber;
        private String commentTime;
        private String avatar;
        private String address;
        private String content;

        public CommentItem(String userName, int likeNumber, String commentTime, String avatar, String address, String content) {
            this.userName = userName;
            this.likeNumber = likeNumber;
            this.commentTime = commentTime;
            this.avatar = avatar;
            this.address = address;
            this.content = content;
        }

        public CommentItem()
        {

        }

        public String getUserName() {
            return userName;
        }

        public int getLikeNumber() {
            return likeNumber;
        }

        public String getCommentTime() {
            return commentTime;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getAddress() {
            return address;
        }

        public String getContent() {
            return content;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setLikeNumber(int likeNumber) {
            this.likeNumber = likeNumber;
        }

        public void setCommentTime(String commentTime) {
            this.commentTime = commentTime;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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
            dest.writeString(this.userName);
            dest.writeInt(this.likeNumber);
            dest.writeString(this.commentTime);
            dest.writeString(this.avatar);
            dest.writeString(this.address);
            dest.writeString(this.content);
        }

        protected CommentItem(Parcel in) {
            this.userName = in.readString();
            this.likeNumber = in.readInt();
            this.commentTime = in.readString();
            this.avatar = in.readString();
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
