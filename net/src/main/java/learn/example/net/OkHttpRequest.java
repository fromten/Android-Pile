package learn.example.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.io.File;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;


/**
 * Created on 2016/7/1.
 */
public class OkHttpRequest{


    private static OkHttpRequest sInstance;

    private Handler mHandler;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;

    private static final boolean DEBUG=BuildConfig.DEBUG;
    public static final int CACHE_SIZE=50*1024*1024;// 50 MIB
    public static final int DEF_TIME_OUT=10;// second
    public static final String CACHE_DIRECTORY ="com.app.okhttp3_cache";



    private OkHttpRequest(Context context) {
        OkHttpClient.Builder okBuilder=new OkHttpClient.Builder();
        if (context!=null)
        {
            File file=new File(context.getCacheDir(), CACHE_DIRECTORY);
            Cache cache=new Cache(file,CACHE_SIZE);
            okBuilder.cache(cache);
        }
        if (DEBUG)
        {
            okBuilder.addInterceptor(new NetLogInterceptor());
        }
        mOkHttpClient =okBuilder.connectTimeout(DEF_TIME_OUT,TimeUnit.SECONDS)
                             .readTimeout(DEF_TIME_OUT,TimeUnit.SECONDS)
                             .writeTimeout(DEF_TIME_OUT*2,TimeUnit.SECONDS)
                             .build();
        mHandler=new Handler(Looper.getMainLooper());
        mGson= new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create();
    }


    /**
     * 不安全的使用,如果这个方法在getInstance(Context context)之前调用,将返回Null对象
     * @return
     */
    public static OkHttpRequest getInstanceUnsafe()
    {
        return sInstance;
    }


    /**
     *
     * @param context 使用磁盘缓存,可以传递Null对象,不使用缓存
     * @return
     */
    public synchronized static OkHttpRequest getInstance(Context context)
    {
        if (sInstance ==null)
        {
            sInstance =new OkHttpRequest(context);
        }
        return sInstance;
    }

    public <T> Call newGsonRequest(final Class<T> clazz, final Request request, final RequestCallback<T> callback)
    {
        Call call= mOkHttpClient.newCall(request);
        call.enqueue(new GsonCallback<T>(clazz,mGson,mHandler,callback));
        return call;
    }

    public <T> Call newGsonRequest(final Class<T> clazz, final Request request,Gson gson, final RequestCallback<T> callback)
    {
        Call call= mOkHttpClient.newCall(request);
        call.enqueue(new GsonCallback<T>(clazz,gson,mHandler,callback));
        return call;
    }

    public Call newStringRequest(Request request, final RequestCallback<String> callback)
    {
        Call call= mOkHttpClient.newCall(request);
        call.enqueue(new StringCallback(mHandler,callback));
        return call;
    }


    public Call syncCall(String url)
    {
        return syncCall(new Request.Builder().url(url).build());
    }

    public Call syncCall(Request request)
    {
        return mOkHttpClient.newCall(request);
    }


    public interface RequestCallback<T>
    {
        void onSuccess(T res);
        void onFailure(String msg);
    }

}
