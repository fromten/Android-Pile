package learn.example.pile.net;

import learn.example.pile.jsonbean.OpenEyeVideo;
import learn.example.pile.provider.OpenEyes;

/**
 * Created on 2016/7/22.
 */
public class OpenEyeService extends NetService {
    private static final String TAG = "OpenEyeService";

    public void getHotVideo(Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getHotUrl(2);
        newRequest(TAG,OpenEyeVideo.class,url,callback);
    }

    public void getCategoryVideoInDateSort(@OpenEyes.Category int categoryID, Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getCategoryUrl(categoryID,OpenEyes.Strategy.DATE);
        newRequest(TAG,OpenEyeVideo.class,url,callback);
    }

    public void getCategoryVideoInShareCountSort(@OpenEyes.Category int categoryID, Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getCategoryUrl(categoryID,OpenEyes.Strategy.SHARE_COUNT);
        newRequest(TAG,OpenEyeVideo.class,url,callback);
    }


    public void getComments(int id, Callback<String> callback)
    {
        String url=OpenEyes.COMMENT_URL+"?id="+id+OpenEyes.APP_PARAMS;
        newStringRequest(TAG,url,callback);
    }


    //翻页,获得下个视频列表
    public void nextVideoList(String nextUrl, Callback<OpenEyeVideo> callback)
    {
        String url=OpenEyes.getNextUrl(nextUrl);
        newRequest(TAG,OpenEyeVideo.class,url,callback);
    }


    public void nextCommentList(String nextUrl,Callback<String> callback)
    {
        String url=OpenEyes.getNextUrl(nextUrl);
        newStringRequest(TAG,url,callback);
    }


}
