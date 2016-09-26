package learn.example.pile.service;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import learn.example.net.OkHttpRequest;
import learn.example.pile.util.DeviceInfo;
import learn.example.pile.util.GsonHelper;
import okhttp3.Response;

/**
 * Created on 2016/9/25.
 */

public class StartImageCacheService extends IntentService {

    public static final String URL_JSON_START_IMAGE="http://news-at.zhihu.com/api/4/start-image";
    public static final String CACHE_FILE_NAME="startImageCacheABCD";
    public static final String KEY_IMAGE_OWN="bootactivity_start_image_own";
    public static final String PREFERENCE_FILE_NAME="service_preference_file";

    private String mImageUrl;

    public StartImageCacheService() {
        super("StartImageCacheService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent==null)return;

        try {
            //尝试八次
            for (int i = 0; i < 8; i++) {
                if (mImageUrl==null)
                {
                    mImageUrl=requestJson();
                }
                if (mImageUrl!=null)
                {
                    if (startCache(mImageUrl))
                    {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 缓存图片到磁盘
     * @param imageUrl 图片Url地址
     * @return true 缓存成功,else wise
     * @throws IOException
     */
    private boolean startCache(String imageUrl) throws IOException {
        OkHttpRequest request=OkHttpRequest.getInstance(getApplicationContext());
        Response response= request.newCall(imageUrl).execute();
        if (response.isSuccessful())
        {
            InputStream inputStream=response.body().byteStream();
            File file=getFileStreamPath(CACHE_FILE_NAME);
            FileOutputStream out=new FileOutputStream(file);
            byte[] buffer=new byte[1024];
            int len=-1;
            while ((len=inputStream.read(buffer))>=0)
            {
                out.write(buffer,0,len);
            }
            out.close();
            inputStream.close();
            return true;
        }
        return false;
    }


    /**
     * 请求启动界面图片Json数据
     * @return 图片Url地址,null 请求失败
     * @throws IOException
     */
    private String requestJson() throws IOException {
        OkHttpRequest request=OkHttpRequest.getInstance(getApplicationContext());
        Response response= request.newCall(getMatchStartImageUrl()).execute();
        if (response.isSuccessful())
        {
            JsonObject object= (JsonObject) new JsonParser().parse(response.body().string());
            final String imageUrl= GsonHelper.getAsString(object.get("img"),null);
            final String text=GsonHelper.getAsString(object.get("text"),null);

            getSharedPreferences(PREFERENCE_FILE_NAME,Context.MODE_PRIVATE)
                    .edit().putString(KEY_IMAGE_OWN,text).apply();
            return imageUrl;
        }
        return null;
    }


    /**
     * 缓存图片到磁盘
     * @param bitmap bitmap
     */
    public void cacheBitmap(Bitmap bitmap)
    {   if (bitmap==null)return;

        File file=getFileStreamPath(CACHE_FILE_NAME);
        FileOutputStream outputStream=null;
        try {
            outputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream!=null)
            {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getMatchStartImageUrl()
    {
        DeviceInfo info=new DeviceInfo(getApplicationContext());
        return URL_JSON_START_IMAGE+"/"+info.SCREEN_WIDTH+"*"+info.SCREEN_HEIGHT;
    }




}
