package learn.example.pile.fragment.caterogy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.activeandroid.Model;

import java.util.List;

import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.fragment.base.BaseListFragment;
import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.net.NetService;
import learn.example.pile.net.NetEaseNewsService;
import learn.example.pile.util.ActiveAndroidHelper;
import learn.example.pile.util.DeviceInfo;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends BaseListFragment implements NetService.Callback<NetEaseNews>{

     private static final String KEY_REQUEST_INDEX ="requestindex";
     private static final String KEY_DATABASE_INDEX ="databaseindex";
     private static final String TAG = "NewsListFragment";

    private NewsListAdapter mNewsListAdapter;
    private NetEaseNewsService mService;

    public static final int MAX_NUMBER=20;

    private int mRequestIndex;
    private int mDatabaseIndex;

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
            mRequestIndex =savedInstanceState.getInt(KEY_REQUEST_INDEX);
            mDatabaseIndex=savedInstanceState.getInt(KEY_DATABASE_INDEX);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_REQUEST_INDEX, mRequestIndex);
        outState.putInt(KEY_DATABASE_INDEX,mDatabaseIndex);
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

        mRequestIndex +=MAX_NUMBER;
        mNewsListAdapter.addAll(data.getT1348647909107());
        notifySuccess();


        checkDatabaseCapacity();
        Log.d(TAG, "start");
        saveItemsToDatabase(data.getT1348647909107());
        Log.d(TAG,"end" );
    }

    @Override
    public void onFailure(String msg) {
        notifyError();

        //当前adapter没有展现任何数据
        if (mNewsListAdapter.getItemCount()<=0)
        {
            addDatabaseItemsToAdapter(false);
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
                addDatabaseItemsToAdapter(false);
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
            addDatabaseItemsToAdapter(false);
            notifySuccess();
        }
    }

    private void checkDatabaseCapacity()
    {
        int rowCount=ActiveAndroidHelper.rowSize(NetEaseNews.T1348647909107Bean.class);
        Log.d(TAG, String.valueOf(rowCount));
        if (rowCount>=10000)//1w
        {
          ActiveAndroidHelper.deleteAll(NetEaseNews.T1348647909107Bean.class);
        }
    }

    /**
     * 添加元素到adapter
     * @param clearAdapterItems true 清除现在Adapter,在添加元素
     */
    public void addDatabaseItemsToAdapter(boolean clearAdapterItems)
    {
        if (clearAdapterItems)mNewsListAdapter.clear();

        List<NetEaseNews.T1348647909107Bean> items = ActiveAndroidHelper
                                .getItemsFromDatabase(NetEaseNews.T1348647909107Bean.class,true,
                                                      mDatabaseIndex,MAX_NUMBER);
        if (items!=null&&items.size()>0)
        {
            mNewsListAdapter.addAll(items);
            mDatabaseIndex+=items.size();
        }
    }

    public boolean saveItemsToDatabase(List<? extends Model> items)
    {
       return ActiveAndroidHelper.saveItemsToDatabase(items);
    }
}
