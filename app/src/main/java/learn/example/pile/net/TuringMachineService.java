package learn.example.pile.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.MyURI;
import learn.example.pile.jsonobject.TuringMachineJson;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created on 2016/7/2.
 */
public class TuringMachineService extends Service<TuringMachineJson>{


    public static final String APi_KEY="16c644fa5cd7be8e4d8c22572ba7ef1a";
    public static final String USER_ID="abcd";
    public static final String URL="http://www.tuling123.com/openapi/api";
    public TuringMachineService(ServiceListener<TuringMachineJson> listener) {
        super(listener);
    }

    public void getMessage(String question)
    {
        RequestBody body=getRequestBody(question);
        if (body == null) {
            failure("url encode fail,request TuringMachineService question="+question);
        }else {

            Request req=new Request.Builder().url(URL).post(body).build();
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
     *
     * @param info 询问的问题
     * @return RequestBody 如果成功
     */
    private  RequestBody getRequestBody(String info)
    {
        try {
            String encodeInfo= URLEncoder.encode(info,"UTF-8");
            JsonObject object=new JsonObject();
            object.addProperty("key",APi_KEY);
            object.addProperty("info",encodeInfo);
            object.addProperty("userid",USER_ID);
            return RequestBody.create(MediaType.parse("text"),object.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

}