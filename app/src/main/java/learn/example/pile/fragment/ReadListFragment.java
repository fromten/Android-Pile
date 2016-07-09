package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import learn.example.net.Service;
import learn.example.pile.adapters.ZhiHuMsgListAdapter;
import learn.example.pile.jsonbean.ZhihuStories;
import learn.example.pile.net.ZhihuStoryService;
import learn.example.pile.object.Zhihu;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/6/3.
 */
public class ReadListFragment extends BaseListFragment implements Service.ServiceListener<ZhihuStories>{



    private ZhiHuMsgListAdapter mAdapter;
    private static final String TAG="ReadListFragment";

    private ZhihuStoryService mService;
    private String date;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new ZhiHuMsgListAdapter();
        setAdapter(mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        mService=new ZhihuStoryService(this);
        if (savedInstanceState==null)
        {
            showRefreshProgressbar();
            mService.getStories();
        }
    }

    //覆盖父类方法,取消显示装饰器
    @Override
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
         //no show ItemDecoration
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
    public void onSuccess(ZhihuStories data) {
        date=data.getDate();
        mAdapter.addAll(Zhihu.valueOf(data));
        hideRefreshProgressbar();
    }

    @Override
    public void onFailure(String msg) {
        hideRefreshProgressbar();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
        mAdapter.clear();
        mService.getStoriesAtTime(date);
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        mService.getStoriesAtTime(date);
    }
}
