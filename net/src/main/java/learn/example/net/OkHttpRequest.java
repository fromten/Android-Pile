package learn.example.net;

import android.os.Handler;
import android.os.Looper;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created on 2016/7/1.
 */
public class OkHttpRequest {
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private static OkHttpRequest mInstance;
    private Gson mGson;
    private OkHttpRequest() {
        mOkHttpClient=new OkHttpClient();
        mHandler=new Handler(Looper.getMainLooper());
        mGson=new Gson();
    }

    public synchronized static OkHttpRequest getInstance()
    {
        if (mInstance==null)
        {
            mInstance=new OkHttpRequest();
        }
        return mInstance;
    }

    public <T> void newGsonRequest(final Class<T> type,Request request, final RequestCallback<T> callback){
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                 deliverFailureResult("request fail "+call.request().toString(),callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    T bean=mGson.fromJson(response.body().string(),type);
                    deliverSuccessResult(bean,callback);
                }catch (JsonSyntaxException e)
                {
                    deliverFailureResult(e.getCause().toString(),callback);
                }
            }
        });
    }
    public void newStringRequest(Request request, final RequestCallback<String> callback)
    {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverFailureResult("Request fail "+call.request().toString(),callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               deliverSuccessResult(response.body().string(),callback);
            }
        });
    }


    public Response syncRequest(Request request) throws IOException {
        return mOkHttpClient.newCall(request).execute();
    }

    public String syncStringRequest(String url) throws IOException {
        Request req=new Request.Builder().url(url).build();
        Response response=mOkHttpClient.newCall(req).execute();
        if (response.isSuccessful())
        {
           return response.body().string();
        }
        return null;
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
