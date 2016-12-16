package learn.example.pile.net;


import android.support.annotation.Nullable;
import android.support.v4.util.SimpleArrayMap;


import com.google.gson.Gson;

import learn.example.net.OkHttpRequest;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created on 2016/7/11.
 */
public class NetService implements IService {
    private SimpleArrayMap<String, Call> map = new SimpleArrayMap<>();


    public void newStringRequest(final String tag, String url, final IService.Callback<String> callback) {
        Request request = new Request.Builder().url(url).build();
        newStringRequest(tag, request, callback);
    }

    public void newStringRequest(final String tag, Request request, final IService.Callback<String> callback) {
        final Call call = OkHttpRequest.getInstanceUnsafe().newStringRequest(request, new OkHttpRequest.RequestCallback<String>() {
            @Override
            public void onSuccess(String res) {
                callback.onSuccess(res);
                remove(tag);
            }

            @Override
            public void onFailure(String msg) {
                callback.onFailure(msg);
                remove(tag);
            }
        });
        addToMap(tag, call);
    }

    public <T> void newRequest(final String tag, Class<T> clazz, String url, final IService.Callback<T> callback) {
        Request request = new Request.Builder().url(url).build();
        newRequest(tag, clazz, request, callback);
    }

    public <T> void newRequest(final String tag, Class<T> clazz, Request request, final IService.Callback<T> callback) {
        newRequest(tag, clazz, request, null, callback);
    }

    public <T> void newRequest(final String tag, Class<T> clazz, Request request,
                               @Nullable Gson gson, final IService.Callback<T> callback) {
        OkHttpRequest.RequestCallback<T> requestCallback = new OkHttpRequest.RequestCallback<T>() {
            @Override
            public void onSuccess(T res) {
                callback.onSuccess(res);
                remove(tag);
            }
            @Override
            public void onFailure(String msg) {
                callback.onFailure(msg);
                remove(tag);
            }
        };
        Call call = null;
        if (gson == null) {
            call = OkHttpRequest.getInstanceUnsafe().newGsonRequest(clazz, request, requestCallback);
        } else {
            call = OkHttpRequest.getInstanceUnsafe().newGsonRequest(clazz, request, gson, requestCallback);
        }
        addToMap(tag, call);
    }


    protected void addToMap(String tag, Call call) {
        map.put(tag, call);
    }

    protected void remove(String tag) {
        cancel(tag);
    }

    public void cancel(String tag) {
        Call call = map.get(tag);
        if (call != null) {
            call.cancel();
        }
        map.remove(tag);
    }

    public void cancelAll() {
        int count = map.size();
        for (int i = 0; i < count; i++) {
            Call value = map.valueAt(i);
            if (value != null) {
                value.cancel();
            }
        }
        map.clear();
    }

}
