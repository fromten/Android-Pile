package learn.example.pile.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.MyURI;
import learn.example.pile.jsonobject.VideoJsonData;
import okhttp3.Request;

/**
 * Created on 2016/7/2.
 */
public class GankVideoService extends Service<VideoJsonData> {


    public GankVideoService(ServiceListener<VideoJsonData> listener) {
        super(listener);
    }

    public void getVideo(int page)
    {
       getVideo(page,5);
    }
    public void getVideo(int page,int num)
    {
        String url=GankAllService.generateUrl("休息视频",page,num);
        if (url==null)
        {
            failure("video url encode error");
        }else {
            Request request=new Request.Builder().url(url).build();
            OkHttpRequest.getInstance().newGsonRequest(VideoJsonData.class, request, new OkHttpRequest.RequestCallback<VideoJsonData>() {
                @Override
                public void onSuccess(VideoJsonData res) {
                    if (res.isError())
                    {
                        failure("video request error");
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


}
