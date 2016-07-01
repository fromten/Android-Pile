package learn.example.pile.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
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
import learn.example.pile.net.JokeService;
import learn.example.pile.net.VolleyRequestQueue;
import learn.example.pile.util.AccessAppDataHelper;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends BaseListFragment implements JokeService.ServiceListener<JokeJsonData>{

    private JokeListAdapter mJokeListAdapter;

    private JokeDatabase mJokeDataBase;

    private int currentDataBasePage=0;//现在页数

    private JokeService mJokeService;

    private final String TAG="JokeListFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
         mJokeListAdapter=new JokeListAdapter();
         setAdapter(mJokeListAdapter);
         setLayoutManager(new LinearLayoutManager(getContext()));
         currentDataBasePage=AccessAppDataHelper.readInteger(getActivity(),AccessAppDataHelper.KEY_JOKE_PAGE);
          mJokeService=new JokeService(this);
         if (savedInstanceState==null)
         {
             showRefreshProgressbar();
             mJokeService.getImageJoke(currentDataBasePage);
             mJokeService.getTextJoke(currentDataBasePage);
         }
    }

    @Override
    public void onDestroy() {
        AccessAppDataHelper.saveInteger(getActivity(),AccessAppDataHelper.KEY_JOKE_PAGE,currentDataBasePage);
        mJokeListAdapter=null;
        if(mJokeDataBase!=null)
        {
            mJokeDataBase.close();
        }
        super.onDestroy();
    }

    @Override
    public void onSuccess(JokeJsonData data) {

        mJokeListAdapter.addAll(data.getResBody().getJokeContentList());


        int page=data.getResBody().getCurrentPage();
        currentDataBasePage=page+1;

        //将数据保存到数据库
        saveToDatabase(data.getResBody().getJokeContentList());

        hideRefreshProgressbar();
    }

    @Override
    public void onFailure(String msg) {
         hideRefreshProgressbar();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
         mJokeListAdapter.clear();
         mJokeService.getTextJoke(currentDataBasePage);
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        mJokeService.getTextJoke(currentDataBasePage);
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
            mJokeListAdapter.addAll(mJokeDataBase.readJoke(currentDataBasePage,10));
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
}
