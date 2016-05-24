package learn.example.pile.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.adapters.JokeListAdapter;
import learn.example.pile.database.JokeDataBase;
import learn.example.pile.jsonobject.BaseJokeData;
import learn.example.pile.net.JokeRequestTask;
import learn.example.pile.database.DataBaseManager;

/**
 * Created on 2016/5/5.
 */
public class JokeListFragment extends RecyclerViewFragment implements JokeRequestTask.TaskCompleteListener {

    private JokeListAdapter mJokeListAdapter;

    private JokeRequestTask mRequestTask;

    private JokeDataBase mJokeDataBase;

    public final static String HISTORY_PAGE_KEY="HISTORY_PAGE_KEY";//最后页数KEY

    private final static String SAVE_STATE_KEY="SAVE_STATE_KEY";//保存状态KEY

    private int currentDataBasePage=0;//现在页数

    private String TAG="JokeFragemnt";
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mJokeListAdapter=new JokeListAdapter(getContext());
        setRecyclerAdapter(mJokeListAdapter);
        mJokeDataBase=DataBaseManager.openJokeDataBase(getActivity());
        if(savedInstanceState!=null)
        {
            List<BaseJokeData> datas=savedInstanceState.getParcelableArrayList(SAVE_STATE_KEY);
            mJokeListAdapter.addAllItem(datas);
        }else{
            correctRequestData();
        }

    }

    @Override
    public void onResume() {
        if(mRequestTask!=null)
         mRequestTask.setTaskCompleteListener(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        if(mRequestTask!=null)
        mRequestTask.setTaskCompleteListener(null);
        super.onPause();
    }
    @Override
    public void onDestroy() {
        mJokeDataBase.close();
        super.onDestroy();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SAVE_STATE_KEY, (ArrayList<? extends Parcelable>) mJokeListAdapter.getAllItem());
        super.onSaveInstanceState(outState);
    }
    @Override
    public void taskComplete(List<BaseJokeData> data) {
        mJokeListAdapter.addAllItem(data);
        Log.i(TAG, String.valueOf(data.get(0).getCurrentPage()));
        saveLastHistoryPage(data.get(0).getCurrentPage());//页数保存到文本
        mJokeDataBase.saveJoke(data);//数据保存的数据库
        setRefreshing(false);
    }
    @Override
    public void taskFail(String msg) {
         Log.e(TAG,msg);
        setRefreshing(false);
    }

    public void correctRequestData()
    {   setRefreshing(true);
        if(checkNetState())//如果网络可用请求网络数据
        {
            requestData(readLastHistoryPage()+1);
        }else//否则加载本地数据库
        {
            loadLocalData();
            setRefreshing(false);
        }
    }

    @Override
    public void pullUpRefresh(RecyclerView recyclerView) {
        correctRequestData();
    }

    @Override
    public void onRefresh() {
        mJokeListAdapter.clearItem();
        correctRequestData();
    }

    public void loadLocalData()
    {
        if(mJokeDataBase==null)
            return;
        List<BaseJokeData> list=mJokeDataBase.readJoke(currentDataBasePage,currentDataBasePage+10);
        mJokeListAdapter.addAllItem(list);
        currentDataBasePage=currentDataBasePage+10;
    }



    //执行网络数据获取
    public void requestData(int page)
    {
        mRequestTask=new JokeRequestTask();
        mRequestTask.setTaskCompleteListener(this);
        String texturl=MyURI.TEXT_JOKE_REQUEST_URL+"?page="+page;
        String imgurl=MyURI.IMAGE_JOKE_REQUEST_URL+"?page="+page;
        mRequestTask.execute(texturl,imgurl);
    }

    //保存最后历史页数
    public void saveLastHistoryPage(int page)
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        p.edit().putInt(HISTORY_PAGE_KEY,page).apply();
    }

    //读取最后历史页数
    public int readLastHistoryPage()
    {
        SharedPreferences p=getActivity().getPreferences(Context.MODE_PRIVATE);
        return p.getInt(HISTORY_PAGE_KEY,1);
    }


}
