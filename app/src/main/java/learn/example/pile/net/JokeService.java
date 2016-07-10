package learn.example.pile.net;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.MyURI;
import learn.example.pile.jsonbean.JokeJsonData;
import okhttp3.Request;

/**
 * Created on 2016/7/1.
 */
public class JokeService extends Service<JokeJsonData> {
    public JokeService(ServiceListener<JokeJsonData> listener) {
        super(listener);
    }

    public void getImageJoke(int page)
    {
        String url=MyURI.IMAGE_JOKE_REQUEST_URL+"?page="+page;
        final Request req=new Request.Builder().url(url).addHeader("apikey", MyURI.API_KEY).build();
        OkHttpRequest.getInstanceUnsafe().newGsonRequest(JokeJsonData.class,req, new OkHttpRequest.RequestCallback<JokeJsonData>() {
            @Override
            public void onSuccess(JokeJsonData res) {
                if (res.getResCode()==0)
                {
                    success(res);
                }else {
                    failure("image joke request error");
                }

            }

            @Override
            public void onFailure(String msg) {
                 failure(msg);
            }
        });
    }
    public void getTextJoke(int page)
    {
        String url=MyURI.TEXT_JOKE_REQUEST_URL+"?page="+page;
        Request req=new Request.Builder().url(url).addHeader("apikey", MyURI.API_KEY).build();
        OkHttpRequest.getInstanceUnsafe().newGsonRequest(JokeJsonData.class,req, new OkHttpRequest.RequestCallback<JokeJsonData>() {
            @Override
            public void onSuccess(JokeJsonData res) {
                success(res);
            }

            @Override
            public void onFailure(String msg) {
                failure(msg);
            }
        });
    }
}
