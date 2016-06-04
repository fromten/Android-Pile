package learn.example.pile.net;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import learn.example.pile.jsonobject.VideoJsonData;

/**
 * Created on 2016/6/2.
 */
public class VideoHtmlParser {
    private ExecutorService mService;
    private ParserCompleteListener mListener;

    public VideoHtmlParser(@NonNull  ParserCompleteListener listener)
    {
        mService= Executors.newFixedThreadPool(2);
        mListener=listener;
    }

    public void destroy()
    {
        EventBus.getDefault().unregister(this);
        mListener=null;
        mService.shutdownNow();
    }
    public void addParse(@NonNull  VideoJsonData data)
    {
        if (!EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().register(this);
        }
        for (VideoJsonData.VideoItem item:data.getVideoItemList())
        {
            mService.execute(new ParserHtmlThread(item));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 9)
    public void threadComplete(VideoJsonData.VideoItem item) {
        mListener.ParserComplete(item);
    }

    public interface ParserCompleteListener
    {
        void ParserComplete(VideoJsonData.VideoItem item);
    }
}
