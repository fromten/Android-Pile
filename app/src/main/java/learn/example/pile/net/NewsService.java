package learn.example.pile.net;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.MyURI;
import learn.example.pile.jsonbean.NewsJsonData;
import okhttp3.Request;

/**
 * Created on 2016/7/1.
 */
public class NewsService extends Service<NewsJsonData> {

    public NewsService(ServiceListener<NewsJsonData> listener) {
        super(listener);
    }

    public void getNews()
    {
        Request request=new Request.Builder().url(MyURI.NEW_DATA_REQUEST_URL).addHeader("apikey", MyURI.API_KEY).build();
        OkHttpRequest.getInstance().newGsonRequest(NewsJsonData.class,request, new OkHttpRequest.RequestCallback<NewsJsonData>() {
            @Override
            public void onSuccess(NewsJsonData res) {
                if (res.getErrNum()==0)
                  success(res);
                else {
                    failure("news data request fail because response code invalid");
                }
            }

            @Override
            public void onFailure(String msg) {
                  failure(msg);
            }
        });
    }
}
