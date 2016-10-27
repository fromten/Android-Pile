package learn.example.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created on 2016/7/1.
 */
public class OkHttpRequest{


    private static OkHttpRequest sInstance;


    private Handler mHandler;
    private OkHttpClient mOkHttpClient;
    private Gson mGson;
    public static final int CACHE_SIZE=50*1024*1024;// 50 MIB
    public static final int DEF_TIME_OUT=10;// second
    public static final String CAHDE_DIRECTORY="com.app.okhttp3_cache";



    private OkHttpRequest(Context context) {

        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        if (context!=null)
        {
            File file=new File(context.getCacheDir(),CAHDE_DIRECTORY);
            Cache cache=new Cache(file,CACHE_SIZE);
            builder.cache(cache);
        }
        mOkHttpClient =builder.connectTimeout(DEF_TIME_OUT,TimeUnit.SECONDS)
                             .readTimeout(DEF_TIME_OUT,TimeUnit.SECONDS)
                             .addInterceptor(new NetLogInterceptor())
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
    public synchronized static OkHttpRequest getInstanceUnsafe()
    {
        return sInstance;
    }


    /**
     *
     * @param context 使用磁盘缓存,可以传递Null对象,不使用缓存
     * @return
     */
    public synchronized static OkHttpRequest getInstance(@Nullable Context context)
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
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (!call.isCanceled())
                {
                    deliverFailureResult(call.request().toString(),callback);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful())
                {
                    try {
                        T ser=mGson.fromJson(response.body().string(),clazz);
                        deliverSuccessResult(ser,callback);
                    }catch (JsonSyntaxException e)
                    {
                        deliverFailureResult(call.request().toString()+"response: "+response.toString(),callback);
                    }
                }else {
                    deliverFailureResult(call.request().toString()+" response: "+response.toString(),callback);
                }
                response.close();
            }
        });
        return call;
    }

    public Call newStringRequest(Request request, final RequestCallback<String> callback)
    {
        Call call= mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverFailureResult(call.request().toString(),callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful())
                {
                    deliverSuccessResult(response.body().string(),callback);
                }else {
                    deliverFailureResult(call.request().toString(),callback);
                }
                response.close();
            }
        });
        return call;
    }


    public Call newCall(String url)
    {
        return newCall(new Request.Builder().url(url).build());
    }

    public Call newCall(Request request)
    {
        return mOkHttpClient.newCall(request);
    }



    private  <T>void deliverSuccessResult(final T data, final RequestCallback<T> callBack)
    {
       mHandler.post(new Runnable() {
          @Override
          public void run() {
              callBack.onSuccess(data);
          }
      });
    }

    private void deliverFailureResult(final String msg, final RequestCallback callBack)
    {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
               callBack.onFailure(msg);
            }
        });
    }


    public interface RequestCallback<T>
    {
        void onSuccess(T res);
        void onFailure(String msg);
    }

}
