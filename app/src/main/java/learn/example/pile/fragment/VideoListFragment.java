package learn.example.pile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import learn.example.pile.adapters.VideoListAdapter;
import learn.example.pile.jsonbean.VideoJsonData;
import learn.example.pile.net.GankVideoService;
import learn.example.pile.net.IService;
import learn.example.pile.util.AccessAppDataHelper;

/**
 * Created on 2016/5/25.
 */
public class VideoListFragment extends BaseListFragment implements IService.Callback<VideoJsonData> {

    private VideoListAdapter mAdapter;


    private static final int MAX_REQNUM =5;
    private int currentPage;
    private GankVideoService mService;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        mAdapter=new VideoListAdapter();
        setAdapter(mAdapter);
        currentPage=AccessAppDataHelper.readInteger(getActivity(),AccessAppDataHelper.KEY_VIDEO_PAGE,1);
        mService=new GankVideoService();
        if (savedInstanceState==null)
        {
            setRefreshing(true);
            mService.getVideo(currentPage, MAX_REQNUM,this);
        }
    }




    @Override
    public void onDestroy() {
        mService.cancelAll();
        AccessAppDataHelper.saveInteger(getActivity(),AccessAppDataHelper.KEY_VIDEO_PAGE,currentPage);
        super.onDestroy();
    }

    @Override
    public void onSuccess(VideoJsonData data) {
        Log.d("TAG", "onSuccess");
        if (data==null||data.isError()||data.getVideoItemList()==null||data.getVideoItemList().isEmpty())
        {
            Log.d("TAG", "notifyError");
            notifyError();
            return;
        }
        mAdapter.addAll(data.getVideoItemList());
        currentPage++;
        notifySuccess();
    }



    @Override
    public void onFailure(String msg) {
        Log.d("TAG",msg);
         notifyError();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        mService.getVideo(currentPage, MAX_REQNUM,this);
    }

    @Override
    public void onLoadMore() {
        mService.getVideo(currentPage,MAX_REQNUM,this);
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
