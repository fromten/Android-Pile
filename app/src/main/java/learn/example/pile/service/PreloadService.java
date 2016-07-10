package learn.example.pile.service;

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;

import learn.example.net.OkHttpRequest;
import learn.example.pile.object.Zhihu;
import okhttp3.Request;

/**
 * Created on 2016/7/10.
 */
public class PreloadService extends IntentService{

    public static final String KEY_ID="key_id";

    public PreloadService() {
        super("PreloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int id=intent.getIntExtra(KEY_ID,0);
        if (id==0)return;

        String url= Zhihu.CONTENT_URL+id;
        Request req=new Request.Builder().url(url).build();
        try {
            OkHttpRequest.getInstanceUnsafe().syncRequest(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
