package learn.example.pile.factory;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.object.OpenEyes;
import learn.example.pile.util.GsonHelper;

/**
 * Created on 2016/8/5.
 */
public class OpenEyeVideoFactory {


    public List<OpenEyes.VideoInfo> getInfoList(JsonArray issueList) {
        if (issueList != null) {
            try {
                List<OpenEyes.VideoInfo> list = new ArrayList<>();
                for (JsonElement e : issueList) {

                    JsonArray itemList = e.getAsJsonObject().getAsJsonArray("itemList");
                    if (itemList != null) {
                        for (JsonElement item : itemList) {
                            JsonObject o = item.getAsJsonObject();
                            String type = GsonHelper.getAsString(o.get("type"), null);
                            if (TextUtils.equals(type, "video")) {
                                list.add(getVideoInfo(o.getAsJsonObject("data")));
                            }
                        }
                    }
                }
                return list;
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private OpenEyes.VideoInfo getVideoInfo(JsonObject data) {
        String title = GsonHelper.getAsString(data.get("title"), null);
        String playUrl = GsonHelper.getAsString(data.get("playUrl"), null);
        int duration = GsonHelper.getAsInteger(data.get("duration"), 0);
        int id = GsonHelper.getAsInteger(data.get("id"), 0);
        JsonObject cover = data.getAsJsonObject("cover");
        String imageUrl = GsonHelper.getAsString(cover.get("feed"), null);
        return new OpenEyes.VideoInfo(title, playUrl, imageUrl, duration, id);
    }


}
