package learn.example.pile.net;





import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.jsonbean.ZhihuStories;
import learn.example.pile.object.Zhihu;
import okhttp3.Request;

/**
 * Created on 2016/7/9.
 */
public class ZhihuStoryService extends Service<ZhihuStories> {


    public ZhihuStoryService(ServiceListener<ZhihuStories> listener) {
        super(listener);
    }

    public void getStories()
    {
        performRequest(Zhihu.STORY_URL);
    }

    public void getStoriesAtTime(String date)
    {
        performRequest(Zhihu.STORY_URL_AT_TIME+date);
    }



    private void performRequest(String url)
    {
        Request req=new Request.Builder().url(url).build();
        OkHttpRequest.getInstanceUnsafe().newGsonRequest(ZhihuStories.class, req, new OkHttpRequest.RequestCallback<ZhihuStories>() {
            @Override
            public void onSuccess(ZhihuStories res) {
                 success(res);
            }

            @Override
            public void onFailure(String msg) {
                failure(msg);
            }
        });
    }



}
