package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.List;

import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.factory.OpenEyeVideoFactory;
import learn.example.pile.jsonbean.OpenEyeVideo;
import learn.example.pile.net.IService;
import learn.example.pile.net.OpenEyeService;
import learn.example.pile.object.OpenEyes;
import learn.example.pile.util.TimeUtil;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends BaseListFragment implements IService.Callback<OpenEyeVideo> {


    //save state key
    private static final String KEY_NEXT_URL="next_url";
    private static final String KEY_NEXT_PUSH_TIME="next_push_time";


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
        }else {
            nextUrl=savedInstanceState.getString(KEY_NEXT_URL);
            nextPushTime=savedInstanceState.getLong(KEY_NEXT_PUSH_TIME);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_NEXT_URL,nextUrl);
        outState.putLong(KEY_NEXT_PUSH_TIME,nextPushTime);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mService.cancelAll();
        super.onDestroy();
    }


    @Override
    public void onSuccess(OpenEyeVideo data) {
        List<OpenEyes.VideoInfo> infos=new OpenEyeVideoFactory().getInfoList(data.getIssueList());
        nextUrl=data.getNextPageUrl();
        nextPushTime=data.getNextPublishTime();
        mAdapter.addAll(infos);
        notifySuccess();
        Log.d("tag", "onSuccess");
    }

    @Override
    public void onFailure(String message) {
        notifyError();
        Log.d("tag", "onFailure");
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
        {
            mService.nextVideoList(nextUrl,this);
        }else {
            notifyRequestEnd();
        }
    }

}
