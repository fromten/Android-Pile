package learn.example.pile.fragment.caterogy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import learn.example.pile.adapters.ZhiHuStoriesListAdapter;
import learn.example.pile.fragment.base.BaseListFragment;
import learn.example.pile.jsonbean.ZhihuStories;
import learn.example.pile.net.IService;
import learn.example.pile.net.ZhihuStoryService;

/**
 * Created on 2016/6/3.
 */
public class ReadListFragment extends BaseListFragment implements IService.Callback<ZhihuStories> {


    private static final String STATE_DATE ="STATE_DATE";

    private ZhiHuStoriesListAdapter mAdapter;


    private ZhihuStoryService mService;
    private String date;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mAdapter = new ZhiHuStoriesListAdapter();
        setAdapter(mAdapter);
        mService=new ZhihuStoryService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getStories(this);
        }else {
            date=savedInstanceState.getString(STATE_DATE);
            mAdapter.setDate(formatDate(date));
        }
    }

    //覆盖父类方法,取消显示装饰器
    @Override
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
         //no show ItemDecoration
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_DATE,date);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }

    @Override
    public void onSuccess(ZhihuStories data) {
        if (isRefreshing())
        {
            mAdapter.clear();
        }

        date=data.getDate();

        mAdapter.setDate(formatDate(date));
        mAdapter.addAll(data.getStories());
        notifySuccess();
    }

    //只能传递 ZhihuStories.date 网络得到的日期
    private String formatDate(String date)
    {
        if (date==null)
        {
            return null;
        }
        return date.substring(0,4)+"-"+date.substring(4,6)+"-"+date.substring(6);
    }

    @Override
    public void onFailure(String msg) {
         notifyError();
    }

    @Override
    public void onRefresh() {
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
