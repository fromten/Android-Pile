package learn.example.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created on 2016/7/1.
 */
public class OkHttpRequest{
    private Handler mHandler;
    private OkHttpClient mOkhttpClient;

    private static OkHttpRequest mInstance;

    private Gson mGson;
    public static final int CACHE_SIZE=30*1024*1024;// 30 MIB
    public static final String CAHDE_DIRECTORY="com.app.okhttp3_cache";



    private OkHttpRequest(Context context) {
        File file=new File(context.getCacheDir(),CAHDE_DIRECTORY);
        Cache cache=new Cache(file,CACHE_SIZE);
        mOkhttpClient=new OkHttpClient.Builder().cache(cache).build();
        mHandler=new Handler(Looper.getMainLooper());
        mGson=new Gson();
    }

    public synchronized static OkHttpRequest getInstanceUnsafe()
    {
        if (mInstance==null)
        {
            throw new NullPointerException("Class no init,to use getInstance(Context context)");
        }
        return mInstance;
    }

    public synchronized static OkHttpRequest getInstance(Context context)
    {
        if (mInstance==null)
        {
            mInstance=new OkHttpRequest(context);
        }
        return mInstance;
    }

    public <T> Call newGsonRequest(final Class<T> clazz, final Request request, final RequestCallback<T> callback)
    {
        Call call=mOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverFailureResult(call.request().toString(),callback);
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
                        deliverFailureResult(call.request().toString(),callback);

                    }
                }else {
                    deliverFailureResult(call.request().toString(),callback);
                }
                response.close();
            }
        });
        return call;
    }

    public Call newStringRequest(Request request, final RequestCallback<String> callback)
    {
        Call call=mOkhttpClient.newCall(request);
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
        return mOkhttpClient.newCall(request);
    }



    private  <T>void deliverSuccessResult(final T data, final RequestCallback<T> callBack)
    {
       mHandler.postDelayed(new Runnable() {
          @Override
          public void run() {
              callBack.onSuccess(data);
          }
      },200);
    }

    private void deliverFailureResult(final String msg, final RequestCallback callBack)
    {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
               callBack.onFailure(msg);
            }
        },200);
    }


    public interface RequestCallback<T>
    {
        void onSuccess(T res);
        void onFailure(String msg);
    }

}
