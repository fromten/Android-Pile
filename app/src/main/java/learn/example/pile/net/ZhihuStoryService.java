package learn.example.pile.net;





import learn.example.pile.jsonbean.ZhihuStories;
import learn.example.pile.object.Zhihu;
import okhttp3.Request;

/**
 * Created on 2016/7/9.
 */
public class ZhihuStoryService extends GsonService {

    private static final String TAG = "ZhihuStoryService";

    public void getStories(Callback<ZhihuStories> callback)
    {
        performRequest(Zhihu.STORY_URL,callback);
    }

    public void getStoriesFromDate(String date, Callback<ZhihuStories> callback)
    {
        performRequest(Zhihu.STORY_URL_AT_TIME+date,callback);
    }



    private void performRequest(String url,Callback<ZhihuStories> callback)
    {
        Request req=new Request.Builder().url(url).build();
        newRequest(TAG,ZhihuStories.class,req,callback);
    }



}
