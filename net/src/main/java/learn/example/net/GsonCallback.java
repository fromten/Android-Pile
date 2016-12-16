package learn.example.net;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created on 2016/10/31.
 */

public class GsonCallback<T> extends MainThreadCallback {

    private Class<T> clazz;
    private Gson mGson;
    public GsonCallback(Class<T> serClass, Gson gson, Handler handler,OkHttpRequest.RequestCallback<T> callback) {
        super(handler, callback);
        this.clazz=serClass;
        mGson=gson;
    }

    @Override
    public void onFailure(final Call call, IOException e) {
        if (!call.isCanceled())
        {
            deliverFailureResult("Request fail in "+call.request());
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (call.isCanceled())return;
        String resStr=response.body().string();
        try {
            T ser=mGson.fromJson(resStr,clazz);
            deliverSuccessResult(ser);
        }catch (JsonSyntaxException e)
        {
            deliverFailureResult("JsonSyntaxException "+e.getMessage()+
                                 ",Request " +call.request()+
                                 ",response is "+resStr);
        }
    }

    private void deliverSuccessResult(final T data)
    {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(data);
            }
        });
    }

    private void deliverFailureResult(final String msg)
    {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onFailure(msg);
            }
        });
    }
}
