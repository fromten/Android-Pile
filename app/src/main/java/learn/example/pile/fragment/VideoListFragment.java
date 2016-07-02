package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.jsonobject.VideoJsonData;
import learn.example.pile.net.GankVideoService;
import learn.example.pile.net.VideoHtmlParser;
import learn.example.pile.util.AccessAppDataHelper;
import learn.example.uidesign.CommonRecyclerView;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends BaseListFragment implements GankVideoService.ServiceListener<VideoJsonData>{

    private VideoListAdapter mAdapter;
    private VideoHtmlParser mVideoHtmlParser;
    private static final String TAG="VideoListFragment";


    private static final int MAX_REQNUM =5;
    private int currentPage;
    private GankVideoService mService;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mAdapter=new VideoListAdapter();
        setAdapter(mAdapter);
        setLayoutManager(new LinearLayoutManager(getContext()));
        currentPage=AccessAppDataHelper.readInteger(getActivity(),AccessAppDataHelper.KEY_VIDEO_PAGE,1);
        mService=new GankVideoService(this);
        if (savedInstanceState==null)
        {
            showRefreshProgressbar();
            mService.getVideo(currentPage, MAX_REQNUM);
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
    public void onSuccess(VideoJsonData data) {
        hideRefreshProgressbar();
        mAdapter.addAll(data.getVideoItemList());
        currentPage++;
    }

    @Override
    public void onFailure(String msg) {
        hideRefreshProgressbar();
    }

    @Override
    public void refresh(CommonRecyclerView recyclerView) {
        mAdapter.clear();
        mService.getVideo(currentPage, MAX_REQNUM);
    }

    @Override
    public void loadMore(CommonRecyclerView recyclerView) {
        mService.getVideo(currentPage,MAX_REQNUM);
    }

}
