package learn.example.net;

import android.os.Handler;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created on 2016/10/31.
 */

public class StringCallback extends MainThreadCallback {

    public StringCallback(Handler handler, OkHttpRequest.RequestCallback<String> callback) {
        super(handler, callback);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if (!call.isCanceled())
        {
            deliverFailureResult(call.request().toString());
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
         if (call.isCanceled())return;
         String res=response.body().string();
         deliverSuccessResult(res);
    }


    public void deliverSuccessResult(final String data)
    {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onSuccess(data);
            }
        });
    }

    public void deliverFailureResult(final String msg)
    {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onFailure(msg);
            }
        });
    }
}
