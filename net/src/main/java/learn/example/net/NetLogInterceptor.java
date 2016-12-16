package learn.example.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created on 2016/10/27.
 */

public class NetLogInterceptor implements Interceptor {
    private static final String TAG = "NetLogInterceptor";
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Log.d(TAG,String.format("start request >> Url=%s Head={%s}",request.url(),request.headers()));
        Response response=chain.proceed(request);
        if (response.isSuccessful())
        {
            Log.d(TAG,"request success " +response.toString());
        }else {
            Log.d(TAG,"request failed "+response.toString() );
        }
        return response;
    }


}
