package learn.example.pile.object;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.facebook.stetho.common.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import learn.example.pile.jsonbean.NetEaseComment;
import learn.example.pile.jsonbean.ZhihuComment;
import learn.example.pile.util.GsonHelper;
import learn.example.pile.util.TimeUtil;

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

    public static Comment valueOf(ZhihuComment.CommentsBean zhihuCommentItem) {
        String timeStr = TimeUtil.formatYMD(zhihuCommentItem.getTime());
        Comment comment = new Comment
                (zhihuCommentItem.getAuthor(),
                        zhihuCommentItem.getLikes(), timeStr,
                        zhihuCommentItem.getAvatar(), null,
                        zhihuCommentItem.getContent());
        return comment;
    }


    public static Comment valueOf(JsonObject netEaseCommen) {
        String author = GsonHelper.getAsString(netEaseCommen.get("n"), "网友");
        String time = GsonHelper.getAsString(netEaseCommen.get("t"), null);
        String content = GsonHelper.getAsString(netEaseCommen.get("b"), null);
        int like = GsonHelper.getAsInteger(netEaseCommen.get("v"), 0);
        String imgUrl = GsonHelper.getAsString(netEaseCommen.get("timg"), null);
        String address = GsonHelper.getAsString(netEaseCommen.get("f"), null);
        try {
            int index = address.indexOf("&nbsp");
            if (index > 0) {
                address = address.substring(0, index);
            }
        } catch (NullPointerException | IndexOutOfBoundsException e) {
            address = null;
        }

        return new Comment(author, like, time, imgUrl, address, content);
    }

    public static List<Comment> toList(ZhihuComment zhihuComment) {

        List<Comment> list = new ArrayList<>();
        for (ZhihuComment.CommentsBean item : zhihuComment.getComments()) {
            Comment comment = valueOf(item);
            if (comment != null)
                list.add(comment);
        }
        return list;
    }


    public static List<Comment> toList(NetEaseComment netEaseComment) {
        List<Comment> list = new ArrayList<>();
        JsonArray array = netEaseComment.getHotPosts();
        int len = array.size();
        for (int i = 0; i < len; i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            Comment comment = valueOf(object.getAsJsonObject("1"));
            if (comment != null)
                list.add(comment);
        }
        return list;
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
