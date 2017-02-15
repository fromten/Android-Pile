package learn.example.pile.util.gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import learn.example.pile.pojo.Joke;

/**
 * Created on 2016/11/4.
 */

public class JokeTypeAdapter implements JsonDeserializer<Joke> {
    @Override
    public Joke deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray array=json.getAsJsonObject().getAsJsonObject("data").getAsJsonArray("data");
        if (array==null||array.size()==0)return null;
        List<Joke.Item> list=new ArrayList<>();
        for (JsonElement e:array) {
            if (e.isJsonNull())
                continue;
            JsonObject ob=e.getAsJsonObject();
            int type=ob.getAsJsonPrimitive("type").getAsInt();
            if (type==5)continue;//跳过广告
            JsonObject group=ob.getAsJsonObject("group");
            Joke.Item item=createItem(group);
            if (item!=null)
            {
                list.add(createItem(group));
            }
        }
        return new Joke(list);
    }

    public Joke.Item createItem(JsonObject group)
    {
        Joke.Item item=null;
        if (isValid(group))
        {
            item=new Joke.Item();
            commonSet(item,group);
            typeSet(item,group);
        }
        return item;
    }

    private boolean isValid(JsonObject group) {
        return !group.has("is_gif") || group.has("gifvideo");
    }

    private void commonSet(Joke.Item item, JsonObject group)
    {
        SafeJsonObject safeWrap=new SafeJsonObject(group);
        String title=safeWrap.getString("title");
        String idStr=safeWrap.getString("id_str");
        String coverUrl=coverUrl(group);
        String content=safeWrap.getString("content");
        int commentCount=safeWrap.getInteger("comment_count");
        int likeCount=safeWrap.getInteger("favorite_count");
        int unlikeCount=safeWrap.getInteger("bury_count");
        long time=safeWrap.getLong("create_time");
        item.setTitle(title);
        item.setId_str(idStr);
        item.setImage(coverUrl==null?null:new Joke.Image(coverUrl,false));
        item.setText(content);
        item.setLikeCount(likeCount);
        item.setUnLikeCount(unlikeCount);
        item.setCommentCount(commentCount);
        item.setPushTime(time);

        JsonObject user=group.getAsJsonObject("user");
        if (user!=null)
        {
            item.setAvatar(GsonHelper.getAsString(user.get("avatar_url"),null));
            item.setUserName(GsonHelper.getAsString(user.get("name"),null));
        }
    }


    private void typeSet(Joke.Item item, JsonObject group)
    {
        String videoUrl=null;
        if (group.has("is_video")&&group.has("mp4_url"))
        {
            item.setVideo(true);
            videoUrl=group.get("mp4_url").getAsString();
        }else if (group.has("is_gif"))
        {
            item.setGif(true);
            videoUrl=group.getAsJsonObject("gifvideo").get("mp4_url").getAsString();
        }else if (group.has("is_multi_image"))
        {
            item.setMultiImages(true);
            JsonArray array=group.getAsJsonArray("large_image_list");
            int size=array.size();
            Joke.Image[] images=new Joke.Image[size];
            for (int i = 0; i < size; i++) {
                JsonObject o=array.get(i).getAsJsonObject();
                String url=getUrl(o);
                boolean isGif=o.get("is_gif").getAsBoolean();
                images[i]=new Joke.Image(url,isGif);
            }
            item.setImageUrls(images);
        }
        item.setVideo(videoUrl==null?null:new Joke.Video(videoUrl));
    }


    private String coverUrl(JsonObject group)
    {
        JsonObject imageOb=null;
        if (group.has("large_cover"))
        {
            imageOb=group.getAsJsonObject("large_cover");
        }else if (group.has("large_image"))
        {
            imageOb=group.getAsJsonObject("large_image");
        }else if (group.has("middle_image"))
        {
            imageOb=group.getAsJsonObject("middle_image");
        }
        return imageOb==null?null:getUrl(imageOb);
    }

    private String getUrl(JsonObject object)
    {
        JsonArray array=object.getAsJsonArray("url_list");
        return array==null?null:array.get(0).getAsJsonObject().get("url").getAsString();
    }
}
