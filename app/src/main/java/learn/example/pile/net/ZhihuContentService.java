package learn.example.pile.net;


import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.object.Zhihu;
import okhttp3.Request;

/**
 * Created on 2016/7/10.
 */
public class ZhihuContentService extends GsonService {

    private static final String TAG = "ZhihuContentService";

    public void getContent(int id,Callback<ZhihuNewsContent> callback)
    {
        String url= Zhihu.CONTENT_URL+id;
        Request req=new Request.Builder().url(url).build();
        newRequest(TAG,ZhihuNewsContent.class,req,callback);
    }
}
