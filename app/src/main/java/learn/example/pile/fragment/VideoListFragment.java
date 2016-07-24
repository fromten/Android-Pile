package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import java.util.List;

import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.jsonbean.OpenEyeVideo;
import learn.example.pile.net.IService;
import learn.example.pile.net.OpenEyeService;
import learn.example.pile.object.OpenEyes;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends BaseListFragment implements IService.Callback<OpenEyeVideo> {

    private VideoListAdapter mAdapter;


    private OpenEyeService mService;
    private String nextUrl;
    private long nextPushTime;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mAdapter=new VideoListAdapter();
        setAdapter(mAdapter);
        mService=new OpenEyeService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getHotVideo(this);
        }
    }
    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }


    @Override
    public void onSuccess(OpenEyeVideo data) {
        List<OpenEyes.VideoInfo> infos=OpenEyes.buildVideoInfo(data);
        nextUrl=data.getNextPageUrl();
        nextPushTime=data.getNextPublishTime();
        mAdapter.addAll(infos);
        notifySuccess();
    }

    @Override
    public void onFailure(String message) {
        notifyError();
    }

    @Override
    public void onRefresh() {
        long second=nextPushTime- TimeUtil.getTime();
        if (second<=0)
        {
            mAdapter.clear();
            mService.getHotVideo(this);
        }else {
            setRefreshing(false);
        }
    }

    @Override
    public void onLoadMore() {
        if (nextUrl!=null)
         mService.next(nextUrl,this);
    }



}
