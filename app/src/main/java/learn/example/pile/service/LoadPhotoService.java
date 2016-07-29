package learn.example.pile.service;

import android.app.IntentService;
import android.content.Intent;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import learn.example.net.OkHttpRequest;
import learn.example.pile.PhotoActivity;
import learn.example.pile.object.NetEase;
import learn.example.pile.util.GsonHelper;
import okhttp3.Request;

/**
 * Created on 2016/7/28.
 */


public class LoadPhotoService extends IntentService {

    public static final String KEY_SKIPID = "key_skipid";

    //此类作用于NetEase 新闻图片
    public LoadPhotoService()
    {
        super("LoadPhotoService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String skipID=intent.getStringExtra(KEY_SKIPID);
        int line=skipID.indexOf("|");
        String first=skipID.substring(4,line);
        String second=skipID.substring(line+1,skipID.length());
        Request request=new Request.Builder().url(String.format(NetEase.PHOTOS_SET_URL,first,second)).build();
        OkHttpRequest.getInstance(getApplicationContext())
                .newStringRequest(request, new OkHttpRequest.RequestCallback<String>() {
                    @Override
                    public void onSuccess(String res) {
                        JsonObject object=new JsonParser().parse(res).getAsJsonObject();
                        JsonArray array=object.getAsJsonArray("photos");
                        if (array!=null)
                        {
                            int size=array.size();
                            String[] urls=new String[size];
                            for (int i=0;i<size;++i)
                            {
                                JsonObject o= (JsonObject) array.get(i);
                                String string= GsonHelper.getAsString(o.get("imgurl"),null);
                                urls[i]=string;
                            }
                            Intent newIntent=new Intent(getApplicationContext(), PhotoActivity.class);
                            newIntent.putExtra(PhotoActivity.KEY_IMG_URL,urls);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            getApplicationContext().startActivity(newIntent);
                        }

                    }

                    @Override
                    public void onFailure(String msg) {

                    }
                });


    }
}
