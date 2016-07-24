package learn.example.pile.net;

import java.util.Locale;

import learn.example.pile.MyURI;
import learn.example.pile.jsonbean.NetEaseComment;
import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.jsonbean.NewsJsonData;
import learn.example.pile.object.NetEase;
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


    public void getNetEaseNews(int page,Callback<NetEaseNews> callback)
    {
       // String url=String.format(Locale.CHINA,NetEase.TOUTAI_URL,page,20);
        String sign="SK6FxIsU30l0eN4Kb/7PUv0OSGGIfD8Up2mqkxqGk+R48ErR02zJ6/KXOnxX046I";
        String url=NetEase.generateParamsUrl(page,sign);
        newRequest(TAG,NetEaseNews.class,url,callback);
    }

    public void getHotComment(String boradId, String docId, int start, int length, Callback<NetEaseComment> callback)
    {
       String url=NetEase.generateHotCommentUrl(boradId,docId,start,length);
       newRequest(TAG,NetEaseComment.class,url,callback);
    }

    public void getNormalComment(String boradId, String docId, int start, int length, Callback<NetEaseComment> callback)
    {
        String url=NetEase.generateNormalCommentUrl(boradId,docId,start,length);
        newRequest(TAG,NetEaseComment.class,url,callback);
    }
}
