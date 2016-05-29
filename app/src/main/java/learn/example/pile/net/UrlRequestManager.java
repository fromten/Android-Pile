package learn.example.pile.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created on 2016/5/29.
 */
public class UrlRequestManager {
    private  static UrlRequestManager mManager;
    private RequestQueue mRequestQueue;
    private Context mc;
    private UrlRequestManager(Context context)
    {
        mc=context;
        mRequestQueue=getRequestQueue();
    }
    public static synchronized UrlRequestManager getInstance(Context context)
    {
             if(mManager==null)
             {
                 mManager=new UrlRequestManager(context.getApplicationContext());
             }
        return mManager;
    }
    public RequestQueue getRequestQueue()
    {
        if(mRequestQueue==null)
        {
            mRequestQueue= Volley.newRequestQueue(mc);
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
    public void cancelAll(String tag)
    {
        mRequestQueue.cancelAll(tag);
    }
}
