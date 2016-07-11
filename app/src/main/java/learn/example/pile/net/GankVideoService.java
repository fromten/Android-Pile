package learn.example.pile.net;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.jsonbean.GankCommonJson;
import learn.example.pile.jsonbean.VideoJsonData;
import okhttp3.Request;

/**
 * Created on 2016/7/2.
 */
public class GankVideoService extends GsonService {

    private static final String TAG = "GankVideoService";


    public void getVideo(int page, IService.Callback<VideoJsonData> callback)
    {
       getVideo(page,5,callback);
    }
    public void getVideo(int page, int num, IService.Callback<VideoJsonData> callback)
    {
        String url=GankAllService.generateUrl("休息视频",page,num);
        if (url==null)
        {
            callback.onFailure("encode fail in getVideo from the GamlVideoService");
        }else {
            Request request=new Request.Builder().url(url).build();
            newRequest(TAG,VideoJsonData.class,request,callback);
        }
    }


}
