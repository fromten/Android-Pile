package learn.example.pile.net;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.MyURI;
import learn.example.pile.jsonbean.JokeJsonData;
import okhttp3.Request;

/**
 * Created on 2016/7/1.
 */
public class JokeService extends GsonService {

    private static final String TAG = "JokeService";

    public void getImageJoke(int page,IService.Callback<JokeJsonData> callback)
    {
        String url=MyURI.IMAGE_JOKE_REQUEST_URL+"?page="+page;
        Request req=new Request.Builder().url(url).addHeader("apikey", MyURI.API_KEY).build();
        newRequest(TAG,JokeJsonData.class,req,callback);
    }
    public void getTextJoke(int page,IService.Callback<JokeJsonData> callback)
    {
        String url=MyURI.TEXT_JOKE_REQUEST_URL+"?page="+page;
        Request req=new Request.Builder().url(url).addHeader("apikey", MyURI.API_KEY).build();
        newRequest(TAG,JokeJsonData.class,req,callback);
    }
}
