package learn.example.pile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.database.JokeDatabase;
import learn.example.pile.jsonobject.JokeJsonData;
import learn.example.pile.net.GsonRequest;
import learn.example.pile.database.DatabaseManager;
import learn.example.pile.net.UrlRequestManager;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends RecyclerViewFragment implements Response.ErrorListener,
        Response.Listener<JokeJsonData>{

    private JokeListAdapter mJokeListAdapter;

    private JokeDatabase mJokeDataBase;

    private RequestQueue mRequestQueue;
    public final static String JOKE_HISTORY_PAGE_KEY="JOKE_HISTORY_PAGE_KEY";//最后页数KEY

    private final static String JOKE_SAVE_STATE_KEY="JOKE_SAVE_STATE_KEY";//保存状态KEY

    private int currentDataBasePage=0;//现在页数

    private String TAG="JokeListFragment";
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mJokeListAdapter=new JokeListAdapter(getContext());
        setRecyclerAdapter(mJokeListAdapter);
        mRequestQueue=UrlRequestManager.getInstance(getContext()).getRequestQueue();
        if(savedInstanceState!=null)
        {
            List<JokeJsonData.JokeResBody.JokeItem> datas=savedInstanceState.getParcelableArrayList(JOKE_SAVE_STATE_KEY);
             if(datas!=null)
              mJokeListAdapter.addAllItem(datas);
        }else{
            correctRequestData();
        }

    }

    @Override
    public void onDestroy() {
        if(mJokeDataBase!=null)
        {
            mJokeDataBase.close();
        }
        mJokeListAdapter=null;
        mRequestQueue.cancelAll(TAG);
        super.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(JOKE_SAVE_STATE_KEY, (ArrayList<? extends Parcelable>) mJokeListAdapter.getAllItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        setRefreshing(false);
    }
    @Override
    public void onResponse(JokeJsonData response) {
          if(response!=null&&response.getResCode()==0)
          {
              mJokeListAdapter.addAllItem(response.getResBody().getJokeContentList());
              saveLastHistoryPage(response.getResBody().getCurrentPage());//保存请求页数
              saveToDatabase(response.getResBody().getJokeContentList());//保存到数据库
          }
         setRefreshing(false);
    }

    public void correctRequestData()
    {   setRefreshing(true);
        if(checkNetState())//如果网络可用请求网络数据
        {
            int page=readLastHistoryPage()+1;
            requestImgJoke(page);//加载文本
            requestTextJoke(page);//加载图片
        }else//否则加载本地数据库
        {
            loadLocalData();
            setRefreshing(false);
        }
    }

    @Override
    public void pullUpRefresh() {
        correctRequestData();
    }

    @Override
    public void pullDownRefresh() {
        mJokeListAdapter.clearItem();
        correctRequestData();
    }

    public void loadLocalData()
    {
        if(mJokeDataBase==null)
        {
            mJokeDataBase= DatabaseManager.openJokeDatabase(getContext());
        }
        List<JokeJsonData.JokeResBody.JokeItem> data=mJokeDataBase.readJoke(currentDataBasePage,10);
        if(data!=null&&!data.isEmpty())
        {
            mJokeListAdapter.addAllItem(mJokeDataBase.readJoke(currentDataBasePage,10));
            currentDataBasePage=currentDataBasePage+10;
        }

    }
    public void saveToDatabase(List<JokeJsonData.JokeResBody.JokeItem> data)
    {
       if(mJokeDataBase==null)
       {
           mJokeDataBase=DatabaseManager.openJokeDatabase(getContext());
       }
        mJokeDataBase.saveJoke(data);
    }


    //执行网络数据获取
    public void requestImgJoke(int page)
    {
         String imgurl=MyURI.IMAGE_JOKE_REQUEST_URL+"?page="+page;
         GsonRequest reqImg=new GsonRequest(imgurl,JokeJsonData.class,this,this);
         reqImg.setTag(TAG);
         mRequestQueue.add(reqImg);
    }

    public void requestTextJoke(int page)
    {
        String url=MyURI.TEXT_JOKE_REQUEST_URL+"?page="+page;
        GsonRequest request=new GsonRequest(url,JokeJsonData.class,this,this);
        request.setTag(TAG);
        mRequestQueue.add(request);
    }

    //保存最后历史页数
    public void saveLastHistoryPage(int page)
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        p.edit().putInt(JOKE_HISTORY_PAGE_KEY,page).apply();
    }

    //读取最后历史页数
    public int readLastHistoryPage()
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        return p.getInt(JOKE_HISTORY_PAGE_KEY,1);
    }


}
