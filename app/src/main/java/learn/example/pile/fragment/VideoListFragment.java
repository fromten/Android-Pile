package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.List;

import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.jsonobject.VideoJsonData;
import learn.example.pile.net.GankVideoService;
import learn.example.pile.observer.VideoUrlParseObserver;
import learn.example.pile.util.AccessAppDataHelper;
import learn.example.uidesign.CommonRecyclerView;
import rx.Subscriber;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends BaseListFragment implements GankVideoService.ServiceListener<VideoJsonData>{

    private VideoListAdapter mAdapter;
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
        AccessAppDataHelper.saveInteger(getActivity(),AccessAppDataHelper.KEY_VIDEO_PAGE,currentPage);
        super.onDestroy();
    }

    @Override
    public void onSuccess(VideoJsonData data) {
        hideRefreshProgressbar();
        mAdapter.addAll(data.getVideoItemList());
        currentPage++;

    }

    public String[] getUrls(List<VideoJsonData.VideoItem> list )
    {
        int len=list.size();
        String[] arr=new String[len];
        for (int i = 0; i <len ; i++) {
            arr[i]=list.get(i).getHtmlUrl();
        }
        return arr;
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

//    private Subscriber<String[]> mSubscriber=new Subscriber<String[]>() {
//        @Override
//        public void onCompleted() {
//            Log.d(TAG, "onCompleted() called with: " + "");
//
//        }
//
//        @Override
//        public void onError(Throwable e) {
//            Log.d(TAG, e.toString());
//        }
//
//        @Override
//        public void onNext(String[] strings) {
//            Log.d(TAG, Arrays.toString(strings));
//            List<VideoJsonData.VideoItem> list=mAdapter.getAll();
//            int len=list.size();
//            for (int i = len-1; i>=len-5; i--) {
//                if (TextUtils.equals(strings[2],list.get(i).getHtmlUrl()))
//                {
//                    list.get(i).setFileUrl(strings[0]);
//                    list.get(i).setImgUrl(strings[1]);
//                    mAdapter.notifyItemChanged(i);
//                }
//            }
//        }
//    };

}
