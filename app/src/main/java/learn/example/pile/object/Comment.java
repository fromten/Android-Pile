package learn.example.pile.object;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import learn.example.pile.jsonbean.NetEaseComment;
import learn.example.pile.jsonbean.ZhihuComment;

/**
 * Created on 2016/7/20.
 */
public class Comment implements Parcelable {
    private String author;
    private int likeNumber;
    private String time;
    private String usePic;
    private String address;
    private String content;

    public Comment(String author, int likeNumber, String time, String usePic, String address, String content) {
        this.author = author;
        this.likeNumber = likeNumber;
        this.time = time;
        this.usePic = usePic;
        this.address = address;
        this.content = content;
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

    protected Comment(Parcel in) {
        this.author = in.readString();
        this.likeNumber = in.readInt();
        this.time = in.readString();
        this.usePic = in.readString();
        this.address = in.readString();
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
