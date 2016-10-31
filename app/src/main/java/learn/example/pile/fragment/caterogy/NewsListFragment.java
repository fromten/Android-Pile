package learn.example.pile.fragment.caterogy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.database.AADatabaseStore;
import learn.example.pile.database.DatabaseStore;
import learn.example.pile.fragment.base.PersistentFragment;
import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.net.NetService;
import learn.example.pile.net.NetEaseNewsService;
import learn.example.pile.util.DeviceInfo;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends PersistentFragment implements NetService.Callback<NetEaseNews>{

     private static final String STATE_REQUEST_INDEX ="STATE_REQUEST_INDEX";

    private NewsListAdapter mNewsListAdapter;
    private NetEaseNewsService mService;

    public static final int MAX_REQUEST_NUMBER =20;

    private int mRequestIndex;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsListAdapter=new NewsListAdapter();
        setAdapter(mNewsListAdapter);
        mService=new NetEaseNewsService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getNetEaseNews(mRequestIndex,this);
        }else {
            mRequestIndex =savedInstanceState.getInt(STATE_REQUEST_INDEX);
        }
    }

    @Override
    public DatabaseStore<?, ?> onCreateDataStore() {
        return new AADatabaseStore(NetEaseNews.NewsItem.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_REQUEST_INDEX, mRequestIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(NetEaseNews data) {
        if (isRefreshing())
        {
            mNewsListAdapter.clear();
        }

        mRequestIndex += MAX_REQUEST_NUMBER;
        mNewsListAdapter.addAll(data.getNewsItems());
        notifySuccess();

        saveDataToDB(data.getNewsItems());
    }

    @Override
    public void onFailure(String msg) {
        notifyError();

        //当前adapter没有展现任何数据
        if (mNewsListAdapter.getItemCount()<=0)
        {
            addDBItemsToAdapter();
        }
    }

    @Override
    public void onRefresh() {
        if (DeviceInfo.checkNetConnected(getContext()))
        {
            mService.getNetEaseNews(mRequestIndex,this);
        }else {
            if (mNewsListAdapter.getItemCount()<=0)
            {
                addDBItemsToAdapter();
                notifySuccess();//清除刷新状态
            }else {
                clearRequestState();
            }
        }
    }

    @Override
    public void onLoadMore() {
        if (DeviceInfo.checkNetConnected(getContext()))
        {
            mService.getNetEaseNews(mRequestIndex,this);
        }else {
            addDBItemsToAdapter();
            notifySuccess();
        }
    }



}
