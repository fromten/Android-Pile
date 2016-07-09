package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import java.util.List;

import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.database.JokeDatabase;
import learn.example.pile.jsonbean.JokeJsonData;
import learn.example.pile.database.DatabaseManager;
import learn.example.pile.net.JokeService;
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
         currentDataBasePage=AccessAppDataHelper.readInteger(getActivity(),AccessAppDataHelper.KEY_JOKE_PAGE,1);
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

         ++currentDataBasePage;
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
         requestData();
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        requestData();
    }

    public void requestData()
    {
        if (currentDataBasePage%2==0)
        {
            mJokeService.getTextJoke(currentDataBasePage);
        }else {
            mJokeService.getImageJoke(currentDataBasePage);
        }

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
