package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import learn.example.pile.adapters.ZhiHuMsgListAdapter;
import learn.example.pile.jsonbean.ZhihuStories;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuStoryService;
import learn.example.pile.object.Zhihu;

/**
 * Created on 2016/6/3.
 */
public class ReadListFragment extends BaseListFragment implements IService.Callback<ZhihuStories> {


    private static final String KEY_DATE="date";

    private ZhiHuMsgListAdapter mAdapter;


    private ZhihuStoryService mService;
    private String date;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mAdapter = new ZhiHuMsgListAdapter();
        setAdapter(mAdapter);
        mService=new ZhihuStoryService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getStories(this);
        }else {
            date=savedInstanceState.getString(KEY_DATE);
        }
    }

    //覆盖父类方法,取消显示装饰器
    @Override
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
         //no show ItemDecoration
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_DATE,date);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(ZhihuStories data) {
        date=data.getDate();
        mAdapter.addAll(Zhihu.valueOf(data));
        notifySuccess();
    }

    @Override
    public void onFailure(String msg) {
         notifyError();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        mService.getStories(this);
    }

    @Override
    public void onLoadMore() {
         correctGet();
    }

    private void correctGet()
    {
        if (date==null)
        {
            mService.getStories(this);
        }else {
            mService.getStoriesFromDate(date,this);
        }
    }
}
