package learn.example.pile.database.typeserializer;

import com.activeandroid.annotation.Table;
import com.activeandroid.serializer.TypeSerializer;


import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.util.GsonHelper;

/**
 * Created on 2016/9/19.
 */
@Table(name = "ImageUrls")
public class NewsImageExtraArraySerializer extends TypeSerializer {


    @Override
    public Class<?> getDeserializedType() {
        return NetEaseNews.T1348647909107Bean.ImageExtraBean[].class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public Object serialize(Object data) {
        if (data==null)return null;
        String json= GsonHelper.serialize(data);
        return json;
    }

    @Override
    public Object deserialize(Object data) {
        if (data==null)return null;
        NetEaseNews.T1348647909107Bean.ImageExtraBean[] images=
                GsonHelper.deserialize(data.toString(),NetEaseNews.T1348647909107Bean.ImageExtraBean[].class);
        return images;
    }
}
