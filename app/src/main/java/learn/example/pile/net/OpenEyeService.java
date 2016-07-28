package learn.example.pile.net;

import learn.example.pile.jsonbean.OpenEyeVideo;
import learn.example.pile.object.OpenEyes;
import okhttp3.Request;

/**
 * Created on 2016/7/22.
 */
public class OpenEyeService extends GsonService{
    private static final String TAG = "OpenEyeService";

    public void getHotVideo(Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getHotUrl(2);
        newRequest(TAG,OpenEyeVideo.class,buildRequest(url),callback);
    }

    public void next(String nextUrl,Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getNextHotUrl(nextUrl);
        newRequest(TAG,OpenEyeVideo.class,buildRequest(url),callback);
    }

    private Request buildRequest(String url)
    {
        return new Request.Builder().url(url).addHeader("cookie","wdj_auth=;sdk=23").build();
    }
}
