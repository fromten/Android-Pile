package learn.example.pile.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import learn.example.net.Service;
import learn.example.pile.adapters.NewsListAdapter;
import learn.example.pile.jsonobject.NewsJsonData;
import learn.example.pile.net.NewsService;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/5/7.
 */
public class NewsListFragment extends BaseListFragment implements Service.ServiceListener<NewsJsonData>{


    private NewsListAdapter mNewsListAdapter;
    private NewsService mService;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNewsListAdapter=new NewsListAdapter();
        setAdapter(mNewsListAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        mService=new NewsService(this);
        if (savedInstanceState==null)
        {
            showRefreshProgressbar();
            mService.getNews();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mService.setListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mService.removeListener(this);
    }

    @Override
    public void onSuccess(NewsJsonData data) {
         mNewsListAdapter.addAll(data.getNewsItemList());
         hideRefreshProgressbar();
    }

    @Override
    public void onFailure(String msg) {
        notifyLoadError();
        hideRefreshProgressbar();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
        mNewsListAdapter.clear();
        mService.getNews();
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        mService.getNews();
    }
}
