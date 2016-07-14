package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import learn.example.net.Service;
import learn.example.pile.adapters.ZhiHuMsgListAdapter;
import learn.example.pile.jsonbean.ZhihuStories;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuStoryService;
import learn.example.pile.object.Zhihu;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/6/3.
 */
public class ReadListFragment extends BaseListFragment implements IService.Callback<ZhihuStories> {



    private ZhiHuMsgListAdapter mAdapter;
    private static final String TAG="ReadListFragment";

    private ZhihuStoryService mService;
    private String date;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new ZhiHuMsgListAdapter();
        setAdapter(mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        mService=new ZhihuStoryService();
        if (savedInstanceState==null)
        {
            showRefreshProgressbar();
            mService.getStories(this);
        }
    }

    //覆盖父类方法,取消显示装饰器
    @Override
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
         //no show ItemDecoration
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(ZhihuStories data) {
        notifyLoadSuccess();

        date=data.getDate();
        mAdapter.addAll(Zhihu.valueOf(data));
        hideRefreshProgressbar();
    }

    @Override
    public void onFailure(String msg) {
        Log.d(TAG,msg );
        notifyLoadError();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
        mAdapter.clear();
        mService.getStories(this);
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        mService.getStoriesAtTime(date,this);
    }
}
