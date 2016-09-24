package learn.example.pile.net;

import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.object.NetEase;

/**
 * Created on 2016/7/1.
 */
public class NetEaseNewsService extends NetService {
    private static final String TAG = "NetEaseNewsService";



    public void getNetEaseNews(int page,Callback<NetEaseNews> callback)
    {
       // String url=String.format(Locale.CHINA,NetEase.TOUTAI_URL,page,20);
        String sign="Mi3jS7P4ngUOdV/GUQtfsA4pAaxXio5y4Q7f6L16X0x48ErR02zJ6/KXOnxX046I";
        String url=NetEase.generateParamsUrl(page,sign);
        newRequest(TAG,NetEaseNews.class,url,callback);
    }

    public void getHotComment(String boradId, String docId, int start, int length, Callback<String> callback)
    {
       String url=NetEase.generateHotCommentUrl(boradId,docId,start,length);
       newStringRequest(TAG,url,callback);
    }

    public void getNormalComment(String boradId, String docId, int start, int length, Callback<String> callback)
    {
        String url=NetEase.generateNormalCommentUrl(boradId,docId,start,length);
        newStringRequest(TAG,url,callback);
    }
}
