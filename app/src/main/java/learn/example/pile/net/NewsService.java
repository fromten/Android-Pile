package learn.example.pile.net;

import learn.example.pile.MyURI;
import learn.example.pile.jsonbean.NewsJsonData;
import okhttp3.Request;

/**
 * Created on 2016/7/1.
 */
public class NewsService extends GsonService {
    private static final String TAG = "NewsService";
    public void getNews(Callback<NewsJsonData> callback)
    {
        Request request=new Request.Builder().url(MyURI.NEW_DATA_REQUEST_URL).addHeader("apikey", MyURI.API_KEY).build();
        newRequest(TAG,NewsJsonData.class,request,callback);
    }
}
