package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.jsonbean.NewsJsonData;
import learn.example.pile.net.GsonService;
import learn.example.pile.net.NewsService;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends BaseListFragment implements GsonService.Callback<NewsJsonData>{


    private NewsListAdapter mNewsListAdapter;
    private NewsService mService;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsListAdapter=new NewsListAdapter();
        setAdapter(mNewsListAdapter);
        mService=new NewsService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getNews(this);
        }
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(NewsJsonData data) {
         mNewsListAdapter.addAll(data.getNewsItemList());
         notifySuccess();
    }

    @Override
    public void onFailure(String msg) {
        notifyError();
    }

    @Override
    public void onRefresh() {
        mNewsListAdapter.clear();
        mService.getNews(this);
    }

    @Override
    public void onLoadMore() {
        mService.getNews(this);
    }
}
