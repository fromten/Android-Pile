package learn.example.pile.jsonobject;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonObject;

/**
 * Created on 2016/5/7.
 */
public  class BaseJokeData implements Parcelable{
    public final static int TYPE_PNG =1;//静态图片
    public final static int TYPE_TEXT=2;//静态文本
    public final static int TYPE_GIF=3;//动态图片
    private int type;
    private String imgurl;
    private String title;
    private String text;
    private String lasttime;
    private int currentpage;

    //使用JokeDataBuilder去 new BaseJokeData
    public BaseJokeData() {

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getImgUrl() {
        return imgurl;
    }

    public void setImgUrl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLastTime() {
        return lasttime;
    }

    public void setLastTime(String lastTime) {
        this.lasttime = lastTime;
    }

    public int getCurrentPage() {
        return currentpage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentpage = currentPage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
          dest.writeString(title);
          dest.writeString(text);
          dest.writeString(imgurl);
          dest.writeInt(type);
          dest.writeInt(currentpage);
          dest.writeString(lasttime);
    }
    public  static final Parcelable.Creator<BaseJokeData> CREATOR=new Parcelable.Creator<BaseJokeData>()
    {
        @Override
        public BaseJokeData createFromParcel(Parcel source) {
             BaseJokeData data=new BaseJokeData();
             data.setTitle(source.readString());
             data.setText(source.readString());
             data.setImgUrl(source.readString());
             data.setType(source.readInt());
             data.setCurrentPage(source.readInt());
             data.setLastTime(source.readString());
            return data;
        }

        @Override
        public BaseJokeData[] newArray(int size) {
            return new BaseJokeData[size];
        }
    };

    @Override
    public String toString() {
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("type",type);
        jsonObject.addProperty("lasttime",lasttime);
        jsonObject.addProperty("title",title);
        jsonObject.addProperty("text",text);
        jsonObject.addProperty("imgurl",imgurl);
        jsonObject.addProperty("currentpage",currentpage);
        return jsonObject.toString();
    }
}

