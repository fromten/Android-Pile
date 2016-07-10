package learn.example.pile.net;


import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.jsonbean.ZhihuNewsContent;
import learn.example.pile.object.Zhihu;
import okhttp3.Request;

/**
 * Created on 2016/7/10.
 */
public class ZhihuContentService extends Service<ZhihuNewsContent> {
    public ZhihuContentService(ServiceListener<ZhihuNewsContent> listener) {
        super(listener);
    }

    public void getContent(int id)
    {
        String url= Zhihu.CONTENT_URL+id;
        Request req=new Request.Builder().url(url).build();
        OkHttpRequest.getInstanceUnsafe().newGsonRequest(ZhihuNewsContent.class, req, new OkHttpRequest.RequestCallback<ZhihuNewsContent>() {
            @Override
            public void onSuccess(ZhihuNewsContent res) {
                success(res);
            }

            @Override
            public void onFailure(String msg) {
                failure(msg);
            }
        });
    }
}
