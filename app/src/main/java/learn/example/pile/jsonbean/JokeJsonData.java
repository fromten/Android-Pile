package learn.example.pile.jsonbean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/5/5.
 */
public class JokeJsonData {

    @SerializedName("showapi_res_code")
    private int resCode=-1;
    @SerializedName("showapi_res_error")
    private String resErrorMsg;

    @SerializedName("showapi_res_body")
    private JokeResBody resBody;

    public static class JokeResBody
    {

        private int allNum;
        private int allPages;

        @SerializedName("contentlist")
        private List<JokeItem> jokeContentList;
        private int currentPage;
        private int maxResult;
        public static class JokeItem implements Parcelable{
             @SerializedName("ct")
             private String createTime;
            private String text;
            private String title;
            private String img;
            private int type;

            public String getCreateTime() {
                return createTime;
            }

            public String getText() {
                return text;
            }

            public String getTitle() {
                return title;
            }

            public String getImg() {
                return img;
            }

            public int getType() {
                return type;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                   dest.writeString(getCreateTime());
                   dest.writeString(getImg());
                   dest.writeString(getText());
                   dest.writeString(getTitle());
                   dest.writeInt(getType());
            }
            public static final Creator<JokeItem> CREATOR=new Creator<JokeItem>() {
                @Override
                public JokeItem createFromParcel(Parcel source) {
                    JokeItem jokeItem=new JokeItem();
                    jokeItem.setCreateTime(source.readString());
                    jokeItem.setImg(source.readString());
                    jokeItem.setText(source.readString());
                    jokeItem.setTitle(source.readString());
                    jokeItem.setType(source.readInt());
                    return jokeItem;
                }

                @Override
                public JokeItem[] newArray(int size) {
                    return new JokeItem[size];
                }
            };

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public void setText(String text) {
                this.text = text;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public void setType(int type) {
                this.type = type;
            }

            @Override
            public String toString() {
                JsonObject o=new JsonObject();
                o.addProperty("ct",getCreateTime());
                o.addProperty("text",getText());
                o.addProperty("title",getTitle());
                o.addProperty("img",getImg());
                o.addProperty("type",getType());
                return o.toString();
            }
        }

        public int getAllNum() {
            return allNum;
        }

        public int getAllPages() {
            return allPages;
        }

        public List<JokeItem> getJokeContentList() {
            return jokeContentList;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public int getMaxResult() {
            return maxResult;
        }
    }

    public int getResCode() {
        return resCode;
    }

    public String getResErrorMsg() {
        return resErrorMsg;
    }

    public JokeResBody getResBody() {
        return resBody;
    }
}
