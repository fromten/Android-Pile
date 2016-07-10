package learn.example.pile.net;

import android.content.Context;

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
public class GankAllService extends Service<GankCommonJson> {
    public GankAllService(ServiceListener<GankCommonJson> listener) {
        super(listener);
    }

    public void getAndroidPostList(int page,int num)
    {
        String url=generateUrl("Android",page,num);
        if (url==null)
        {
            failure("request gank android  url encode error");
        }else {
            Request req=new Request.Builder().url(url).build();
            performRequest(req);
        }
    }



    private void performRequest(Request request)
    {
        if (request==null){
            throw  new NullPointerException("No can perform request,Request must is valid object");
        }else{
            OkHttpRequest.getInstanceUnsafe().newGsonRequest(GankCommonJson.class, request, new OkHttpRequest.RequestCallback<GankCommonJson>() {
                @Override
                public void onSuccess(GankCommonJson res) {
                    if (res.isError())
                    {
                        failure("Gank data request fail");
                    }else {
                        success(res);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    failure(msg);
                }
            });
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
