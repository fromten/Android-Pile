package learn.example.pile.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import learn.example.pile.MyURI;
import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.jsonobject.BaseVideoData;
import learn.example.pile.net.VideoRequestTask;

/**
 * Created on 2016/5/25.
 */
public class VideoFragment extends RecyclerViewFragment implements VideoRequestTask.VideoTaskListener{

    private VideoListAdapter mVideoListAdapter;
    private VideoRequestTask mRequestTask;
    private String TAG="VideoFragment";
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mVideoListAdapter=new VideoListAdapter();
        setRecyclerAdapter(mVideoListAdapter);
        requestNetData(10,2);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if(mRequestTask!=null)
            mRequestTask.setTaskListener(null);
        mRequestTask.cancel(true);
        super.onDestroy();
    }

    public void requestNetData(int reqnum,int page)
    {
        mRequestTask=new VideoRequestTask();
        mRequestTask.setTaskListener(this);
        try {
            String type= URLEncoder.encode("休息视频","utf-8");
            final String url= MyURI.VIDEO_DATA_REQUEST_URL+type+"/"+reqnum+"/"+page;
            mRequestTask.execute(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.e(TAG,"requestNetData method error ");
    }

    @Override
    public void taskComplete(List<BaseVideoData> data) {
         mVideoListAdapter.addItemAll(data);
    }

    @Override
    public void taskError(String msg) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void pullUpRefresh(RecyclerView recyclerView) {

    }
}
