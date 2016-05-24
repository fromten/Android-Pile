package learn.example.pile.jsonobject;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2016/5/7.
 */
public class NewsJsonData {
    public int errNum;
    public String errMsg;

    @SerializedName("retData")
    public List<resData> resData;
    public static class resData{
        public String title;
        @SerializedName("url")
        public String newsUrl;
        @SerializedName("abstract")
        public String newsDes;
        @SerializedName("image_url")
        public String imageUrl;
    }
}
