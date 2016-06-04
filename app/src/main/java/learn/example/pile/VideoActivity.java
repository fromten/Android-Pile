package learn.example.pile;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import learn.example.joke.R;
import learn.example.pile.ui.VolumeProgressView;

/**
 * Created on 2016/5/26.
 */
public class VideoActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener,
MediaPlayer.OnErrorListener,MediaPlayer.OnPreparedListener{

    private VideoView mVideoView;
    private MediaController mMediaController;
    private VolumeProgressView mVolumeProgressView;
    private TextView  mLogView;
    private static final String SAVE_PLAYPOS_KEY="SAVE_PLAYPOS_KEY";
    public static final String KEY_VIDEO_URL="KEY_VIDEO_URL";
    private Bundle  saveState;
    private String TAG="VideoActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        initView();
        saveState=savedInstanceState;
        Intent intent=getIntent();
        if(intent!=null)
        {
            Uri uri=Uri.parse(intent.getStringExtra(KEY_VIDEO_URL));
            mLogView.setText("加载中....");
            mVideoView.setVideoURI(uri);
        }else {
            mLogView.setText("无效的对象");
        }
    }


    public void initView()
    {
        mVideoView= (VideoView) findViewById(R.id.video_view);
        mVolumeProgressView= (VolumeProgressView) findViewById(R.id.video_volume);
        mLogView= (TextView) findViewById(R.id.video_logmsg);
        mVolumeProgressView.setRectColor(getResources().getColor(R.color.grey));
        mMediaController=new MediaController(this);
        FrameLayout.LayoutParams layoutParams=new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0,0,0,20);
        mMediaController.setLayoutParams(layoutParams);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        mVideoView.setOnTouchListener(touchListener);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!mVideoView.isPlaying())
            mVideoView.start();
    }

    @Override
    protected void onPause() {
        if(mVideoView.isPlaying())
            mVideoView.pause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //保存现在的播放位置
        outState.putInt(SAVE_PLAYPOS_KEY,mVideoView.getCurrentPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mVideoView.stopPlayback();
        mVideoView=null;
        mMediaController=null;
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

        //跳到旋转之前的播放位置
        if(saveState!=null)
        {
            int playOldPos=saveState.getInt(SAVE_PLAYPOS_KEY);
            mVideoView.seekTo(playOldPos);
        }
        mVideoView.start();
        mLogView.setText(null);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mLogView.setText("播放结束");
    }
    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        mLogView.setText("发生错误了...");
        return true;
    }

    //监听,控制播放的音量
    private View.OnTouchListener touchListener=new View.OnTouchListener() {
        private float mDownY;
        private boolean inPress;
        private boolean inMove;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action=event.getAction();
            if(action==MotionEvent.ACTION_DOWN)
            {
                mDownY=event.getY();
                inPress=true;
                inMove=false;
            }else if(action==MotionEvent.ACTION_MOVE)
            {   //移动增加或减少音量
                float y=event.getY();
                if(Math.abs(mDownY-y)>90)
                {
                    if(mVolumeProgressView.getVisibility()!=View.VISIBLE)
                        mVolumeProgressView.setVisibility(View.VISIBLE);
                    int value=mVolumeProgressView.getCurrentValue();
                    if(mDownY>y)
                    {
                        mVolumeProgressView.setCurrentValue(value+1);
                    }else {
                        mVolumeProgressView.setCurrentValue(value-1);
                    }
                    inPress=false;
                    inMove=true;
                    mDownY=event.getY();
                }
            }else if(action==MotionEvent.ACTION_UP)
            {   inMove=false;
                if(inPress)
                {
                    mMediaController.show();
                }else {
                    mVolumeProgressView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!inMove)
                                mVolumeProgressView.setVisibility(View.INVISIBLE);
                        }
                    },3000);//三秒后移除音量进度栏
                }
            }
            return true;
        }
    };
}
