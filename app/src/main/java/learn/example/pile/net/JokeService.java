package learn.example.pile.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import learn.example.pile.pojo.Joke;
import learn.example.pile.provider.JokeProvider;
import learn.example.pile.util.gson.JokeTypeAdapter;
import okhttp3.Request;

/**
 * Created on 2016/7/1.
 */
public class JokeService extends NetService {

    private static final String TAG = "JokeService";



    public void getTuijianJoke(int count,int screenWidth,IService.Callback<Joke> callback)
    {
        String url= JokeProvider.createHotUrl(count,screenWidth);
        Request request=new Request.Builder().url(url).build();
        newRequest(TAG,Joke.class,request,buildMyGson(),callback);
    }

    public void getComment(int start,int len,String groupID,IService.Callback<String> callback)
    {
        String url= JokeProvider.createCommentUrl(start,len,groupID);
        newStringRequest(TAG,url,callback);
    }

    private Gson buildMyGson()
    {
        return new GsonBuilder()
                .registerTypeAdapter(Joke.class,new JokeTypeAdapter())
                .create();
    }
}
