package learn.example.pile.net;

import android.content.Context;

import com.google.gson.JsonObject;

import learn.example.net.OkHttpRequest;
import learn.example.net.Service;
import learn.example.pile.jsonbean.TuringMachineJson;
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

            Request req=new Request.Builder().url(URL).post(body).build();
            OkHttpRequest.getInstanceUnsafe().newGsonRequest(TuringMachineJson.class, req, new OkHttpRequest.RequestCallback<TuringMachineJson>() {
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

    /**
     *
     * @param info 询问的问题
     * @return RequestBody 如果成功
     */
    private  RequestBody getRequestBody(String info)
    {
        JsonObject object=new JsonObject();
        object.addProperty("key",APi_KEY);
        object.addProperty("info",info);
        object.addProperty("userid",USER_ID);
        return RequestBody.create(MediaType.parse("text"),object.toString());
    }

}
