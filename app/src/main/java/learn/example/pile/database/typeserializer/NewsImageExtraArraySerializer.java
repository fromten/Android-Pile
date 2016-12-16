package learn.example.pile.database.typeserializer;


import com.activeandroid.serializer.TypeSerializer;


import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.util.gson.GsonHelper;

/**
 * Created on 2016/9/19.
 */
public class NewsImageExtraArraySerializer extends TypeSerializer {


    @Override
    public Class<?> getDeserializedType() {
        return NetEaseNews.NewsItem.ImageExtraBean[].class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public String serialize(Object data) {
        if (data==null)return null;
        String json= GsonHelper.serialize(data);
        return json;
    }

    @Override
    public NetEaseNews.NewsItem.ImageExtraBean[] deserialize(Object data) {
        if (data==null)return null;
        NetEaseNews.NewsItem.ImageExtraBean[] images=
                GsonHelper.deserialize(data.toString(),NetEaseNews.NewsItem.ImageExtraBean[].class);
        return images;
    }
}
