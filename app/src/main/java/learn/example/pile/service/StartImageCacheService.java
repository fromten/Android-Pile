package learn.example.pile.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import learn.example.net.OkHttpRequest;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.gson.GsonHelper;
import okhttp3.Response;

/**
 * Created on 2016/9/25.
 */

public class StartImageCacheService extends IntentService {

    public static final String URL_JSON_START_IMAGE = "http://news-at.zhihu.com/api/7/prefetch-launch-images";
    public static final String CACHE_FILE_NAME = "startImageCacheABCD";
    public static final String KEY_IMAGE_OWNER = "bootactivity_start_image_owner";
    public static final String PREFERENCE_FILE_NAME = "service_preference";

    private String mImageUrl;

    public StartImageCacheService() {
        super("StartImageCacheService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) return;
        //尝试3次
        for (int i = 0; i < 3; i++) {
            if (mImageUrl == null) {
                try {
                    mImageUrl = getImageUrlAndSaveExtraMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (mImageUrl==null)continue;
            }
            try {
                if (startCache(mImageUrl))
                {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 缓存图片到磁盘
     *
     * @param imageUrl 图片Url地址
     * @return true 缓存成功,else wise
     * @throws IOException
     */
    private boolean startCache(String imageUrl) throws IOException {
        OkHttpRequest request = OkHttpRequest.getInstance(getApplicationContext());
        Response response = request.syncCall(imageUrl).execute();
        if (response.isSuccessful()) {
            InputStream inputStream = response.body().byteStream();
            File file = getFileStreamPath(CACHE_FILE_NAME);
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            inputStream.close();
            return true;
        }
        return false;
    }


    /**
     * 请求启动界面图片所需要Json数据,并返回图片Url
     *
     * @return 图片Url地址, null 请求失败
     * @throws IOException
     */
    private String getImageUrlAndSaveExtraMsg() throws IOException {
        OkHttpRequest request = OkHttpRequest.getInstance(getApplicationContext());
        Response response = request.syncCall(getMatchStartImageUrl()).execute();
        String body=response.body().string();
        response.close();
        if (response.isSuccessful()) {
            JsonObject object = (JsonObject) new JsonParser().parse(body);
            try {
               JsonArray array= object.getAsJsonArray("creatives");
               if (array!=null&&!array.isJsonNull()&&array.size()>1)
               {
                   object=array.get(1).getAsJsonObject();
                   final String imageUrl = GsonHelper.getAsString(object.get("url"), null);
                   final String text = GsonHelper.getAsString(object.get("text"), null);
                   //保存作者信息
                   getSharedPreferences(PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)
                           .edit().putString(KEY_IMAGE_OWNER, text).apply();
                   return imageUrl;
               }
            }catch (Exception e)
            {

            }
        }
        return null;
    }


    public String getMatchStartImageUrl() {
        DeviceInfo info = new DeviceInfo(getApplicationContext());
        return URL_JSON_START_IMAGE + "/" + info.SCREEN_WIDTH + "*" + info.SCREEN_HEIGHT;
    }


}
