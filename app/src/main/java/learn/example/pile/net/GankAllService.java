package learn.example.pile.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.MyURI;
import learn.example.pile.jsonbean.GankCommonJson;
import okhttp3.Request;

/**
 * Created on 2016/7/2.
 */
public class GankAllService extends GsonService{
    private static final String TAG = "GankAllService";

    public void getAndroidPostList(int page,int num,Callback<GankAllService> callback)
    {
        String url=generateUrl("Android",page,num);
        if (url==null)
        {
            callback.onFailure("request gank android  url encode error");
        }else {
            Request req=new Request.Builder().url(url).build();
            performRequest(req,callback);
        }
    }



    private void performRequest(Request request,Callback<GankAllService> callback)
    {
        if (request==null){
            throw  new NullPointerException("No can perform request,Request must is valid object");
        }else{
           newRequest(TAG,GankAllService.class,request,callback);
        }
    }


    public static String generateUrl(String type,int page,int num)
    {
        try {
            type= URLEncoder.encode(type,"utf-8");
            String url= MyURI.GANK_DATA_REQUEST_URL+type+"/"+num+"/"+page;
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
