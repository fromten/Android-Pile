package learn.example.pile.net;

import learn.example.pile.jsonbean.OpenEyeComment;
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

    public void getCategoryVideoDateSort(int categoryID,Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getCategoryUrl(categoryID,OpenEyes.Strategy.DATE);
        newRequest(TAG,OpenEyeVideo.class,buildRequest(url),callback);
    }

    public void getCategoryVideoShareCountSort(int categoryID,Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getCategoryUrl(categoryID,OpenEyes.Strategy.SHARE_COUNT);
        newRequest(TAG,OpenEyeVideo.class,buildRequest(url),callback);
    }


    public void getComments(int id, Callback<OpenEyeComment> callback)
    {
        String url=OpenEyes.COMMENT_URL+"?id="+id+OpenEyes.APP_PARAMS;
        newRequest(TAG,OpenEyeComment.class,buildRequest(url),callback);
    }


    //翻页,获得下个视频列表
    public void nextVideoList(String nextUrl, Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getNextUrl(nextUrl);
        newRequest(TAG,OpenEyeVideo.class,buildRequest(url),callback);
    }


    public void nextCommentList(String nextUrl,Callback<OpenEyeComment> callback)
    {
        String url=OpenEyes.getNextUrl(nextUrl);
        newRequest(TAG,OpenEyeComment.class,buildRequest(url),callback);
    }

    private Request buildRequest(String url)
    {
        return new Request.Builder().url(url).addHeader("cookie","wdj_auth=;sdk=23").build();
    }
}
