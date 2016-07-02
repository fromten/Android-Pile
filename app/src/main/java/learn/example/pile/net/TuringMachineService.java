package learn.example.pile.net;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.MyURI;
import learn.example.pile.jsonobject.TuringMachineJson;
import okhttp3.Request;

/**
 * Created on 2016/7/2.
 */
public class TuringMachineService extends Service<TuringMachineJson>{


    public static final String KEY="879a6cb3afb84dbf4fc84a1df2ab7319";
    public static final String USERID="eb2edb736";
    public static final String URL="http://apis.baidu.com/turing/turing/turing";

    public TuringMachineService(ServiceListener<TuringMachineJson> listener) {
        super(listener);
    }

    public void getMessage(String question)
    {
        String url=getUrl(question);
        if (url == null) {
            failure("url encode fail,request TuringMachineService question="+question);
        }else {
            Request req=new Request.Builder().url(url).addHeader("apikey",MyURI.API_KEY).build();
            OkHttpRequest.getInstance().newGsonRequest(TuringMachineJson.class, req, new OkHttpRequest.RequestCallback<TuringMachineJson>() {
                @Override
                public void onSuccess(TuringMachineJson res) {
                    success(res);
                }

                @Override
                public void onFailure(String msg) {
                     failure(msg);
                }
            });
        }
    }

    /**
     * 生成完整的请求Url
     * @param info 所需要的消息
     * @return 完整的Url
     */
    private   String getUrl(String info)
    {
        try {
            String encodeInfo= URLEncoder.encode(info,"UTF-8");
            String url=URL+"?"+"key="+KEY+"&"+"info="+encodeInfo+"&"+"userid="+USERID;
            return url;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
