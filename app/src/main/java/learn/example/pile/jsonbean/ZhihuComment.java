package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created on 2016/7/13.
 */
public class ZhihuComment {


    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }


    public static class CommentsBean implements Parcelable {
        private String author;
        private int id;
        private String content;
        private int likes;
        private int time;
        private String avatar;


        public String getAuthor() {
            return author;
        }

        public int getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public int getLikes() {
            return likes;
        }

        public int getTime() {
            return time;
        }

        public String getAvatar() {
            return avatar;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.author);
            dest.writeInt(this.id);
            dest.writeString(this.content);
            dest.writeInt(this.likes);
            dest.writeInt(this.time);
            dest.writeString(this.avatar);
        }

        public CommentsBean() {
        }

        protected CommentsBean(Parcel in) {
            this.author = in.readString();
            this.id = in.readInt();
            this.content = in.readString();
            this.likes = in.readInt();
            this.time = in.readInt();
            this.avatar = in.readString();
        }

        public static final Parcelable.Creator<CommentsBean> CREATOR = new Parcelable.Creator<CommentsBean>() {
            @Override
            public CommentsBean createFromParcel(Parcel source) {
                return new CommentsBean(source);
            }

            @Override
            public CommentsBean[] newArray(int size) {
                return new CommentsBean[size];
            }
        };
    }
}
