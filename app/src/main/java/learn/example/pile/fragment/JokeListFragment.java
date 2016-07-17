package learn.example.pile.fragment;

import android.os.Bundle;
import android.view.View;

import java.util.List;

import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.database.JokeDatabase;
import learn.example.pile.jsonbean.JokeJsonData;
import learn.example.pile.database.DatabaseManager;
import learn.example.pile.net.IService;
import learn.example.pile.net.JokeService;
import learn.example.pile.util.AccessAppDataHelper;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends BaseListFragment implements IService.Callback<JokeJsonData> {

    private JokeListAdapter mJokeListAdapter;

    private JokeDatabase mJokeDataBase;

    private int currentDataBasePage=0;//现在页数

    private JokeService mJokeService;

    private final String TAG="JokeListFragment";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view,savedInstanceState);
         mJokeListAdapter=new JokeListAdapter();
         setAdapter(mJokeListAdapter);
         currentDataBasePage=AccessAppDataHelper.readInteger(getActivity(),AccessAppDataHelper.KEY_JOKE_PAGE,1);
          mJokeService=new JokeService();
         if (savedInstanceState==null)
         {
             setRefreshing(true);
             mJokeService.getImageJoke(currentDataBasePage,this);
             mJokeService.getTextJoke(currentDataBasePage,this);
         }
    }

    @Override
    public void onDestroy() {
        mJokeService.cancelAll();
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
        if (data==null||data.getResCode()!=0)
        {
            notifyError();
            return;
        }

        mJokeListAdapter.addAll(data.getResBody().getJokeContentList());

         ++currentDataBasePage;
        //将数据保存到数据库
        saveToDatabase(data.getResBody().getJokeContentList());
        notifySuccess();

    }

    @Override
    public void onFailure(String msg) {
         notifyError();
    }

    @Override
    public void onRefresh() {
         mJokeListAdapter.clear();
         requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    public void requestData()
    {
        if (currentDataBasePage%2==0)
        {
            mJokeService.getTextJoke(currentDataBasePage,this);
        }else {
            mJokeService.getImageJoke(currentDataBasePage,this);
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
