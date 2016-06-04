package learn.example.pile.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created on 2016/5/29.
 */
public class VolleyRequestQueue {
    private static VolleyRequestQueue mInstance;
    private RequestQueue mRequestQueue;
    private Context mc;
    private VolleyRequestQueue(Context context)
    {
        mc=context;
        mRequestQueue=getRequestQueue();
    }
    public static synchronized VolleyRequestQueue getInstance(Context context)
    {
             if(mInstance==null)
             {
                 mInstance=new VolleyRequestQueue(context.getApplicationContext());
             }
        return mInstance;
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
