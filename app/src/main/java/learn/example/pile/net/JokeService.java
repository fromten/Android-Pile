package learn.example.pile.net;

import learn.example.pile.jsonbean.JokeBean;
import learn.example.pile.provider.JokeProvider;
import okhttp3.Request;

/**
 * Created on 2016/7/1.
 */
public class JokeService extends NetService {

    private static final String TAG = "JokeService";



    public void getTuijianJoke(int count,int screenWidth,IService.Callback<JokeBean> callback)
    {
        String url= JokeProvider.createHotUrl(count,screenWidth);
        Request req=new Request.Builder().url(url).build();
        newRequest(TAG,JokeBean.class,req,callback);
    }

    public void getComment(int start,int len,String groupID,IService.Callback<String> callback)
    {
        String url= JokeProvider.createCommentUrl(start,len,groupID);
        newStringRequest(TAG,url,callback);
    }
}
