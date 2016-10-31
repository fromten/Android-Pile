package learn.example.net;


import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created on 2016/10/31.
 */

public abstract class MainThreadCallback implements Callback {
    protected Handler mHandler;
    protected OkHttpRequest.RequestCallback mCallback;

    public MainThreadCallback(Handler handler, OkHttpRequest.RequestCallback callback) {
        if (handler.getLooper()!= Looper.getMainLooper())
        {
            throw new IllegalArgumentException("Handler Lopper must is Looper.getMainLooper()");
        }
        mHandler = handler;
        mCallback = callback;
    }


    @Override
    public abstract void onFailure(Call call, IOException e) ;


    @Override
    public abstract void onResponse(Call call, Response response) throws IOException;

}
