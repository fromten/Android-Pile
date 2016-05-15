package learn.example.joke.jsonobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

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
    private String lastTime;
    private int currentPage;

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
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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
          dest.writeInt(currentPage);
          dest.writeString(lastTime);
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
}

