package learn.example.pile.activity.normal;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.VideoView;

import learn.example.pile.R;
import learn.example.pile.activity.base.FullScreenActivity;

/**
 * Created on 2016/8/13.
 *
 *  播放小视屏,Video开启无限循环
 *  Uri uri=getIntent().getData();得到视屏的Url地址
 */
public class ShortVideoActivity extends FullScreenActivity  implements MediaPlayer.OnCompletionListener,MediaPlayer.OnPreparedListener{

    private VideoView mVideoView;
    private boolean inPress=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        mVideoView= (VideoView) findViewById(R.id.video_view);
        mVideoView.setOnPreparedListener(this);

        Uri uri=getIntent().getData();
        if (uri!=null)
        {
            mVideoView.setVideoURI(uri);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action=ev.getAction();
        if (action==MotionEvent.ACTION_DOWN)
        {
            inPress=true;
        }else if (action==MotionEvent.ACTION_MOVE)
        {
            inPress=false;
        }else if (action==MotionEvent.ACTION_UP)
        {
            if (inPress)
            {
                finish();
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
          mVideoView.start();
          mp.setLooping(true);
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        super.onDestroy();
    }
}
