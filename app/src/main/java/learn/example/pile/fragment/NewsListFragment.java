package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import learn.example.net.Service;
import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.jsonbean.NewsJsonData;
import learn.example.pile.net.GsonService;
import learn.example.pile.net.NewsService;
import learn.example.uidesign.CommonRecyclerView;

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
        setLayoutManager(new LinearLayoutManager(getContext()));
        mService=new NewsService();
        if (savedInstanceState==null)
        {
            showRefreshProgressbar();
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
         notifyLoadSuccess();
         mNewsListAdapter.addAll(data.getNewsItemList());
    }

    @Override
    public void onFailure(String msg) {
        notifyLoadError();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
        mNewsListAdapter.clear();
        mService.getNews(this);
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        mService.getNews(this);
    }
}
