package learn.example.pile.observer;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Arrays;

import learn.example.net.OkHttpRequest;
import learn.example.pile.util.VideoParser;
import okhttp3.Request;
import okhttp3.Response;
import rx.Notification;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created on 2016/7/6.
 */
public class VideoUrlParseObserver{

    public static final String USER_AGENT="Mozilla/5.0 (Linux; U; Android 4.0.3; ko-kr; LG-L160L Build/IML74K) AppleWebkit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30";


    private String [] array;

    public VideoUrlParseObserver(String[] urls)
    {
        array=urls;
    }

    public Subscription subscriber(final Subscriber<String[]> subscriber)
    {
       return Observable.from(array).filter(new Func1<String, Boolean>() {
           @Override
           public Boolean call(String s) {
               return s.contains("weibo.com")||s.contains("miaopai.com");
           }
       }).map(new Func1<String, String[]>() {
           @Override
           public String[] call(String s) {
               String [] result=startParse(s);
               String [] newArr=Arrays.copyOf(result,3);
               newArr[2]=s;
               return newArr;
           }
       }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private String[] startParse(String srcUrl)
    {
       boolean fromWeibo=srcUrl.contains("weibo.com");
       boolean fromMiaopai=srcUrl.contains("miaopai.com");
        String[] result=new String[2];
        try {
            Response res=OkHttpRequest.getInstanceUnsafe().syncRequest(generateRequest(srcUrl));
            if (res.isSuccessful()) {
                String html=res.body().string();
                if (!TextUtils.isEmpty(html))
                {
                    if (fromWeibo) {
                        result[0] = VideoParser.getWeiboVideoFileUrl(html);
                        result[1] = VideoParser.getWeiboVideoImgUrl(html);
                    }else if (fromMiaopai)
                    {
                        result[0]= VideoParser.getMPVidFileUrlFromNet(html);
                        result[1]=VideoParser.getMPVidImgUrlFromNet(html);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Request generateRequest(String url)
    {
        Request request=new Request.Builder().url(url).addHeader("User-Agent",USER_AGENT).build();
        return request;
    }


}
