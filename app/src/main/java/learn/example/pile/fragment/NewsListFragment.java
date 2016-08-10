package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.jsonbean.NetEaseNews;
import learn.example.pile.net.GsonService;
import learn.example.pile.net.NetEaseNewsService;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends BaseListFragment implements GsonService.Callback<NetEaseNews>{

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
        mNewsListAdapter.clear();
        mService.getNetEaseNews(page,this);
    }

    @Override
    public void onLoadMore() {
       mService.getNetEaseNews(page,this);
    }
}
