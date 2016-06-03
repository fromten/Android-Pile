package learn.example.pile.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.jsonobject.NewsJsonData;
import learn.example.pile.net.GsonRequest;
import learn.example.pile.net.NetRequestQueue;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends RecyclerViewFragment implements Response.Listener<NewsJsonData>,
 Response.ErrorListener{


    private NewsListAdapter mNewsListAdapter;
    private RequestQueue mRequestQueue;
    private String TAG="NewsListFragment";
    public static final String KEY_NEWS_SAVE_STATE="KEY_NEWS_SAVE_STATE";
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsListAdapter=new NewsListAdapter(getContext());
        setRecyclerAdapter(mNewsListAdapter);
        mRequestQueue= NetRequestQueue.getInstance(getContext()).getRequestQueue();
        if (savedInstanceState!=null)
        {
            List<NewsJsonData.NewsItem> savedata=savedInstanceState.getParcelableArrayList(KEY_NEWS_SAVE_STATE);
            mNewsListAdapter.addAllItem(savedata);
        }else {
            correctReqData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_NEWS_SAVE_STATE, (ArrayList<? extends Parcelable>) mNewsListAdapter.getAllItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mRequestQueue.cancelAll(TAG);
        mRequestQueue=null;
        mNewsListAdapter=null;
        super.onDestroy();
    }

    @Override
    public void onResponse(NewsJsonData response) {
         if(response!=null&&response.getErrNum()<=0)
         {
             mNewsListAdapter.addAllItem(response.getNewsItemList());
         }
         stopRefresh();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG,error.getMessage());
        stopRefresh();
    }

    @Override
    public void pullUpRefresh() {
        correctReqData();
    }

    @Override
    public void pullDownRefresh() {
        mNewsListAdapter.clearItem();
        correctReqData();
    }


    public void correctReqData()
    {
        startRefresh();
        if(checkNetState())
        {
            requestNews();
        }else
        {
            if(mNewsListAdapter.getSelfItemSize()==0)
            {
                setEmptyViewText("网络请求失败");
            }
            stopRefresh();
        }
    }
    public void requestNews()
    {
        GsonRequest<NewsJsonData> request=new GsonRequest<>
                (MyURI.NEW_DATA_REQUEST_URL,NewsJsonData.class,this,this);
        request.setTag(TAG);
        mRequestQueue.add(request);
    }


}
