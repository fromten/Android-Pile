package learn.example.pile.fragment;


import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.jsonobject.NewsJsonData;
import learn.example.pile.net.GsonRequest;
import learn.example.pile.net.VolleyRequestQueue;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends RecyclerViewFragment implements Response.Listener<NewsJsonData>,
 Response.ErrorListener{


    private NewsListAdapter mNewsListAdapter;
    private String TAG="NewsListFragment";
    public static final String KEY_NEWS_SAVE_STATE="KEY_NEWS_SAVE_STATE";
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mNewsListAdapter=new NewsListAdapter(getContext());
        setRecyclerAdapter(mNewsListAdapter);
        if (savedInstanceState!=null)
        {
            List<NewsJsonData.NewsItem> savedata=savedInstanceState.getParcelableArrayList(KEY_NEWS_SAVE_STATE);
            mNewsListAdapter.addAllItem(savedata);
        }else {
            requestData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_NEWS_SAVE_STATE, (ArrayList<? extends Parcelable>) mNewsListAdapter.getAllItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        VolleyRequestQueue.getInstance(getContext()).cancelAll(TAG);
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
        stopRefresh();
        if (mNewsListAdapter.getSelfItemSize()==0)
        {
            setEmptyViewText("数据飞走了");
        }
    }

    @Override
    public void pullUpRefresh() {
        requestData();
    }

    @Override
    public void pullDownRefresh() {
        mNewsListAdapter.clearItem();
        requestData();
    }


    public void requestData()
    {
        startRefresh();
        setEmptyViewText(null);
        requestNews();
    }
    public void requestNews()
    {
        GsonRequest<NewsJsonData> request=new GsonRequest<>
                (MyURI.NEW_DATA_REQUEST_URL,NewsJsonData.class,this,this,true);
        request.setTag(TAG);
        VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(request);
    }


}
