package learn.example.pile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import learn.example.joke.R;
import learn.example.pile.MyURI;
import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.database.JokeDatabase;
import learn.example.pile.jsonobject.JokeJsonData;
import learn.example.pile.net.GsonRequest;
import learn.example.pile.database.DatabaseManager;
import learn.example.pile.net.VolleyRequestQueue;
import learn.example.pile.util.AccessAppDataHelper;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends RecyclerViewFragment implements Response.ErrorListener,
        Response.Listener<JokeJsonData>{

    private JokeListAdapter mJokeListAdapter;

    private JokeDatabase mJokeDataBase;

    public final static String KEY_JOKE_SAVE_STATE ="KEYJOKESAVESTATE";//保存状态KEY

    private int currentDataBasePage=0;//现在页数

    private final String TAG="JokeListFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mJokeListAdapter=new JokeListAdapter(getContext());
        setRecyclerAdapter(mJokeListAdapter);
        if(savedInstanceState!=null)
        {
            List<JokeJsonData.JokeResBody.JokeItem> datas=savedInstanceState.getParcelableArrayList(KEY_JOKE_SAVE_STATE);
             if(datas!=null)
              mJokeListAdapter.addAllItem(datas);
        }else{
            startRefresh();
            correctRequestData();
        }
    }

    @Override
    public DividerItemDecoration getDividerItemDecoration() {
        Drawable drawable= ContextCompat.getDrawable(getContext(),R.drawable.joke_divider);
        return new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL_LIST,drawable);
    }


    @Override
    public void onDestroy() {
        mJokeListAdapter=null;
        if(mJokeDataBase!=null)
        {
            mJokeDataBase.close();
        }
        VolleyRequestQueue.getInstance(getContext()).cancelAll(TAG);
        super.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(KEY_JOKE_SAVE_STATE, (ArrayList<? extends Parcelable>) mJokeListAdapter.getAllItem());
        super.onSaveInstanceState(outState);
    }

    @Override
    public  void onErrorResponse(VolleyError error) {
        refreshFail();
        if (mJokeListAdapter.getItemSize()==0)
        {
            setEmptyViewText("数据飞走了");
        }
    }
    @Override
    public  void onResponse(JokeJsonData response) {
          if(response!=null&&response.getResCode()==0)
          {
              mJokeListAdapter.addAllItem(response.getResBody().getJokeContentList());

              //将页数保存到内部数据中
              int page=response.getResBody().getCurrentPage();
              AccessAppDataHelper.saveInteger(getActivity(),AccessAppDataHelper.KEY_JOKE_PAGE,page);

              //将数据保存到数据库
              saveToDatabase(response.getResBody().getJokeContentList());
          }

         refreshComplete();
    }

    public void correctRequestData()
    {
        setEmptyViewText(null);
        if (checkNetState())
        {
            //读取页数
            int page=AccessAppDataHelper.readInteger(getActivity(),AccessAppDataHelper.KEY_JOKE_PAGE)+1;
            requestImgJoke(page);//加载文本
            requestTextJoke(page);//加载图片
        }else {
            loadLocalData();
            refreshComplete();
        }

    }

    @Override
    public void pullUpRefresh() {
         correctRequestData();
    }

    @Override
    public void pullDownRefresh() {
        startRefresh();
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
         GsonRequest<JokeJsonData> reqImg=new GsonRequest<>(imgurl,JokeJsonData.class,this,this,true);
         reqImg.setTag(TAG);
         VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(reqImg);
    }

    public void requestTextJoke(int page)
    {
        String url=MyURI.TEXT_JOKE_REQUEST_URL+"?page="+page;
        GsonRequest<JokeJsonData> requestText=new GsonRequest<>(url,JokeJsonData.class,this,this,true);
        requestText.setTag(TAG);
        VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(requestText);
    }






}
