package learn.example.pile.fragment.caterogy;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.fragment.base.BaseListFragment;
import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.net.NetService;
import learn.example.pile.net.NetEaseNewsService;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends BaseListFragment implements NetService.Callback<NetEaseNews>{

     private static final String KEY_PAGE="page";


    private NewsListAdapter mNewsListAdapter;
    private NetEaseNewsService mService;

    private int page;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsListAdapter=new NewsListAdapter();
        setAdapter(mNewsListAdapter);
        mService=new NetEaseNewsService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getNetEaseNews(page,this);
        }else {
            page=savedInstanceState.getInt(KEY_PAGE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_PAGE,page);
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

        page+=20;
        mNewsListAdapter.addAll(data.getT1348647909107());
        notifySuccess();
    }

    @Override
    public void onFailure(String msg) {
        notifyError();
    }

    @Override
    public void onRefresh() {
        mService.getNetEaseNews(page,this);
    }

    @Override
    public void onLoadMore() {
       mService.getNetEaseNews(page,this);
    }
}
