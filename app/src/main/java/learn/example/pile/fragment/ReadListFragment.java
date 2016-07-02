package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import learn.example.pile.adapters.ReadListAdapter;
import learn.example.pile.jsonobject.GankCommonJson;
import learn.example.pile.net.GankAllService;
import learn.example.pile.net.GankVideoService;
import learn.example.pile.util.AccessAppDataHelper;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/6/3.
 */
public class ReadListFragment extends BaseListFragment implements GankVideoService.ServiceListener<GankCommonJson>{



    private ReadListAdapter mAdapter;
    private static final String TAG="ReadListFragment";

    //最大请求个数
    private final int MAX_REQNUM=15;
    //现在页数
    private int currentPage=0;
    private GankAllService mService;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter = new ReadListAdapter();
        setAdapter(mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        mService=new GankAllService(this);
        //读取保存的页数
        currentPage = AccessAppDataHelper.readInteger(getActivity(), AccessAppDataHelper.KEY_READ_PAGE,1);
        if (savedInstanceState==null)
        {
            showRefreshProgressbar();
            mService.getAndroidPostList(currentPage,MAX_REQNUM);
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
    public void onDestroy() {
        AccessAppDataHelper.saveInteger(getActivity(),AccessAppDataHelper.KEY_READ_PAGE,currentPage);
        super.onDestroy();
    }

    @Override
    public void onSuccess(GankCommonJson data) {
        hideRefreshProgressbar();
        mAdapter.addAll(data.getResults());
        currentPage++;
    }

    @Override
    public void onFailure(String msg) {
        hideRefreshProgressbar();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
        mAdapter.clear();
        mService.getAndroidPostList(currentPage,MAX_REQNUM);
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        mService.getAndroidPostList(currentPage,MAX_REQNUM);
    }
}
